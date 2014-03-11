package a2;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

/** 
 * This class contains the regularExpressionMatch to help with matching regular
 * expressions.
 *
 */
public class RegularExpressionEngine
{
    /**
     * This method returns true if a file name matches a regular expression
     *
     * @param String the regular expression
     * @param String the file name to match
     * @return ArrayList<String> with all the directory names
     */
    public static boolean regularExpressionMatch(String regex, String fileSystemName)
    {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(fileSystemName);
        boolean ret = matcher.find();
        return ret;
    }
}
