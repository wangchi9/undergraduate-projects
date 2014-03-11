
/*
 * 
 */
package a2;

import java.io.*;
import java.util.*;

/** JShell is the shell that the user interfaces with. It provides functionality
 * to read and write to the screen console
 *
 */
public class JShell {

    /**
     *  The current working directory
     */
    private Directory currentWorkingDir;
    private Directory rootDirectory;

    /**
     * Constructor for the JShell        
     *
     * @param the initial current working directory
     * @return void
     */
    public JShell(Directory cur, Directory root) {
        this.currentWorkingDir = cur;
        this.rootDirectory = root;
    }

    /**
     * Returns the Current Working Directory
     * @return Directory Current Working Directory
     */
    public Directory getCurrentWorkingDirectory() {
        return this.currentWorkingDir;
    }

    /**
     * Returns the root Directory
     * @return Directory Root
     */
    public Directory getRoot() {
        return this.rootDirectory;
    }

    /**
     * Set the given directory as the current working directory
     * @param A Directory
     */
    public void setCurrentWorkingDirectory(Directory current) {
        this.currentWorkingDir = current;
    }

    /**
     * Set the given directory as the root directory
     * @param A Directory
     */
    public void setRootDirectory(Directory current) {
        this.rootDirectory = current;
    }

    /**
     * Print given message string onto new line in the shell
     * @param A String
     */
    public void printLine(String message) {
        System.out.println(message);
    }

    /**
     * Print given error string onto new line in the shell
     * @param A String
     */
    public void printError(String error) {
        System.err.println(error);
    }

    /**
     * This method polls the user in an infinite loop until the user
     * enters "exit"
     *
     *
     * @return void
     */
    public void runShell() {
        //Create a hash table of possible commands
        Hashtable<String, Command> hash = new Hashtable<String, Command>();
        hash.put("rm", new rm());
        hash.put("mkdir", new mkdir());
        hash.put("cd", new cd());
        hash.put("pwd", new pwd());
        hash.put("ls", new ls());
        hash.put("man", new man());
        hash.put("get", new get());
        hash.put("mv", new mv());
        hash.put("echo", new echo());
        hash.put("cp", new cp());
        hash.put("ln", new ln());
        hash.put("cat", new cat());
        hash.put("find", new find());

        String userInput = "";

        String command;
        String parameter;
        //"exit" ends current user shell session
        while (!userInput.equals("exit")) {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            try {
                String fileToCopyTo = "";
                System.out.print(currentWorkingDir.printDirectoryAbsolutePathname() + "# ");
                userInput = br.readLine();
                userInput.replace("\n", "");
                int index = userInput.indexOf(" ");
                if (index != -1) {
                    command = new String(userInput.substring(0, index));
                    parameter = new String(userInput.substring(index + 1, userInput.length()));
                } /* If there are no parameters */ else {
                    command = userInput;
                    parameter = "";
                }

                /* We need to deal with the chevrons: > and >> */
                /* If we have no chevrons then process the file normally */
                int numberOfSingleChevrons = File.countNumberOfOccurences(parameter, ">");
                int numberOfDoubleChevrons = File.countNumberOfOccurences(parameter, ">>");
                File f = new File();
                if (numberOfSingleChevrons != 0) {
                    /* If there are three or more chevrons, then we have a
                     * problem.
                     * This is invalid: echo This is a string output > file >>
                     */
                    if (numberOfSingleChevrons > 2) {
                        throw new Exception("Too many chevrons");
                    }

                    /* If we have two chevrons, they must be side-by-side. */
                    if ((numberOfSingleChevrons == 2) && (numberOfDoubleChevrons == 0)) {
                        throw new Exception("The two chevrons must be next to each other.");
                    }
                    parameter = parameter.replace(" ", "");
                    String fullParameter = parameter;
                    int indexPtr = parameter.indexOf(">");
                    parameter = StringManipulator.ltrim(fullParameter.substring(0, indexPtr).trim());
                    int cursor = 1;
                    if (numberOfDoubleChevrons == 1) {
                        cursor = 2;
                    }
                    int fullParameterLength = fullParameter.length();
                    int parameterLength = parameter.length();
                    fileToCopyTo = fullParameter.substring(indexPtr + cursor, indexPtr + fullParameterLength - parameterLength);
                    /* Returns a handle to a new file created for an existing file with the fileToCopyTo name */
                    /* If the file 'fileToCopyTo' is a shortcut to another file, then copy it there */
                    File fi = (File.getFile(fileToCopyTo, this));
                    /* No shortcut? Then just create a new file and copy the contents there */
                    if (fi == null) {
                        f = FileMaker.makeFiles(fileToCopyTo, this);
                    } /* There is a shortcut, so then put the contents into the target */ else {
                        if (fi.getShortCut() != null) {
                            f = (File) fi.getShortCut();
                        } else {
                            f = FileMaker.makeFiles(fileToCopyTo, this);
                        }
                    }
                }

                Command c = hash.get(command);
                /* If the command is not in the hashtable */
                if (c != null && (!command.equals("exit"))) {
                    String returnStr = c.execute(parameter, this);
                    if (returnStr.compareToIgnoreCase("") != 0) {
                        if (f.getName() != null) {
                            if (numberOfDoubleChevrons == 1) {

                                f.contents = f.contents + returnStr;
                            } else {
                                f.contents = returnStr;
                            }
                        } else {
                            printLine(returnStr);
                        }
                    }
                } else {
                    if (command.compareToIgnoreCase("exit") != 0) {
                        printError("Error: Command not recognized");
                    }
                }
            } catch (IOException i) {
                printError("Error: IO error trying to read user command.");
            } catch (Exception e) {
                printError("Syntax error.");
            }
        }
        printLine(" ");
        printLine("Exiting JShell....");
    }

    public static void main(String[] args) {


        Directory root = new Directory("root");



        JShell j;
        j = new JShell(root, root);
        j.runShell();



    }
}
