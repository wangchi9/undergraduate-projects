package a2;

import java.io.*;
import java.util.*;

/** 
 * This class implements the Command interface and implements the man command
 * from the command line. This is the command line console manual for users
 * to read. It has instructions on the syntax for each of the commands.
 *
 */
public class man implements Command {

    /* Hold the manual inside a hashtable */
    Hashtable<String, String> manual;
    
    /* Constructor */
    public man()
    {
        manual = new Hashtable<String,String>();
        manual.put("exit","Command: exit\n\nQuit the program.");
        manual.put("mkdir", "Command: mkdir DIR\n\nCreate directories, each of which may be relative to the current directory or may be a full path. ");
        manual.put("cd", "Command: cd\n\nChange directory to DIR, which may be relative to the current directory or may be a full path. As with Unix, .. means a parent directory and . means the current directory. The directory separator must be /, the forward slash. The root of the file system is a single slash: /.");
        manual.put("ls","Command: ls\n\nls [-R] [PATH ...]\nAddition: if -R is present, recusively list subdirectories.\nIf no paths are given, print the contents of the current directory, with a new line following each of them.\nOtherwise, for each path p, in the order listed\nIf p specifies a file, print p.\nIf p specifies a directory, print p, a colon, then the contents of that directory, then an extra newline.\nIf p does not exist, print a suitable message.");
        manual.put("pwd","Command: pwd\n\nPrint the current working directory (including the whole path)");
        manual.put("mv", "Command: mv\n\nmv OLDPATH NEWPATH\nMove item OLDPATH to NEWPATH. Both OLDPATH and NEWPATH may be relative to the current directory or may be full paths. If NEWPATH is a directory, move the item into the directory.");
        manual.put("cp","Command: cp\n\ncp OLDPATH NEWPATH\n\nLike mv, but don't remove OLDPATH. If OLDPATH is a directory, recursively copy the contents.");
        manual.put("cat", "Command: cat\n\n cat FILE\n\nDisplay the contents of FILE in the shell.");
        manual.put("get", "Command: get\n\n get URL\n\nURLURL is a web address. Retrieve the file at that URL and add it to the current directory.");
        manual.put("echo", "Command: echo STRING\n\nPrint STRING.");
        manual.put("rm", "Command: rm\n\nrm [-f] PATH ...Confirm with the user that they want to delete PATH, and if so, remove it from the file system. If PATH is a directory, recursively remove all files and directories in it, prompting for confirmation for each one. If -f is supplied, do not confirm: just remove. ");
        manual.put("ln", "Command: ln\n\nln PATH1 PATH2\n\nMake PATH1 a symbolic link to PATH2. Both PATH1 and PATH2 may be relative to the current directory or may be full paths. PATH1 is a synonym for PATH2. if PATH2 is moved, PATH1 will continue to be a synonym for it. If PATH2 is deleted or moved, then PATH1 will still exist but is invalid. Clarification: these symbolic links do not have to track a file or directory if it is moved.");
        manual.put("man", "Command: man\n\nCMDPrint documentation for CMD."); 
    }
    
    /**
     * This method implements the man command in the JShell
     *
     * @param  String the parameter from the command line
     * @param  JShell the command line console
     * @return String any error messages from the execution of the command
     */
    @Override
    public String execute(String parameter, JShell j) throws Exception
    {
        /* return the manual for the command */
        if (manual.containsKey(parameter.toLowerCase()))
        {
            return manual.get(parameter);
        }
        else
        {
            return "Error: The command specified does not exist";
        }
    }
}
