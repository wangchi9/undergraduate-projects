package a2;

import java.util.*;

/** 
 * This class contains static methods to help with common String manipulation
 * functions.
 *
 */
public class StringManipulator {
    
    /**
     * Verified correct
     * This method returns an array list with all the directories in a specified
     * pathname. This can be relative or absolute.
     * For example, support we have the following absolute path:
     * /grandfather/father/son.
     * Calling this function on the string "/grandfather/father/son" will return:
     * ArrayList[0] = "grandfather"
     * ArrayList[1] = "father"
     * ArrayList[2] = "son"
     *
     * This function should also work on "grandfather/father/son"
     * and also "grandfather/father/son/
     * and also "/grandfather/father/son/
     * @param string name of the absolute pathName
     * @return ArrayList<String> with all the directory names
     */
    public static ArrayList<String> getDirectoriesfromPath(String parameter) {
        StringTokenizer t = new StringTokenizer(parameter, "/");
        ArrayList returnArray = new ArrayList();
        if (!parameter.contains("/")) {
            returnArray.add(parameter);
            return returnArray;
        }
        String directory = "";
        if (t.hasMoreTokens()) { directory = t.nextToken("/"); }
        /* If the directory is not empty */
        if (!directory.equals("")) {
            returnArray.add(directory);
            while (t.hasMoreTokens()) {
                directory = t.nextToken("/");
                returnArray.add(directory);
            }
        }
        return returnArray;
    }
    
    /**
     * Verified Correct
     * This method will iterate through the ArrayList that is passed in as a
     * parameter and print it to screen. Each element is preceded by the
     * following text: Element:
     * @param a ArrayList
     */
    public static void printArrayListOutput(ArrayList a)
    {
        Iterator e = a.iterator();
        while (e.hasNext())
        {
            System.out.println("Element: " + e.next().toString());
        }
    }

    /**
     * Verified Correct
     * Return a string that has the last file folder removed.
     * "/grandfather/father/son" returns "/grandfather/father".
     * "/grandfather/father" returns "/grandfather".
     * "/grandfather" returns "/"
     * "/" returns "/"
     * "../grandfather" returns ".."
     * "../../grandfather" returns "../.."
     * "grandfather/father" returns "grandfather"
     * "grandfather" returns ""
     * 
     * @param pathname
     * @return 
     */
    public static String takeOutTheLastFileFolder(String pathname)
    {
        ArrayList arr = getDirectoriesfromPath(pathname);
        if (arr.size() > 0)
        {
            arr.remove(arr.size()-1);
        }
        Iterator e =  arr.iterator();
        String returnStr = "";
        /* If it is an absolute path then it needs to start with / */
        if (pathname.substring(0,1).equals("/"))
        {
            returnStr = "/";
        }
        /* Take the first element */
        if (e.hasNext()) { returnStr = returnStr + e.next(); }
        /* Iterate through all the other elements */
        while (e.hasNext())
        {
            returnStr = returnStr + "/" + e.next();
        }
        return returnStr;
    }

    /**
     * Return a string that has the last file folder removed.
     * "/grandfather/father/son" returns "son"
     * "/grandfather/father" returns "father".
     * "/grandfather" returns "grandfather"
     * "/" returns null (error)
     * "../grandfather" returns "grandfather"
     * "../../grandfather" returns "grandfather"
     * "grandfather/father" returns "father"
     * "grandfather" returns "grandfather"
     * @param pathname
     * @return 
     */
    public static String getLastFileFolder(String pathname)
    {
        if (pathname.contains("/")==false)
        {
            return pathname;
        }
        StringTokenizer t = new StringTokenizer(pathname, "/");
        ArrayList tokenlist = new ArrayList();
        if (!pathname.contains("/")) {
            return tokenlist.get(0).toString();
        }
        String directory = t.nextToken("/");
        /* If the directory is not empty */
        if (!directory.equals("")) {
            tokenlist.add(directory);
            while (t.hasMoreTokens()) {
                directory = t.nextToken("/");
                tokenlist.add(directory);
            }
        }
        return tokenlist.get(tokenlist.size() - 1).toString();
    }
    
    /** 
    * This is a ltrim implementation for a string.  
    * It trims all spaces at only beginning of the String(not end). 
    * e.g.  for input "  Test String  " 
    * result is "Test String  " 
    * Not very commonly used but good for practice exercise for students. 
    * People who are familiar with Oracle may also try to look for this 
    * type of function in java but its not really used much as such in  
    * practical programming world.  
    * @param s 
    * @return 
    */  
    public static String ltrim(String s)
    {  
        int i = 0;  
        while (i < s.length() && Character.isWhitespace(s.charAt(i))) {  
            i++;  
        }  
        return s.substring(i);  
    }

    /** 
    * This is a rtrim implementation for a string.  
    * It trims all spaces at the end of the String. 
    * e.g.  for input "  Test String  " 
    * result is "Test String  " 
    * Not very commonly used but good for practice exercise for students. 
    * People who are familiar with Oracle may also try to look for this 
    * type of function in java but its not really used much as such in  
    * practical programming world.  
    * @param s 
    * @return 
    */  
    public static String rtrim(String s)
    {  
        int i = s.length() -1;  
        while (i >=0 && Character.isWhitespace(s.charAt(i))) {  
            i--;  
        }  
        return s.substring(0,i+1);  
    }
    
}
