package a2;

/** 
 * This class implements the Command interface and implements the cat command.
 * usage: cat FILE
 * Display the contents of FILE in the shell.
 *
 */
public class cat implements Command {

    /**
     * This method implements the cat command
     *
     * @param  String the parameter from the command line
     * @param  JShell the command line console
     * @return String any error messages from the execution of the command
     */
    @Override
    public String execute(String parameter, JShell j) throws Exception {
        Path filePath = new Path(parameter, j.getCurrentWorkingDirectory());
        if (!filePath.childExists()) {
            return "Error: File not found";
        } else {
            File f = filePath.getChildFile();
            if (f.getShortCut() == null) {
                return f.getContents();
            } /* Return the shortcut instead */ else {
                return ((File) f.getShortCut()).getContents();
            }
        }
    }
}
