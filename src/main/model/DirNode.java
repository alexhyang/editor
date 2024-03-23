package model;

import model.exceptions.DuplicateException;
import model.exceptions.IllegalNameException;
import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Represents a directory in file system.
 * Like a general file system, all files in the directory should have unique
 * names, and all subdirectories should have unique names.
 */
public class DirNode implements Writable {
    private final String name;
    private final boolean isRootDir;
    private DirNode parentDir;
    private final List<DirNode> subDirs;
    private final List<File> files;
    private final Set<String> subDirNames;
    private final Set<String> fileNames;
    private int numFiles;
    private int numSubDirs;
    private final String illegalFileNameMsg = "File name must be nonempty string.";
    private final String illegalDirNameMsg = "Directory name must be nonempty string.";
    private final String duplicateFileMsg = "File already exists.";
    private final String duplicateDirMsg = "Directory already exists.";

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
     *                with the given name; throws IllegalNameException if the
     *                given name is blank (i.e. empty or contains only white space
     */
    public DirNode(String name) throws IllegalNameException {
        if (name.isBlank()) {
            throw new IllegalNameException("DirNode.DirNode: " + illegalDirNameMsg);
        }
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
     *                same filename, throw DuplicateException if the filename exists;
     *                return true if file is added successfully
     */
    public boolean addFile(File file) throws DuplicateException {
        if (containsFile(file.getName())) {
            throw new DuplicateException("DirNode.addFile_File: " + duplicateFileMsg);
        }

        files.add(file);
        fileNames.add(file.getName());
        numFiles++;
        return true;
    }

    /*
     * MODIFIES:  this
     * EFFECTS:   adds file with empty content if no file in this directory has the
     *                given filename, return true if file is added successfully;
     *            throws DuplicateException if the filename exists,
     *            throws IllegalNameException if filename is blank
     */
    public boolean addFile(String fileName) throws IllegalNameException, DuplicateException {
        if (containsFile(fileName)) {
            throw new DuplicateException("DirNode.addFile_String: " + duplicateFileMsg);
        }

        try {
            files.add(new File(fileName));
            fileNames.add(fileName);
            numFiles++;
            return true;
        } catch (IllegalNameException e) {
            throw new IllegalNameException("DirNode.addFile_String: " + illegalFileNameMsg);
        }
    }

    /*
     * EFFECTS:   return file that has the given name in this directory,
     *                return null if the file cannot be found
     *            throws IllegalNameException if fileName is blank
     */
    public File getFile(String fileName) throws IllegalNameException {
        if (fileName.isBlank()) {
            throw new IllegalNameException("DirNode.getFile: " + illegalFileNameMsg);
        }
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
     *            throws IllegalNameException if fileName is blank
     */
    public boolean deleteFile(String fileName) throws IllegalNameException {
        if (fileName.isBlank()) {
            throw new IllegalNameException("DirNode.deleteFile: " + illegalFileNameMsg);
        }
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
     * EFFECTS:   adds a given subdirectory if no subdirectories in
     *                this directory have the same name;
     *                throws DuplicateException if the dirname exists,
     *                returns true if the process is successful
     */
    public boolean addSubDir(DirNode dirNode) throws DuplicateException {
        if (containsSubDir(dirNode.getName())) {
            throw new DuplicateException("DirNode.addSubDir: " + duplicateDirMsg);
        }
        subDirs.add(dirNode);
        dirNode.addParentDir(this);
        numSubDirs++;
        subDirNames.add(dirNode.getName());
        return true;
    }

    /*
     * REQUIRES:  must not add self as subdirectory
     * MODIFIES:  this
     * EFFECTS:   add an empty subdirectory with the given name if no subdirectories in
     *                this directory have the same name;
     *                throws IllegalNameException if dirname is blank,
     *                throws DuplicateException if dirname exists,
     *                return true if the process is successful
     */
    public boolean addSubDir(String dirName) throws IllegalNameException, DuplicateException {
        if (containsSubDir(dirName)) {
            throw new DuplicateException("DirNode.addSubDir: " + duplicateDirMsg);
        }
        try {
            DirNode child = new DirNode(dirName);
            subDirs.add(child);
            child.addParentDir(this);
            numSubDirs++;
            subDirNames.add(dirName);
            return true;
        } catch (IllegalNameException e) {
            throw new IllegalNameException("DirNode.addSubDir: " + illegalDirNameMsg);
        }
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
     *                throws IllegalNameException if dirName is blank
     */
    public DirNode getSubDir(String dirName) throws IllegalNameException {
        if (dirName.isBlank()) {
            throw new IllegalNameException("DirNode.getSubDir: " + illegalDirNameMsg);
        }
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
     *                throws IllegalNameException if dirName is blank
     */
    public boolean deleteSubDir(String dirName) throws IllegalNameException {
        if (dirName.isBlank()) {
            throw new IllegalNameException("DirNode.deleteSubDir: " + illegalDirNameMsg);
        }
        if (subDirs.removeIf(child -> child.getName().equals(dirName))) {
            numSubDirs--;
            subDirNames.remove(dirName);
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
            totalNum += getOrderedSubDirNames().stream().mapToInt(name -> {
                try {
                    return getSubDir(name).getTotalNumFiles();
                } catch (IllegalNameException e) {
                    throw new RuntimeException(e);
                }
            }).sum();
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
            totalNum += getOrderedSubDirNames().stream().mapToInt(name -> {
                try {
                    return getSubDir(name).getTotalNumSubDirs();
                } catch (IllegalNameException e) {
                    throw new RuntimeException(e);
                }
            }).sum();
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

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("isRootDir", isRootDir);
        json.put("subDirs", subDirsToJson());
        json.put("files", filesToJson());
        return json;
    }

    /*
     * EFFECTS:   returns subdirectories as JSONArray
     */
    private JSONArray subDirsToJson() {
        JSONArray jsonArray = new JSONArray();

        for (DirNode dirNode: subDirs) {
            jsonArray.put(dirNode.toJson());
        }

        return jsonArray;
    }

    /*
     * EFFECTS:   returns files as JSONArray
     */
    private JSONArray filesToJson() {
        JSONArray jsonArray = new JSONArray();

        for (File file: files) {
            jsonArray.put(file.toJson());
        }

        return jsonArray;
    }


    /*
     * EFFECTS:   returns a string representation of a directory
     */
    @Override
    public String toString() {
        return getName() + " (" + getNumFiles() + " files)";
    }
}
