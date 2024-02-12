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
     * EFFECTS: create an empty directory
     */
    public DirNode() {
        files = new ArrayList<>();
    }

    /*
     * REQUIRES:  there is no file in the directory that has the same name as given file
     * MODIFIES:  this
     * EFFECTS:   add given file to the directory
     */
    public void addFile(File file) {
    }

    /*
     * REQUIRES:  there is a file in the directory with the given filename
     * EFFECTS:   return file that has the given name
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
     * EFFECTS:   delete file with the given filename
     */
    public void deleteFile(String fileName) {
    }

    // TODO: should I just cache all filenames in a new field?
    /*
     * EFFECTS:   return filenames in alphabetical order
     */
    public List<String> getOrderedFilenames() {
        return null;
    }

    /*
     * EFFECTS:   returns true if directory contains file with the given name
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
