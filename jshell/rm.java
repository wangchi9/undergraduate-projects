package a2;

import java.util.*;
import java.io.*;

/** 
 * This class implements the Command interface and implements the rm command
 * from the command line. It removes the file folder or directory.
 */
public class rm implements Command {

    Boolean promptUser;
    Boolean delete;
    

    /**
     * This method implements the rm command in the JShell
     *
     * @param  String the parameter from the command line
     * @param  JShell the command line console
     * @return String any error messages from the execution of the command
     */
    @Override
    public String execute(String parameter, JShell j) throws Exception {
        promptUser = true;
        delete = true;
        /* If -f exists in the command line parameter */
        parameter = parameter.trim();
        if ((parameter.indexOf("-f ") != -1) || (parameter.indexOf("-F ") != -1)) {
            promptUser = false;
            parameter = parameter.replace("-f ", "");
        }

        /* create an array list of paths, spliting on " " */
        ArrayList<Path> pathArray = Path.whiteSpaceTokenizer(parameter, j.getCurrentWorkingDirectory());

        /* check if each path is possible, stop otherwise */
        for (Path n : pathArray) {
            /* The directory or filename is not valid because it is not contained within a directory */
            if (!n.parentExists()) {
                return "Error: Specified pathname is not valid";
            }
            /* The directory or filename is not valid because it is not contained within a directory */
            if (!n.childExists()) {
                return "Error: Directory(s) or Files(s) do not exists";
            }
            /* Cannot delete root */
            if (n.getChildName().equals("/")) {
                return "Error: Specified pathname is not valid";
            }
        }

        if (Path.checkDouble(pathArray) == true) {
            return "Error: At least two specified pathnames are the same";
        }

        for (Path v : pathArray) {
            if (v.childExists()) {
                Directory parent = v.getParent();
                //if the item we're deleting is a directory
                if (parent.has(v.getChildName()).equals("D")) {
                    Directory toDelete = v.getChildDirectory();
                    recursiveRemove(v.getChildDirectory(), j);
                    String path = toDelete.printDirectoryAbsolutePathname();

                    if (promptUser) {
                        prompt(path, j);
                    }
                    if (delete) {
                        v.getParent().remDirectory(toDelete);
                    }

                } //otherwise its a file
                else {
                    File toDelete = v.getChildFile();
                    String path = v.getParent().printDirectoryAbsolutePathname() + v.getChildName();
                    if (promptUser) {
                        prompt(path, j);
                    }
                    if (delete) {
                        v.getParent().remDirectory(toDelete);
                    }
                }
            }
        }
        return "";
    }

    private Directory recursiveRemove(Directory parent, JShell j) {
        ArrayList<FileSystem> children = parent.getChildrenFoldersAndFiles();
        ArrayList<Directory> dirArray = new ArrayList();
        ArrayList<FileSystem> delArray = new ArrayList();

        for (int i = 0; i <= (children.size() - 1); i++) {
            String name = children.get(i).getName();
            if (parent.has(name).equals("D")) {
                recursiveRemove(parent.getDirectory(name), j);
            }
            if (promptUser) {
                prompt(parent.printDirectoryAbsolutePathname() + name, j);
            }
            if (delete) {
                dirArray.add(parent);
                delArray.add((FileSystem) children.get(i));
            }
        }

        for (int k = 0; k <= (delArray.size() - 1); k++) {
            Directory dir = dirArray.get(k);
            dir.remDirectory(delArray.get(k));
        }
        return parent;
    }

    private void prompt(String name, JShell j) {
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        /* Create a BufferedReader object which will read the user input for 'y' to delete */
        String userInput = "";
        j.printLine("Are you sure you want to delete " + name + "? (y/n)");
        try {
            userInput = input.readLine();
            userInput.replace("\n", "");
            if (userInput.contains("y") || userInput.contains("Y")) {
                delete = true;
            }
            if (userInput.contains("n") || userInput.contains("N")) {
                delete = false;
                promptUser = false;
            }
        } catch (IOException i) {
            j.printError("Error: IO error trying to read user command.");
            delete = false;

        } catch (Exception e) {
            j.printError("Syntax Error ");
            delete = false;
        }

    }
}
