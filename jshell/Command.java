package a2;

/** The Command interface is implemented by all the commands in the JShell.
 * There is only one method that needs to be implemented: execute.
 * It takes in a String parameter which is always after the command line.
 * The String parameter can contains one or more path names.
 * 
 */
public interface Command {
    public String execute(String parameter, JShell j) throws Exception;
}
