package persistence;

import model.Dir;
import model.File;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonTest {
    protected void checkDir(String name, Boolean isRootDir, int numFiles, int numSubDirs, Dir dir) {
        assertEquals(name, dir.getName());
        assertEquals(isRootDir, dir.isRootDir());
        assertEquals(numFiles, dir.getNumFiles());
        assertEquals(numSubDirs, dir.getNumSubDirs());
    }

    protected void checkFile(String name, String dateCreated, String dateModified, String content, File file) {
        assertEquals(name, file.getName());
        assertEquals(dateCreated, file.getDateCreated().toString());
        assertEquals(dateModified, file.getDateModified().toString());
        assertEquals(content, file.getContent());
    }
}
