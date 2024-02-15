package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a directory in file system.
 * Like a general file system, all files in the directory should have unique names.
 */
public class DirNode {
    private String name;
    private boolean isRootDir;
    private DirNode parentNode;
    private List<DirNode> childrenNodes;
    private List<File> files;
    private int numFiles;

    /*
     * EFFECTS: create an empty root directory with name "root"
     */
    public DirNode() {
        files = new ArrayList<>();
        childrenNodes = new ArrayList<>();
        name = "root";
        isRootDir = true;
        numFiles = 0;
    }

    /*
     * EFFECTS: create an empty non-root directory with the given name
     */
    public DirNode(String name) {
        files = new ArrayList<>();
        childrenNodes = new ArrayList<>();
        this.name = name;
        isRootDir = false;
        numFiles = 0;
    }

    /*
     * MODIFIES:  this
     * EFFECTS:   add given file if there is no file in the directory with
     *                the same filename, do nothing if the filename exists;
     *                return true if file is added successfully, return false
     *                otherwise
     */
    public boolean addFile(File file) {
        if (!containsFile(file.getName())) {
            files.add(file);
            return true;
        } else {
            return false;
        }
    }

    /*
     * EFFECTS:   return file that has the given name, return null if the
     *                file with the given name cannot be found
     */
    public File getFile(String fileName) {
        for (File file: files) {
            if (file.getName() == fileName) {
                return file;
            }
        }
        return null;
    }

    /*
     * MODIFIES:  this
     * EFFECTS:   delete file with the given filename, do nothing if file
     *                doesn't exist
     *            return true if delete process is successful, false otherwise
     */
    public boolean deleteFile(String fileName) {
        return files.removeIf(file -> file.getName() == fileName);
    }

    /*
     * EFFECTS:   return filenames in alphabetical order
     */
    public List<String> getOrderedFileNames() {
        ArrayList<String> nameList = new ArrayList<>();
        files.forEach(file -> nameList.add(file.getName()));
        Collections.sort(nameList, String.CASE_INSENSITIVE_ORDER);
        return nameList;
    }

    /*
     * EFFECTS:   return the name of directory
     */
    public String getName() {
        return name;
    }

    /*
     * EFFECTS:   return true if current directory is root directory,
     *                   false otherwise
     */
    public boolean isRootDir() {
        return isRootDir;
    }

    /*
     * EFFECTS:   return the number of files in directory
     */
    public int getNumFiles() {
        return numFiles;
    }

    /*
     * EFFECTS:   returns true if directory containsFile file with the given name
     *            returns false otherwise
     */
    public boolean containsFile(String fileName) {
        for (File file: files) {
            if (file.getName() == fileName) {
                return true;
            }
        }
        return false;
    }

    /*
     * EFFECTS:   returns a string representation of a directory
     */
    @Override
    public String toString() {
        return getName() + " (" + getNumFiles() + " files)";
    }
}
