package model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Represents a directory in file system.
 * Like a general file system, all files in the directory should have unique
 * names, and all subdirectories should have unique names.
 */
public class DirNode {
    private final String name;
    private final boolean isRootDir;
    private DirNode parentDir;
    private final List<DirNode> subDirs;
    private final List<File> files;
    private final Set<String> subDirNames;
    private final Set<String> fileNames;
    private int numFiles;
    private int numSubDirs;

    /*
     * EFFECTS:   create an empty (no file, no subdirectory) root directory
     *                with name "root"
     */
    public DirNode() {
        files = new ArrayList<>();
        subDirs = new ArrayList<>();
        name = "root";
        isRootDir = true;
        subDirNames = new HashSet<>();
        fileNames = new HashSet<>();
        numFiles = 0;
    }

    /*
     * EFFECTS:   create an empty (no file, no subdirectory) non-root directory
     *                with the given name
     */
    public DirNode(String name) {
        files = new ArrayList<>();
        subDirs = new ArrayList<>();
        this.name = name;
        isRootDir = false;
        subDirNames = new HashSet<>();
        fileNames = new HashSet<>();
        numFiles = 0;
    }

    /*
     * MODIFIES:  this
     * EFFECTS:   add given file if no file in this directory has the
     *                same filename, do nothing if the filename exists;
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
     * MODIFIES:  this
     * EFFECTS:   add empty file if no file in this directory has the
     *                given filename, do nothing if the filename exists;
     *                return true if file is added successfully, false otherwise
     */
    public boolean addFile(String fileName) {
        if (!containsFile(fileName)) {
            files.add(new File(fileName));
            fileNames.add(fileName);
            numFiles++;
            return true;
        }
        return false;
    }

    /*
     * EFFECTS:   return file that has the given name in this directory,
     *                return null if the file cannot be found
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
     * EFFECTS:   delete file with the given filename in this directory, do
     *                nothing if the file cannot be found;
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
     * REQUIRES:  must not add self as subdirectory
     * MODIFIES:  this
     * EFFECTS:   add a subdirectory with the given name if no subdirectories in
     *                this directory have the same name, do nothing otherwise;
     *                return true if the process is successful, false otherwise
     */
    public boolean addSubDir(String dirName) {
        if (!containsSubDir(dirName)) {
            DirNode child = new DirNode(dirName);
            subDirs.add(child);
            child.addParentDir(this);
            numSubDirs++;
            subDirNames.add(dirName);
            return true;
        }
        return false;
    }

    /*
     * REQUIRES:  must not add self as parent directory
     * MODIFIES:  this
     * EFFECTS:   add a parent directory,
     *                return true if the process is successful, false otherwise
     */
    private void addParentDir(DirNode dirNode) {
        parentDir = dirNode;
    }

    /*
     * EFFECTS:   return the subdirectory in this directory with the given name,
     *                return null if the subdirectory cannot be found
     */
    public DirNode getSubDir(String dirName) {
        for (DirNode dirNode: subDirs) {
            if (dirNode.getName().equals(dirName)) {
                return dirNode;
            }
        }
        return null;
    }

    /*
     * EFFECTS:   return the parent directory of this directory
     */
    public DirNode getParentDir() {
        return parentDir;
    }

    /*
     * MODIFIES:  this
     * EFFECTS:   delete subdirectory with the given name in this directory, do
     *                nothing if the subdirectory cannot be found;
     *                return true if the deletion is successful, false otherwise
     */
    public boolean deleteSubDir(String nodeName) {
        if (subDirs.removeIf(child -> child.getName().equals(nodeName))) {
            numSubDirs--;
            subDirNames.remove(nodeName);
            return true;
        }
        return false;
    }

    /*
     * EFFECTS:   return file names in alphabetical order
     */
    public List<String> getOrderedFileNames() {
        return getOrderedNames(fileNames);
    }

    /*
     * EFFECTS:   return subdirectory names in alphabetical order
     */
    public List<String> getOrderedSubDirNames() {
        return getOrderedNames(subDirNames);
    }

    /*
     * EFFECTS:   return names in the given name set in alphabetical order
     */
    private List<String> getOrderedNames(Set<String> nameSet) {
        ArrayList<String> nameList = new ArrayList<>(nameSet);
        nameList.sort(String.CASE_INSENSITIVE_ORDER);
        return nameList;

    }

    /*
     * EFFECTS:   return the name of this directory
     */
    public String getName() {
        return name;
    }

    /*
     * EFFECTS:   return true if this directory is root directory, false otherwise
     */
    public boolean isRootDir() {
        return isRootDir;
    }

    /*
     * EFFECTS:   return the number of files in this directory
     */
    public int getNumFiles() {
        return numFiles;
    }

    /*
     * EFFECTS:   return the number of subdirectories in this directory
     */
    public int getNumSubDirs() {
        return numSubDirs;
    }


    /*
     * EFFECTS:   return the total number of files in this directory, including
     *                files in all subdirectories
     */
    public int getTotalNumFiles() {
        int totalNum = getNumFiles();
        if (getNumSubDirs() != 0) {
            totalNum += getOrderedSubDirNames().stream().mapToInt(name -> getSubDir(name).getTotalNumFiles()).sum();
        }
        return totalNum;
    }

    /*
     * EFFECTS:   return the total number of subdirectories in this directory,
     *                including all nested subdirectories in subdirectories
     */
    public int getTotalNumSubDirs() {
        int totalNum = getNumSubDirs();
        if (getNumSubDirs() != 0) {
            totalNum += getOrderedSubDirNames().stream().mapToInt(name -> getSubDir(name).getTotalNumSubDirs()).sum();
        }
        return totalNum;
    }

    /*
     * EFFECTS:   return the absolute path of this directory
     */
    public String getAbsPath() {
        if (isRootDir) {
            return "~";
        } else {
            return parentDir.getAbsPath() + "/" + name;
        }
    }

    /*
     * EFFECTS:   returns true if directory contains file with the given name
     *            returns false otherwise
     */
    public boolean containsFile(String fileName) {
        return fileNames.contains(fileName);
    }

    /*
     * EFFECTS:   returns true if directory contains subdirectory with the given name
     *            returns false otherwise
     */
    public boolean containsSubDir(String dirName) {
        return subDirNames.contains(dirName);
    }

    /*
     * EFFECTS:   returns a string representation of a directory
     */
    @Override
    public String toString() {
        return getName() + " (" + getNumFiles() + " files)";
    }
}
