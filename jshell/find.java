/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package a2;

/**
 *
 */

import java.util.*;

public class find implements Command {
    /**
     * find recursively search the parameter for file names containing REGEX 
     * then it print them (including their path), one per line. 
     * @param String parameter
     * @param j is the JShell class that is managing the directory tree.
     * @throws Exception 
     */

    @Override
    public String execute(String parameter, JShell j) throws Exception {
        String returnText = "";
        /* The regular expression should not be surrounded by double quotes */
        if (parameter.contains("\"") == true)
        {
            return "Error: Do not surround the regular expression with quotations";
        }
        
        /* The regular expression should not be surrounded by single quotes */
        if (parameter.contains("\"") == true)
        {
            return "Error: Do not surround the regular expression with quotations";
        }
        
        parameter = StringManipulator.ltrim(parameter);
        /* Get the regular expression. It is the first parameter */
        String regex;
        int i = parameter.indexOf(" ");
        if (i == -1)
        {
            //return "Error: no regular expression was provided.";
            regex = parameter;
        }
        else
        {
            regex = parameter.substring(0, i);
        }
        
        /*Remove the regular expression from the parameter so that we only have paths */
        parameter = parameter.replace(regex, "");
        parameter = parameter.replace(" ", "");
        parameter = parameter.trim();
        
        if (parameter.compareToIgnoreCase("") == 0)
        {
            returnText = regularExpressionCheck(j.getCurrentWorkingDirectory(), regex);
        }
        else
        {
            /* Get the pathArray without the regular expression */
            ArrayList<Path> pathArray = Path.whiteSpaceTokenizer(parameter, j.getCurrentWorkingDirectory());
        
            //check if each path is possible and new, stop otherwise
            for (Path n : pathArray)
            {
                if (!n.parentExists())
                {
                    return "Error: Specified pathname is not valid";
                }
            
                if (n.childExists())
                {
                    Directory child = n.getChildDirectory();
                    returnText = regularExpressionCheck(child, regex);
                }
            }
        }
     return returnText;
    }
    /**
     * This method will return the results from a regular expression.
     * @param child
     * @param regex
     * @return String
     */
    private String regularExpressionCheck(Directory child, String regex)
    {
        String returnText = "";
        Iterator iter = child.getChildrenFoldersAndFiles().iterator();
        while (iter.hasNext())
        {
            FileSystem fs = (FileSystem)iter.next();
            if (RegularExpressionEngine.regularExpressionMatch(regex, fs.getName()) == true)
            {
                String text = child.printDirectoryAbsolutePathname() + fs.getName();
                if (returnText.compareTo("") ==0)
                {
                    returnText = returnText + text;
                }
                else
                {
                    if (!"".equals(text))
                    {
                        returnText = returnText + "\n" + text;
                    }
                }
            }
            
            /* Look recursively in all the directories as well */
            if (fs instanceof Directory)
            {
                String t = regularExpressionCheck((Directory)fs, regex);
                if (returnText.compareTo("") ==0)
                {
                    returnText = returnText + t;
                }
                else
                {
                    if (!"".equals(t))
                    {
                        returnText = returnText + "\n" + t;
                    }
                }
            }
        }
        return returnText;
    }
}
