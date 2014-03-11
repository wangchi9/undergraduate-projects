package a2;

import java.util.*;
import java.io.*;

/** 
 * This class implements the Command interface and implements the ln command
 * from the command line. This command will link a file to another file or it
 * will link a directory to another directory. You can not link a directory to
 * a file or vice versa. The link is also called a shortcut. The directory
 * or file that it points to is referred to as the target. When you call the
 * method cd on a directory that is a shortcut, the console will open up the
 * target directory instead. When you cat a file shortcut, the console will
 * display the contents of the target file instead.
 *
 */
public class ln implements Command {    
    
    /**
     * This method implements the ln command in the JShell
     *
     * @param  String the parameter from the command line
     * @param  JShell the command line console
     * @return String any error messages from the execution of the command
     */
    @Override
    public String execute(String parameter, JShell j) throws Exception {

        /************************* FILES ********************************/
        /* Parse the parameter with the String Tokenizer and put it into
         * an array */
        ArrayList<String> param = new ArrayList();
        StringTokenizer st = new StringTokenizer(parameter);
        while (st.hasMoreTokens()) {
            param.add(st.nextToken());
        }
        
        if (param.size() != 2)
        {
            return "Invalid use of the ln command. Proper usage: ln [path1] [path2]";
        }
        
        File f1 = File.getFile(param.get(0).toString(), j);
        File f2 = File.getFile(param.get(1).toString(), j);
        
        /* Both parameters are files */
        if (f1!=null && f2!=null)
        {
            f1.setShortCut(f2);
            return "The file '" + f1.getName() + "' has been set as a shortcut to the file '" + f2.getName() + "'";
        }
        
        /************************* DIRECTORIES **************************/
        //create an array list of paths, spliting on " "
        ArrayList<Path> pathArray = Path.whiteSpaceTokenizer(parameter, j.getCurrentWorkingDirectory());
        
        /* The shortcut is the Directory pointer */
        Directory shortcut = new Directory("");
        
        /* The target is the Directory that the pointer points to */
        Directory target = new Directory("");
        
        /* It is expecting two paths as paramters. ln PATH1 PATH2 */
        if (pathArray.size() != 2)
        {
            return "Invalid use of the ln command. The JShell is expecting" +
                    "two paths as parameters.\n Correct usage: " +
                    "ln /grandfather/daughter /grandfather/son\nWill link the two directories";
        }
                
        Path n1 = pathArray.get(0);
        Path n2 = pathArray.get(1);
        
        /* Assuming that we are only working with directories, get the shortcut */
        if (!n1.parentExists())
        {
            return "Error: Specified pathname " + n1.getChildName() + " is not valid";
        }
        if (n1.childExists())
        {
            shortcut = n1.getChildDirectory();
        }
        
        /* Assuming that we are only working with directories, get the target */

        if (!n2.parentExists())
        {
            return "Error: Specified pathname " + n2.getChildName() + " is not valid";
        }
        if (n2.childExists())
        {
            target = n2.getChildDirectory();
        }
        
        /* If both the target and the shortcut exist */
        if (target.getName() != "" && shortcut.getName() != "")
        {
            /* Set the shortcut variable for the shortcut Directory */
            shortcut.setShortCut(target);
            /* Print message to screen */
            return "The directory '" + shortcut.getName() + "' is set to be a shortcut to the directory '" + target.getName() + "'";
        }
        else    
        {
            return "The directories could not be found. No shortcut/link is created.";
        }
    }
}
