package a2;

import java.util.*;

/**
 * @version     1.0                    
 * @since       2011-10-25
 */
public class Directory extends FileSystem {

    /**
     * A directory contains zero, one, or more files and other directories.
     */
    private ArrayList<FileSystem> contains_dir;

    /**
     * Constructor for the Directory class
     *
     * @param  The string name for the directory
     * @return A new directory with the specified name
     */
    public Directory(String name) {
        this.setName(name);
        contains_dir = new ArrayList<FileSystem>();
    }

    /**
     * Get the ArrayList which contains all the Files and Directories contained
     * within this Directory.
     *
     * @return ArrayList an ArrayList of the Files and Directories contained
     * within the current Directory.
     */
    public ArrayList<FileSystem> getChildrenFoldersAndFiles() {
        return contains_dir;
    }

    /**
     * Get a pointer to the root of the entire file system
     *
     * @return The directory which is the root. If the current directory is the
     * root, then this method will return null.
     */
    protected Directory getRoot() {
        if (this.getParent() == null) {
            return this;
        }
        return this.getParent().getRoot();
    }

    /**
     * Add a Directory to this Directory. The specified Directory will be
     * contained within this Directory. In other words, the specified Directory
     * will be a child of this Directory.
     *
     * @param Directory the Directory to add to this Directory
     */
    public void addDirectory(Directory d) {
        contains_dir.add(d);
        d.setParent(this);
    }

    /**
     * Add a File to this Directory. The specified File will be contained
     * within this Directory. In other words, the specified File will be
     * a child of this Directory
     *
     * @param File the File to add to this Directory
     */
    public void addFile(File d) {
        contains_dir.add(d);
        d.setParent(this);
    }

    /**
     * Remove the specified Directory from this Directory
     *
     * @param Directory the Directory to remove from this Directory
     */
    public void remDirectory(FileSystem d) {
        Iterator i = contains_dir.iterator();
        while (i.hasNext()) {
            FileSystem f = (FileSystem) i.next();
            if (f.getName().compareToIgnoreCase(d.getName()) == 0) {
                i.remove();

            }
        }
    }

    /**
     * Get the directory contained within this file folder.
     * Return null if the directory does not exist
     * This method only checks one level down. It is not recursive.
     * For example: /grandfather/father/son
     * If we are currently in the father Directory object, then calling this
     * method passing in the String "son" as the name parameter will return
     * a handle to the son Directory object.
     *
     * @param  The name of the file or directory to search for
     * @return A handle to the Directory with the String name
     */
    protected Directory getDirectory(String name) {
        if (hasDirectory(name) == false) {
            return null;
        }
        FileSystem ptr;
        String dirName;
        Iterator iterator = contains_dir.iterator();
        while (iterator.hasNext()) {
            ptr = (FileSystem) iterator.next();
            dirName = ptr.getName();
            if ((name.trim().compareToIgnoreCase(dirName.trim()) == 0) && (ptr instanceof Directory)) {
                return (Directory) ptr;
            }
        }
        return null;
    }

    /**
     * Get the File contained within this file folder.
     * Return null if the File does not exist
     * This method only checks one level down. It is not recursive.
     * For example: /grandfather/father/file.txt
     * If we are currently in the father Directory object, then calling this
     * method passing in the String "file.txt" as the name parameter will return
     * a handle to the son File object.
     *
     * @param  The name of the File to search for
     * @return A handle to the File with the String name
     */
    protected File getFile(String name) {
        if (hasFile(name) == false) {
            return null;
        }
        FileSystem ptr;
        String dirName;
        Iterator iterator = contains_dir.iterator();
        while (iterator.hasNext()) {
            ptr = (FileSystem) iterator.next();
            dirName = ptr.getName();
            if ((name.trim().compareToIgnoreCase(dirName.trim()) == 0) && (ptr instanceof File)) {
                return (File) ptr;
            }
        }
        return null;
    }

