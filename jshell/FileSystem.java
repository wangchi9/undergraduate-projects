package a2;

/** FileSystem is an abstract class which is the parent of both the File and
 * Directory class. It has methods and variables which are common to both the
 * File and Directory class. For example, both classes are contained within
 * a directory. Moreover, both Files and Directories have a name.
 *
 */
public abstract class FileSystem {
    
    /* The name of the file or directory */
    private String name;
    
    /* The File or Directory that it points to */
    /* Note: Only a Directory can point to another Directory */
    /* Only a File can point to another File */
    private FileSystem shortCut;
    
    /**
    * Every directory or file is contained within another directory. The only exception
    * is when the parents is the root. In this case, the parent is null and the
    * name variable is also set to null.
    */
    private Directory parent;
    
    /** This is the constructor for the FileSystem */
    public FileSystem()
    {
        shortCut = null;
    }
    
    /**
    * Get the shortcut of the FileSystem.
    *
    * @return String the name of the FileSystem.
    */    
    public FileSystem getShortCut()
    {
        return shortCut;
    }
    
    /**
    * Set the shortcut of the FileSystem.
    *
    * @param String the name of the FileSystem to set.
    */    
    public void setShortCut(FileSystem f)
    {
        shortCut = f;
    }
    
    /**
    * Get the name of the FileSystem.
    *
    * @return String the name of the FileSystem.
    */    
    public String getName()
    {
        return name;
    }
    
    /**
    * Set the name of the FileSystem.
    *
    * @param String the name of the FileSystem to set.
    */    
    public void setName(String n)
    {
        name = n;
    }
    
    /**
    * Get the name of the parent of this FileSystem.
    *
    * @return Directory the parent of this FileSystem.
    */    
    public Directory getParent()
    {
        return parent;
    }
    
    /**
    * Set the parent of the FileSystem.
    *
    * @param Directory the Directory to set the parent to.
    */    
    public void setParent(Directory d)
    {
        parent = d;
    }    
}
