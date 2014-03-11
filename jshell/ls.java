/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package a2;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.*;

/**
 * ls prints the contents of a given directory, or if a file is given 
 * it will print its name. Also if a -R command is given a recursive print
 * call will be instantiated. it will return the contents a given directory 
 * and also the contents within the directories recursively
 */
public class ls implements Command {
   

    @Override
    /*
     * Executes the ls command
     * @param String parameter to be used as an argument and the JShell
     * in which the command is located in.
     */
    public String execute(String parameter, JShell j) throws Exception {
        String returnstr = "";
        // if the given ls command is empty print the cwd
        if (parameter.compareToIgnoreCase("") == 0) {
            return _print(j.getCurrentWorkingDirectory(), j) + "\n";
        }

        // seperate if recursive fcn is specified
        if (parameter.contains("-R") == true) {
            // check if after the recursive call the given path is empty
            // if so print cwd recursively
            if (parameter.length() == 2) {
                return   _recursive_print(j.getCurrentWorkingDirectory(), j);
            } 
            // otherwise tokenize given paths and recurse through them seperatly
            else {
                ArrayList<Path> pathArray = Path.whiteSpaceTokenizer(
                        parameter.substring(2, parameter.length()), 
                        j.getCurrentWorkingDirectory());
                returnstr = "";
                for (Path n : pathArray) {
                    // the tokenizer will turn paths given as 
                    //" / " into a directory
                    //linked to the root
                    if (n.getChildDirectory() instanceof Directory) {
                        returnstr += _recursive_print(n.getChildDirectory(), j);
                    }
                    
                    // make sure files are still printed
                    else {
                        if (n.childExists()) {
                            returnstr += "Error: cannot recurse through File" 
                                    + "\n";
                        } else {
                            returnstr += "Error: Path does not exist." 
                                    + "\n";
                        }
                    }
                }return returnstr;
            }

        } else {

            ArrayList<Path> pathArray = Path.whiteSpaceTokenizer(
                    parameter, j.getCurrentWorkingDirectory());
            //check if each path is possible and new, stop otherwise
            // forward slash's will be converted to root calls 
            //and printed as such
            
            for (Path n : pathArray) {
                
                    if (n.childExists()) {
                        if(n.getChildFile()!=null){
                        returnstr += _print(n.getChildFile(), j) + "\n" ;}
                        else{
                            returnstr += _print(n.getChildDirectory(), j) 
                                    + "\n" ;}
                    }
                        else {
                        returnstr += "Error: Path does not exist." + "\n";
                    }
            }
            return returnstr;
        }

    }

    /*
     * @param prints given Directory or file's contents to jshell
     */
    // print fcn works for Files and directories
    private String _print(FileSystem fs, JShell s) {
        FileSystem ptr;
        String returnString = "";
        if (fs instanceof Directory) {
            Directory dir = (Directory) fs;
            ArrayList list = dir.getChildrenFoldersAndFiles();
            Iterator iterator = list.iterator();
            //checks while there are more filesystem in the cwd
            returnString += dir.printDirectoryAbsolutePathname() + ":";
            while (iterator.hasNext()) {
                ptr = (FileSystem) iterator.next();
                returnString += " " + ptr.getName();
            }
        } // if the Filesystem is a file type print it's name
        else {
            returnString += fs.getName();
        }
        return returnString ;
    }
    
    /*@param prints given directory to jshell and the recurses through the
     * items within it, prints any File objects directly to print to jshell
     *  
     */
    private String _recursive_print(FileSystem fs, JShell j) {

        FileSystem prr;
        String returnstring = "";
        if (fs instanceof Directory) {
            Directory dir = (Directory) fs;
            // print the directory
            returnstring += _print(dir,j) + "\n"+ "\n";
            ArrayList list = dir.getChildrenFoldersAndFiles();
            Iterator iterator = list.iterator();
            //recurse through items it contains
            while (iterator.hasNext()) {
                prr = (FileSystem) iterator.next();
                if (prr instanceof Directory){
                returnstring +=  _recursive_print((Directory) prr, j) ;
            }}
        } // if the FileSystem is a file just return its name
        else {
            returnstring += fs.getName();
        }
        return returnstring;
    }
}