    /**
     * Returns a String that is the absolute path for this directory.
     * For example. Suppose that there is a directory called "grandfather"
     * Suppose that the "grandfather" directory contains a directory called
     * "father".
     * Suppose that the "father" directory contains a directory called "son".
     * Calling this method for son would return "/grandfather/father/son".
     *
     * @return String the absolute pathname for this directory
     */
    public String printDirectoryAbsolutePathname() {
        String absolutePath = "";
        Directory ptrDir = this;
        while (ptrDir.getParent() != null) {
            absolutePath = ptrDir.getName() + "/" + absolutePath;
            ptrDir = ptrDir.getParent();
        }
        absolutePath = "/" + absolutePath;
        return absolutePath;
    }

    /**
     * Returns S if search is a shortcut, D if its a Directory, F if its a file,
     * FS if its a fileSystem, and null if it doesn't exit.
     *
     * @param string name of the item we're looking for
     * @return String representation of instance type, null otherwise
     */
    public String has(String search) {
        for (int i = 0; i <= (contains_dir.size() - 1); i++) {
            if (contains_dir.get(i).getName().equals(search)) {
                if (contains_dir.get(i).getShortCut() != null) {
                    return "S";
                } else if (contains_dir.get(i) instanceof Directory) {
                    return "D";
                } else if (contains_dir.get(i) instanceof File) {
                    return "F";
                } else if (contains_dir.get(i) instanceof FileSystem) {
                    return "FS";
                }
            }
        }
        return null;
    }

    /**
     * Returns true if a directory with the specified name exists within the
     * current directory. It is not recursive. It only checks one level down.
     * Return false if the directory does not exist. It will also return false
     * if there is a file with the specified name.
     *
     * @param string name of the directory
     * @return boolean does the directory with the specified name exists
     */
    public boolean hasDirectory(String directory) {
        FileSystem ptr;
        String dirName;
        Iterator iterator = contains_dir.iterator();
        while (iterator.hasNext()) {
            ptr = (FileSystem) iterator.next();
            dirName = ptr.getName();
            if ((directory.trim().compareToIgnoreCase(dirName.trim()) == 0) && (ptr instanceof Directory)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns true if the file with the specified name exists within the
     * current directory. It is not recursive. It only checks one level down.
     * Return false if the directory does not exist. It will also return false
     * if there is a directory with the specified name.
     *
     * @param string name of the file
     * @return boolean does the file with the specified name exists
     */
    public boolean hasFile(String file) {
        FileSystem ptr;
        String dirName;
        Iterator iterator = contains_dir.iterator();
        while (iterator.hasNext()) {
            ptr = (FileSystem) iterator.next();
            dirName = ptr.getName();
            if ((file.trim().compareToIgnoreCase(dirName.trim()) == 0) && (ptr instanceof File)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Clones the directory and anything inside it recursively then passes and 
     * returns the pointer of this clone directory
     * 
     * @return Cloned directory from the one calling it
     */
    public Directory cloning() {
        //create a cloned directory from the one passed in
        Directory cloned = new Directory(this.getName());
        FileSystem child;
        //Sets up pointer and iterator for the path arraylist
        Iterator iterator = contains_dir.iterator();
        while (iterator.hasNext()) {
            child = (FileSystem) iterator.next();
            //if the child points to a directory, create a clone of it and 
            //adds to the main cloned directory, then recurves
            if (child instanceof File) {
                File fileToAdd = new File();
                fileToAdd.setName(child.getName());
                fileToAdd.setShortCut(child.getShortCut());
                cloned.addFile(fileToAdd);

                File oldChild = (File) child;
                fileToAdd.setContents(oldChild.getContents());
                cloned.addFile(fileToAdd);


                //if child points to a file
            } else {
                Directory oldCopy = (Directory) child;
                Directory newCopy = oldCopy.cloning();
                cloned.addDirectory(newCopy);
            }
        }

        cloned.setName(this.getName());
        cloned.setShortCut(this.getShortCut());
        return cloned;
    }
}
