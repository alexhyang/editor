package model;

import java.util.List;
import java.util.ArrayList;

/**
 * Represents a directory in file system.
 * Like a general file system, all files in the directory should have unique names.
 */
public class DirNode {
    private List<File> files;

    /*
     * EFFECTS: create an empty root directory with name "root"
     */
    public DirNode() {
        files = new ArrayList<>();
    }

    /*
     * MODIFIES:  this
     * EFFECTS:   add given file if there is no file in the directory with
     *                the same filename, do nothing if the filename exists;
     *                return true if file is added successfully, return false
     *                otherwise
     */
    public boolean addFile(File file) {
        return false;
    }

    /*
     * EFFECTS:   return file that has the given name, return null if the
     *                file with the given name cannot be found
     */
    public File getFile(String fileName) {
        return null;
    }

    /*
     * REQUIRES:  there is a file in the directory that has the same name as given file
     * MODIFIES:  this
     * EFFECTS:   update file that has the given name
     */
    public void updateFile(File file) {
    }

    /*
     * REQUIRES:  there is a file in the directory with the given filename
     * MODIFIES:  this
     * EFFECTS:   delete file with the given filename, do nothing if file
     *                doesn't exist
     *            return true if delete process is successful, false otherwise
     */
    public boolean deleteFile(String fileName) {
        return false;
    }

    // TODO: should I just cache all filenames in a new field?
    /*
     * EFFECTS:   return filenames in alphabetical order
     */
    public List<String> getOrderedFilenames() {
        return null;
    }

    /*
     * EFFECTS:   returns true if directory containsFile file with the given name
     *            returns false otherwise
     */
    public boolean contains(String fileName) {
        return false;
    }

    /*
     * EFFECTS:   returns a string representation of directory
     */
    @Override
    public String toString() {
        return "This directory: " + getOrderedFilenames();
    }
}
