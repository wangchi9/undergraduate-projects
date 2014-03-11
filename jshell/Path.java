package a2;

import java.util.*;

/** 
 * The Path class can parses the command line parameter for multiple directories
 * and files.
 * This class contains methods to determine whether the pathnames are valid
 * directories that actually exist. There are also methods to return a handle
 * to the Directory object that is specified by the pathname String.
 * Pathnames can either be relative or absolute.
 *
 */
public class Path {

    private ArrayList<String> pathArray = new ArrayList();
    private Directory root;

    public Path(String pathName, Directory current) {
        root = current.getRoot();

        if (pathName.equals("/")) {
            pathArray.add("/");
        } else {
            if (!pathName.startsWith("/", 0)) {
                while (current.getParent() != null) {
                    pathArray.add(0, current.getName());
                    current = current.getParent();
                }
            }

            StringTokenizer t = new StringTokenizer(pathName, "/");
            String filesystem = "";
            if (t.hasMoreTokens()) {
                filesystem = t.nextToken("/");
            }
            pathArray.add(filesystem);
            while (t.hasMoreTokens()) {
                filesystem = t.nextToken("/");
                pathArray.add(filesystem);

            }
        }
    }

    public ArrayList<String> getPathArray() {
        return pathArray;
    }

    /**
     * Creates and returns ArrayList<Path> which was made from a string holding (multiple) absolute and/or relative paths
     * @param string with multiple paths
     * @return return an array with each path instantiated as a Path
     */
    public static ArrayList<Path> whiteSpaceTokenizer(String parameter, Directory current) {
        ArrayList<String> paramArray = new ArrayList();
        ArrayList<Path> pathArray = new ArrayList();
        StringTokenizer p = new StringTokenizer(parameter, " ");
        String param = "";
        if (p.hasMoreTokens()) {
            param = p.nextToken(" ");
        }
        paramArray.add(param);
        while (p.hasMoreTokens()) {
            param = p.nextToken(" ");
            paramArray.add(param);
        }

        for (String e : paramArray) {
            Path instance = new Path(e, current);
            pathArray.add(instance);
        }

        return pathArray;
    }

    /**
     * Return true if there are at least two paths which are the same
     * @param An ArrayList<Path>, possibly created by Path.whiteSpaceTokenizer
     * @return True if two paths in the give array list point to the same thing
     */
    public static boolean checkDouble(ArrayList<Path> PathArray) {
        //from first to last index
        for (int i = 0; i <= (PathArray.size() - 1); i++) {
            Path a = PathArray.get(i);
            //we check path with every other one except itself, never repeating a check
            for (int j = (i + 1); j <= (PathArray.size() - 1); j++) {
                Path b = PathArray.get(j);
                //if the parents of both paths exist, and the parents are the same
                if (a.parentExists() && b.parentExists() && (a.getParent() == b.getParent())) {
                    if (a.getChildName().equals(b.getChildName())) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    /**
     * @return true if parent of path is legal.
     */
    public boolean parentExists() {
        if (pathArray.size() == 1 && pathArray.get(0).equals("/")) {
            return true;
        } else {
            return fileSystemExists(pathArray.size() - 2);
        }
    }

    /**
     * @return true if child of path is legal, .
     */
    public boolean childExists() {
        if (pathArray.size() == 1 && pathArray.get(0).equals("/")) {
            return true;
        }
        return fileSystemExists(pathArray.size() - 1);
    }

    /**
     * @return the parent to the specified path
     */
    public Directory getParent() {
        if (pathArray.size() == 1 && pathArray.get(0).equals("/")) {
            return root;
        }
        Directory directory = root;
        for (int j = 0; j <= (pathArray.size() - 2); j++) {
            if (pathArray.get(j).equals("..")) {
                directory = directory.getParent();
            } else {
                directory = directory.getDirectory(pathArray.get(j));
            }
        }
        return directory;
    }

    /**
     * @return the directory at the path
     */
    public Directory getChildDirectory() {
        if (pathArray.size() == 1 && pathArray.get(0).equals("/")) {
            return root;
        }
        if (this.getChildName().equals("..")){
            return this.getParent().getParent();
        }
        return this.getParent().getDirectory(this.getChildName());
    }

    /**
     * @return the file at the path
     */
    public File getChildFile() {
        return this.getParent().getFile(this.getChildName());
    }

    /**
     * @return the name of the specified directory 
     */
    public String getChildName() {
        return pathArray.get(pathArray.size() - 1);
    }

    /**
     * @param int index representing the index in pathArray
     * @return True if path is correct to given index in pathArray 
     */
    private boolean fileSystemExists(int index) {
        Directory directory = root;
        for (int j = 0; j <= index; j++) {
            String next = pathArray.get(j);
            //move up once to parent for ".."
            if (next.equals("..")) {
                if (directory.getParent() != null) {
                    directory = directory.getParent();
                } else {
                    return false;
                }
            } else if ((directory.has(next) == null)) {
                return false;
            } else {
                directory = directory.getDirectory(next);
            }
        }
        //if we reach here, the path up to the index we checked is true
        return true;
    }
}
