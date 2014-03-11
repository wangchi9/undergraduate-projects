/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package a2;

import java.util.*;

/**
 *
 */
public class cp implements Command {
    /**     
     * This class cp copies the file from oldpath into new path. If the 
     * oldpath refers to a directory it copies into the new path while 
     * recursively copy the content.
     * @param String parameter representing either a path name, relative or
     * absolute
     * @param j is the JShell class that is managing the directory tree.
     * @throws Exception 
     */
    @Override
    public String execute(String parameter, JShell j) throws Exception {

        ArrayList<Path> pathArray = Path.whiteSpaceTokenizer(parameter, j.getCurrentWorkingDirectory());

        //must have two paths
        if (pathArray.size() != 2) {
            return "Error: Two paths expected:";
        }

        Path oldPath = pathArray.get(0);
        Path newPath = pathArray.get(1);

        //test if old path parent exists
        if (!oldPath.parentExists()) {
            return "Error: Old path does not exists";
        }
        //test if new path exists
        if (!newPath.childExists()) {
            return "Error: New path does not exists";
        }

        Directory oldHome = oldPath.getParent();
        String copyName = oldPath.getChildName();
        
        if (oldPath.getChildName().equals("/")){
             Directory newHome = newPath.getChildDirectory();

            //double check we're not adding two directories of the same name
            if (newHome.hasDirectory("root")) {
                return "Error: 'root' already exists in " + newHome.printDirectoryAbsolutePathname();
            }

            //create a new copy of the directory, and all of its contents
            Directory newCopy = j.getRoot().cloning();

            //move new copy to its new home
            newHome.addDirectory(newCopy);

            return "";

        }

        //test is the directory we're looking for exists
        if (oldHome.hasDirectory(copyName)) {

            Directory newHome = newPath.getChildDirectory();

            //double check we're not adding two directories of the same name
            if (newHome.hasDirectory(copyName)) {
                return "Error: " + copyName + " already exists in " + newHome.printDirectoryAbsolutePathname();
            }

            //create a new copy of the directory, and all of its contents
            Directory newCopy = oldHome.getDirectory(copyName).cloning();

            //move new copy to its new home
            newHome.addDirectory(newCopy);

            return "";

            //test is the file we're looking for exists
        } else if (oldHome.hasFile(copyName)) {


            Directory newHome = newPath.getChildDirectory();

            //double check we're not adding two files of the same name
            if (newHome.hasFile(copyName)) {
                return "Error: " + copyName + " already exists in " + newHome.printDirectoryAbsolutePathname();
            }

            //create a new copy of the file
            File oldCopy = oldHome.getFile(copyName);
            File newCopy = new File();
            newCopy.setName(oldCopy.getName());
            String contents = oldCopy.getContents();
            newCopy.setContents(contents);

            //move new copy to its new home
            newHome.addFile(newCopy);


            return "";
        }

        return "Error: " + copyName + " does not exist.";
    }
}
