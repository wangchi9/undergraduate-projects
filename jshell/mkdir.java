package a2;

import java.util.*;

/** 
 * This class will create a directory at the given path.
 * The path can be absolute or relative to the current working directory.
 *
 */
public class mkdir implements Command {
    

    /**
     * This method implements the mkdir command in the JShell
     *
     * @param  String the parameter from the command line
     * @param  JShell the command line console
     * @return String any error messages from the execution of the command
     */
    @Override
    public String execute(String parameter, JShell j) throws Exception {
        
        /* If the parameter is an empty string */
        if (parameter.trim().compareTo("") ==0 )
        {
            return "Error: Specified pathname is not valid";
        }

        /*create an array list of paths, spliting on " " */
        ArrayList<Path> pathArray = Path.whiteSpaceTokenizer(parameter, j.getCurrentWorkingDirectory());
        
        /* check if each path is possible and new, stop otherwise */
        for (Path n : pathArray) {
            if (!n.parentExists()) {
                return "Error: Specified pathname is not valid";
                
            }
            if (n.childExists()) {
                return "Error: Directory(s) or Files(s) already exists";
            }
            if (n.getChildName().equals("/")){
                return "Error: Specified pathname is not valid";
            }
        }
        
        if (Path.checkDouble(pathArray) == true) {
            return "Error: At least two specified pathnames are the same";
        }
        
        /* if we reached here, paths are fine, make each one. */
        for (Path v : pathArray) {
            Directory parent = v.getParent();
            Directory child = new Directory(v.getChildName());
            parent.addDirectory(child);
        }
        return "";
    }
}
