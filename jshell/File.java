package a2;

import java.util.regex.*;
import java.util.*;

/** The File class represents a File in our JShell
 * A file has string contents. It also has a filename which is inherited from FileSystem.
 * A file is contained in a Directory. This property is also inherited from FileStystem.
 * A file can be a shortcut or link to another file.
 * When a link is deleted, the file that it links to persists.
 * A cat on a file shortcut will display the contents of the file that it links to.
 * 
 */
public class File extends FileSystem {

    String contents;

    /**
     * Replace contents of file
     * @param Replace contents of string
     */
    public void setContents(String s) {
        contents = s;
    }

    /**
     * Obtain the contents of the File
     *
     * @return String the contents for the file
     */
    public String getContents() {
        return contents;
    }

    /**
     * Returns true if this method returns a valid file name
     * A valid file name can not contain any of the following characters:
     * /\?%*:|\"<>
     * Moreover, it must contain at least one dot.
     *
     * @param string the filename to test
     * @return boolean is this a valid file name?
     */
    public static boolean isValidFileName(String parameter) {
        /* Set up the regex */
        Pattern p = Pattern.compile("[/\\?%*:|\"<>]");
        Matcher m = p.matcher(parameter);

        /* The filename must not contain any of these characters: /\?%*:|\"<>
         * Moreover, it must also contain exactly one period
         */
        if (m.find() || countNumberOfOccurences(parameter, ".") != 1) {
            return false;
        }

        /* In addition, there must be exactly three or four characters after the
         * period.
         */
        int numberOfCharactersAfterPeriod = parameter.length() - parameter.indexOf(".") - 1;
        if (numberOfCharactersAfterPeriod != 3 && numberOfCharactersAfterPeriod != 4) {
            return false;
        }

        /* The last rule is that the period can not be the first character */
        if (parameter.indexOf(".") == 0) {
            return false;
        }

        /*If it passes all the tests above, then it must be a valid file name */
        return true;
    }

    /**
     * Returns the number of times a character appears in a String
    
     *
     * @param String the string to test
     * @param Char the character to test
     * @return int the number of times the character appears in the String
     */
    public static int countNumberOfOccurences(String parameter, String lookFor) {
        int lastIndex = 0;
        int count = 0;
        while (lastIndex != -1) {
            lastIndex = parameter.indexOf(lookFor, lastIndex);
            if (lastIndex != -1) {
                lastIndex++;
                count++;
            }
        }
        return count;
    }

    /**
     * Get a handle to a File based on an absolute or relative path.
     * If no file is found, then this method will return null.
     *
     * @param String the absolute or relative path of the File
     * @return File the File
     */
    public static File getFile(String parameter, JShell j) {
        /* Trim the left space */
        parameter = StringManipulator.ltrim(parameter);

        /* Trim the right space */
        parameter = StringManipulator.rtrim(parameter);

        /* There must be no empty spaces within the File name */
        if (parameter.indexOf(" ") != -1) {
            return null;
        }

        /* The simple case: If parameter contains a valid filename.
         * For example: parameter = "file.txt"
         *
        /* Is this a valid file name? */
        if (File.isValidFileName(parameter)) {
            /* Does the file exist in the current working directory? */
            if (j.getCurrentWorkingDirectory().hasFile(parameter)) {
                /* Return the contens of the file */
                Directory cwd = j.getCurrentWorkingDirectory();
                File f = cwd.getFile(parameter);
                return f;
            }
        }

        /* The complex case: If the parameter contains a directory and filename
         * For example: parameter = "/grandfather/Filefather/file.txt"
         * filename = "file.txt"
         * container = /grandfather/father
         */
        String filename = StringManipulator.getLastFileFolder(parameter);
        String container = StringManipulator.takeOutTheLastFileFolder(parameter);

        /* Check to see if the filename is a valid */
        if (File.isValidFileName(filename) == false) {
            return null;
        }

        /* Get the directory and is it valid and does it exist? */
        Path pathname = new Path(container, j.getCurrentWorkingDirectory());
        if (!pathname.childExists()) {
            return null;
        }

        /* Get a handle to the File from the directory */
        Directory d = pathname.getChildDirectory();
        File f = d.getFile(filename);
        return f;
    }
}
