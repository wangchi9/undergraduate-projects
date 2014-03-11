package a2;

/**
 *
 */
public class cd implements Command {
    /*
     * Command class for cd
     * @param String parameter to be used as an argument and the JShell
     * in which the command is located in.
     */

    @Override
    /*
     * Executes the cd command
     * @param String parameter to be used as an argument and the JShell
     * in which the command is located in.
     */
    public String execute(String parameter, JShell j) throws Exception {
        //cases for special command arguments.        
        if (parameter.equals("/") || parameter.equals("~")) {
            j.setCurrentWorkingDirectory(j.getRoot());
        }

        Path pathname = new Path(parameter, j.getCurrentWorkingDirectory());
        if (pathname.childExists()) {
            /* If it is a link, then set the current working directory to the target */
            FileSystem cwd = pathname.getChildDirectory().getShortCut();
            
            /* If it is not a shortcut, then just change the working directory */
            if (cwd == null)
            {
                j.setCurrentWorkingDirectory(pathname.getChildDirectory());
            }
            
            /* Else if it is a shortcut, then set the current working directory to the target */
            else
            {
                j.setCurrentWorkingDirectory((Directory)cwd);
            }
        } else {
            return "Error: The specified pathname is not a valid directory";
        }
        return "";
    }
    
}
