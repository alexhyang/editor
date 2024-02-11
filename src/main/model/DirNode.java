package model;

import java.util.List;
import java.util.ArrayList;

/*  The DirNode class represents a directory in file system.
    Like a general file system, all files in the directory should have unique names.
*/
public class DirNode {
    private List<File> files;

    // EFFECTS: create an empty directory
    public DirNode() {
        files = new ArrayList<>();
    }

    // REQUIRES:  there is no file in the directory that has the same name as given file
    // MODIFIES:  this
    // EFFECTS:   add given file to the directory
    public void addFile(File file) {
    }

    // TODO: ask TA about where to check filename, also deleteFile(). ?? Return file if exists, null otherwise ??
    // REQUIRES:  there is a file in the directory with the given filename
    // EFFECTS:   return file that has the given name
    public File getFile(String fileName) {
        return null;
    }

    // TODO: isn't updateFile() doing the same thing as addFile()? should I combine the two methods?
    // TODO: dateCreated and dateModified fields should be updated in different ways
    // REQUIRES:  there is a file in the directory that has the same name as given file
    // MODIFIES:  this
    // EFFECTS:   update file that has the given name
    public void updateFile(File file) {
    }

    // REQUIRES:  there is a file in the directory with the given filename
    // MODIFIES:  this
    // EFFECTS:   delete file with the given filename
    public void deleteFile(String fileName) {
    }

    // TODO: should I just cache all filenames in a new field?
    // EFFECTS:   return filenames in alphabetical order
    public List<String> getOrderedFilenames() {
        return null;
    }

    // EFFECTS:   returns true if directory contains file with the given name
    //            returns false otherwise
    public boolean contains(String fileName) {
        return false;
    }

    // TODO: how should I write the specifications of this method?
    // EFFECTS:   override toString() method
    @Override
    public String toString() {
        return "This directory: " + getOrderedFilenames();
    }
}
