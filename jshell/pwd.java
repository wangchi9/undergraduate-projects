package a2;

/** 
 * This class implements the Command interface and implements the pwd command
 * from the command line. This command shows the current working directory
 * on the command line.
 *
 */
public class pwd implements Command {
    
    /**
     * This method implements the pwd command in the JShell
     *
     * @param  String the parameter from the command line
     * @param  JShell the command line console
     * @return String any error messages from the execution of the command
     */
    @Override
    public String execute(String parameter, JShell j) throws Exception
   
    {
        /* Print out the current working directory */
        return j.getCurrentWorkingDirectory().printDirectoryAbsolutePathname();   
    }
}
