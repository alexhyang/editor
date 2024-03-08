package persistence;

import model.DirNode;
import model.File;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonTest {
    protected void checkDir(String name, Boolean isRootDir, int numFiles, int numSubDirs, DirNode dirNode) {
        assertEquals(name, dirNode.getName());
        assertEquals(isRootDir, dirNode.isRootDir());
        assertEquals(numFiles, dirNode.getNumFiles());
        assertEquals(numSubDirs, dirNode.getNumSubDirs());
    }

    protected void checkFile(String name, String dateCreated, String dateModified, String content, File file) {
        assertEquals(name, file.getName());
        assertEquals(dateCreated, file.getDateCreated().toString());
        assertEquals(dateModified, file.getDateModified().toString());
        assertEquals(content, file.getContent());
    }
}
