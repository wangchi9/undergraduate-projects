package a2;

/** 
 * This class implements the Command interface and implements the echo command.
 * The echo command takes in a string and will write that string back into
 * the command line prompt. You can write the string into a file using a chevron like so:
 * echo string > text.html
 * You can append the string into the specified file by using a double chevron like so:
 * echo string >> text.html
 *
 */
public class echo implements Command {

    /**
     * This method implements the echo command
     *
     * @param  String the parameter from the command line
     * @param  JShell the command line console
     * @return String any error messages from the execution of the command
     */
    @Override
    public String execute(String parameter, JShell j) throws Exception
    {
        return parameter;
    }
}
