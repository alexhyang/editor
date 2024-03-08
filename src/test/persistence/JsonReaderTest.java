package persistence;

import model.DirNode;
import model.File;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

// Cite: this class is based on CPCS210/JsonSerilizationDemo
public class JsonReaderTest extends JsonTest {

    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            DirNode dirNode = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testReaderEmptyFileSystem() {
        JsonReader reader = new JsonReader("./data/testReaderEmptyFileSystem.json");
        try {
            DirNode rootDirNode = reader.read();
            checkDir("root", true, 0, 0, rootDirNode);
        } catch (IOException e) {
            fail("IOException shouldn't be thrown");
        }
    }

    @Test
    void testReaderFileSystemWithEmptySubdirectories() {
        JsonReader reader = new JsonReader("./data/testReaderFileSystemWithEmptySubdirectories.json");
        try {
            DirNode rootDirNode = reader.read();
            checkDir("root", true, 0, 1, rootDirNode);

            assertTrue(rootDirNode.containsSubDir("folder1"));
            DirNode folder1 = rootDirNode.getSubDir("folder1");
            checkDir("folder1", false, 0, 0, folder1);

        } catch (IOException e) {
            fail("IOException shouldn't be thrown");
        }
    }

    @Test
    void testReaderFileSystemWithNonEmptySubDirectories() {
        JsonReader reader = new JsonReader("./data/testReaderFileSystemWithNonEmptySubdirectories.json");
        try {
            DirNode rootDirNode = reader.read();
            checkDir("root", true, 0, 1, rootDirNode);

            assertTrue(rootDirNode.containsSubDir("folder1"));
            DirNode folder1 = rootDirNode.getSubDir("folder1");
            checkDir("folder1", false, 1, 0, folder1);

            assertTrue(folder1.containsFile("file1.txt"));
            File file1 = folder1.getFile("file1.txt");
            String date = "Fri Mar 08 13:21:20 PST 2024";
            checkFile("file1.txt", date, date, "", file1);
        } catch (IOException e) {
            fail("IOException shouldn't be thrown");
        }

    }

    @Test
    void testReaderFileSystemWithFiles() {
        JsonReader reader = new JsonReader("./data/testReaderFileSystemWithFiles.json");
        try {
            DirNode rootDirNode = reader.read();
            checkDir("root", true, 1, 0, rootDirNode);

            assertTrue(rootDirNode.containsFile("file1.txt"));
            File file1 = rootDirNode.getFile("file1.txt");
            String date = "Fri Mar 08 13:19:37 PST 2024";
            checkFile("file1.txt", date, date, "", file1);
        } catch (IOException e) {
            fail("IOException shouldn't be thrown");
        }
    }
}
