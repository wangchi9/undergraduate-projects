/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package a2;

import java.util.*;

/**
 */
public class mv implements Command {

    @Override
    /**
     * Executes the mv command
     *
     * @param  The string parameter representing either a path name, relative or
     * absolute and and the JShell class that is managing the directory tree.
     */
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
            return "Error: Old path does not exits";
        }
        //test if new path exists
        if (!newPath.childExists()) {
            return "Error: New path does not exits";
        }
        
        //test if the two paths are the same
        if (Path.checkDouble(pathArray)){
            return "Error: Both paths are the same";
        }

        Directory oldHome = oldPath.getParent();
        String tenantName = oldPath.getChildName();
        
        if (tenantName.equals("/")) {
            return "Error: Cannot move root";
        }

        //test is the directory we're looking for exists
        if (oldHome.has(tenantName).equals("D")) {
            Directory tenant = oldHome.getDirectory(tenantName);
            Directory newHome = newPath.getChildDirectory();

            //double check we're not adding two directories of the same name
            if (newHome.has(tenant.getName()) != null) {
                return "Error: " + tenantName + " already exists in " + newHome.printDirectoryAbsolutePathname();
            }

            //move into the new home, clean out the old
            newHome.addDirectory(tenant);
            oldHome.remDirectory(tenant);

            return "";
            
        //test is the file we're looking for exists
        } else if (oldHome.has(tenantName).equals("F")) {
            File tenant = oldHome.getFile(tenantName);
            Directory newHome = newPath.getChildDirectory();

            //double check we're not adding two files of the same name
            if (newHome.has(tenant.getName()) != null) {
                return "Error: " + tenantName + " already exists in " + newHome.printDirectoryAbsolutePathname();
            }

            //move into the new home, clean out the old
            newHome.addFile(tenant);
            oldHome.remDirectory(tenant);

            return "";
        }

        return "Error: " + tenantName + " does not exist.";
    }
}
