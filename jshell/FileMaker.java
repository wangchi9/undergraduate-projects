package a2;

import java.util.*;

/** The FileMaker class has a static method that creates a File and returns a
 * handle to that File object. The String parameter specifies the path to save
 * the File as well as the filename. The path could be relative, so we also
 * pass in the JShell to retrieve the current working directory.
 *
 */
public class FileMaker {

    /**
     * This method creates a new File. The String parameter specifies the
     * path to save the File as well as the filename.
     *
     * @param  String the path to save the newly created file. The filename is also specified.
     * @param  JShell the command line console. The JShell is needed in case the the path is relative, so we can obtain the current working directory.
     * @return String any error messages from the execution of the command
     */    
    public static File makeFiles(String parameter, JShell j)
    { 
        //create an array list of paths, spliting on " "
        ArrayList<Path> pathArray = Path.whiteSpaceTokenizer(parameter, j.getCurrentWorkingDirectory());
        File f = new File();
        //check if each path is possible and new, stop otherwise
        for (Path n : pathArray) {
            if (!n.parentExists()) {
                System.err.println("Error: Specified pathname is not valid"); 
            }
            if (n.childExists()) {
                System.err.println("Error: Directory(s) or Files(s) already exists");
            }
        }
        
        //if we reached here, paths are fine, make each one.
        for (Path v : pathArray) {
            Directory d = v.getParent();
            f = new File();
            String childName = v.getChildName();
            if (!File.isValidFileName(childName))
            {
                System.err.println("Error: Invalid file name. File names can not contain the following characters: /\\?%*:|\"<>. In addition, file names must end with an extension such as .txt or .html, etc.");
                return null;
            }
            f.setName(childName);
            
            /* If the file does not already exist, then create a new file */
            /* Else return null */
            if (d.getFile(childName) == null)
            {
                d.addFile(f);
                return f;
            }
            else
            {
                return d.getFile(childName);
            }
        }
        return f;
    }
}
