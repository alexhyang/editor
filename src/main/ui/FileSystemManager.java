package ui;

import model.Dir;
import model.File;
import model.exceptions.DuplicateException;
import model.exceptions.IllegalNameException;
import model.exceptions.NotFoundException;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class FileSystemManager {
    private static final String JSON_STORE = "./data/fileSystem.json";
    private static JsonWriter jsonWriter;
    private static JsonReader jsonReader;
    private static Dir rootDir;

    // MODIFIES: this
    // EFFECTS:  create a file system manager that load file system from ./data/fileSystem.json,
    //     if the file system file doesn't exist, create a new file system with an empty root directory
    public FileSystemManager() {
        Dir rootDirTmp;
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
        try {
            rootDirTmp = jsonReader.read();
        } catch (IOException e) {
            System.out.println("IOException caught...");
            rootDirTmp = new Dir();
        }
        rootDir = rootDirTmp;
    }

    // EFFECTS: return the root dir
    public Dir getRootDir() {
        return rootDir;
    }

    // EFFECTS: get content of file with the given absolute path
    public String getFileContent(String absPath) {
        if (getFile(absPath) != null) {
            return getFile(absPath).getContent();
        }
        return "Can't get file content!";
    }

    // MODIFIES: this
    // EFFECTS: update the file with given absolute path with given content
    public void updateFileContent(String absPath, String content) {
        Date now = Calendar.getInstance().getTime();
        getFile(absPath).update(content, now);
    }

    // EFFECTS: get file with absolution path, returns null if the path is invalid
    private File getFile(String absPath) {
        try {
            String dirPath = getLocationFromPath(absPath);
            String fileName = getNameFromPath(absPath);
            Dir targetDir = findTargetDir(dirPath);
            return targetDir.getFile(fileName);
        } catch (NotFoundException | IllegalNameException e) {
            return null;
        }
    }

    // MODIFIES: this
    // EFFECTS:  create a new file with the given absolute path,
    //    throws NotFoundException if the target directory to add the file doesn't exist,
    //    throws IllegalNameException if the
    public void createFile(String absPath) throws NotFoundException, IllegalNameException, DuplicateException {
        String dirPath = getLocationFromPath(absPath);
        String fileName = getNameFromPath(absPath);
        try {
            Dir targetDir = findTargetDir(dirPath);
            targetDir.addFile(fileName);
            save();
        } catch (NotFoundException e) {
            throw new NotFoundException("FileSystemManager.createFile: target dir location doesn't exist.");
        } catch (IllegalNameException e) {
            throw new IllegalNameException("FileSystemManager.createFile: file name must be nonblank string.");
        } catch (DuplicateException e) {
            throw new DuplicateException("FileSystemManager.createFile: file already exists.");
        }
    }

    // EFFECTS: break the absolute path of a file or directory and return the absolute path of the directory
    //     where the file or the directory is, i.e., the absolute path of the parent node
    //     throws IllegalNameException if the path is invalid
    private String getLocationFromPath(String absPath) throws IllegalNameException {
        if (absPath.isBlank() || !absPath.startsWith("~/")) {
            throw new IllegalNameException("FIleSystemManager.getLocationFromPath:\nabsolute path is invalid.");
        }
        int indexOfDirFileDivider = absPath.lastIndexOf("/");
        return absPath.substring(0, indexOfDirFileDivider);
    }

    // EFFECTS: break the absolute path of a file or directory and return the name of the file or directory,
    //     i.e., the name of the child node
    private String getNameFromPath(String absPath) {
        int indexOfDirFileDivider = absPath.lastIndexOf("/");
        return absPath.substring(indexOfDirFileDivider + 1);
    }

    // EFFECTS: get metadata of directory with the given absolute path
    public String getDirInfo(String absPath) {
        try {
            Dir targetDir = findTargetDir(absPath);
            return targetDir.toString();
        } catch (NotFoundException e) {
            return "No such directory";
        }
    }

    // MODIFIES: this
    // EFFECTS:  create a new directory with the given absolute path
    public void createDir(String absPath) throws NotFoundException, IllegalNameException, DuplicateException {
        String parentDirPath = getLocationFromPath(absPath);
        String newDirName = getNameFromPath(absPath);
        try {
            Dir parentDir = findTargetDir(parentDirPath);
            parentDir.addSubDir(newDirName);
            save();
        } catch (NotFoundException e) {
            throw new NotFoundException("FileSystemManager.createDir: target dir location doesn't exist.");
        } catch (IllegalNameException e) {
            throw new IllegalNameException("FileSystemManager.createDir: directory name must be nonblank string.");
        } catch (DuplicateException e) {
            throw new DuplicateException("FileSystemManager.createDir: directory already exists.");
        }
    }

    // EFFECTS: find and return the directory with the given absolute path
    private Dir findTargetDir(String absPath) throws NotFoundException {
        String[] dirStrs = absPath.split("/");
        if (!dirStrs[0].equals("~")) {
            throw new NotFoundException("FileSystemManager.findTargetDir: the first dir string must be ~");
        }
        Dir targetDir;
        targetDir = findDirectory(rootDir, Arrays.copyOfRange(dirStrs, 1, dirStrs.length));
        return targetDir;
    }

    // EFFECTS:  find directory based on the given array of relative path, if target dir exists,
    //               returns its dirNode, otherwise throws NotFoundException
    public Dir findDirectory(Dir currentDir, String[] dirStrs) throws NotFoundException {
        if (dirStrs.length == 0) {
            return currentDir;
        }
        Dir nextDir = findNextDirectory(currentDir, dirStrs[0]);
        if (dirStrs.length == 1) {
            return nextDir;
        } else {
            String[] remainingDirStrs = Arrays.copyOfRange(dirStrs, 1, dirStrs.length);
            return findDirectory(nextDir, remainingDirStrs);
        }
    }

    // EFFECTS:  return the next directory based on the given dir and nextDirName,
    //               throws NotFoundException if the directory can't be found
    private Dir findNextDirectory(Dir dir, String nextDirName) throws NotFoundException {
        if (nextDirName.equals("..")) {
            if (dir.isRootDir()) {
                return rootDir;
            } else {
                return dir.getParentDir();
            }
        } else if (nextDirName.equals("~")) {
            return rootDir;
        } else if (dir.containsSubDir(nextDirName)) {
            try {
                return dir.getSubDir(nextDirName);
            } catch (IllegalNameException e) {
                System.err.println(e.getMessage());
                throw new NotFoundException(
                        "FileSystemManager.findNextDirectory: Can't find directory with illegal name.");
            }
        } else {
            throw new NotFoundException("FileSystemManager.findNextDirectory: no such directory.");
        }
    }

    // EFFECTS: save the current file system state to ./data/fileSystem.json
    public void save() {
        try {
            jsonWriter.open();
            jsonWriter.write(rootDir);
            jsonWriter.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
