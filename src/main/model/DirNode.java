package model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Represents a directory in file system.
 * Like a general file system, all files in the directory should have unique names,
 * and all subdirectories should have unique names.
 */
public class DirNode {
    private String name;
    private final boolean isRootDir;
    private DirNode parentNode;
    private List<DirNode> childrenNodes;
    private final List<File> files;
    private Set<String> subDirNames;
    private Set<String> fileNames;
    private int numFiles;

    /*
     * EFFECTS: create an empty root directory with name "root"
     */
    public DirNode() {
        files = new ArrayList<>();
        childrenNodes = new ArrayList<>();
        name = "root";
        isRootDir = true;
        subDirNames = new HashSet<>();
        fileNames = new HashSet<>();
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
        subDirNames = new HashSet<>();
        fileNames = new HashSet<>();
        numFiles = 0;
    }

    /*
     * MODIFIES:  this
     * EFFECTS:   add given file if there is no file in the directory with
     *                the same filename, do nothing if the filename exists;
     *                return true if file is added successfully, false otherwise
     */
    public boolean addFile(File file) {
        if (!containsFile(file.getName())) {
            files.add(file);
            fileNames.add(file.getName());
            numFiles++;
            return true;
        }
        return false;
    }

    /*
     * EFFECTS:   return file that has the given name, return null if the
     *                file with the given name cannot be found
     */
    public File getFile(String fileName) {
        for (File file: files) {
            if (file.getName().equals(fileName)) {
                return file;
            }
        }
        return null;
    }

    /*
     * MODIFIES:  this
     * EFFECTS:   delete file with the given filename, do nothing if file
     *                doesn't exist;
     *                return true if delete process is successful, false otherwise
     */
    public boolean deleteFile(String fileName) {
        if (files.removeIf(file -> file.getName().equals(fileName))) {
            numFiles--;
            fileNames.remove(fileName);
            return true;
        }
        return false;
    }

    /*
     * EFFECTS:   return filenames in alphabetical order
     */
    public List<String> getOrderedFileNames() {
        ArrayList<String> nameList = new ArrayList<>();
        nameList.addAll(fileNames);
        nameList.sort(String.CASE_INSENSITIVE_ORDER);
        return nameList;
    }

    /*
     * EFFECTS:   return the name of directory
     */
    public String getName() {
        return name;
    }

    /*
     * EFFECTS:   return true if current directory is root directory, false otherwise
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
     * EFFECTS:   returns true if directory contains file with the given name
     *            returns false otherwise
     */
    public boolean containsFile(String fileName) {
        return fileNames.contains(fileName);
    }

    /*
     * EFFECTS:   returns a string representation of a directory
     */
    @Override
    public String toString() {
        return getName() + " (" + getNumFiles() + " files)";
    }
}
