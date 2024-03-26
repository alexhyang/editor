package persistence;

import model.Dir;
import model.File;
import model.exceptions.IllegalNameException;
import model.exceptions.NotFoundException;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

// Cite: this class is based on CPCS210/JsonSerializationDemo
public class JsonReaderTest extends JsonTest {

    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            Dir dir = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testReaderEmptyFileSystem() {
        JsonReader reader = new JsonReader("./data/testReaderEmptyFileSystem.json");
        try {
            Dir rootDir = reader.read();
            checkDir("root", true, 0, 0, rootDir);
        } catch (IOException e) {
            fail("IOException shouldn't be thrown");
        }
    }

    @Test
    void testReaderFileSystemWithEmptySubdirectories() {
        JsonReader reader = new JsonReader("./data/testReaderFileSystemWithEmptySubdirectories.json");
        try {
            Dir rootDir = reader.read();
            checkDir("root", true, 0, 1, rootDir);

            assertTrue(rootDir.containsSubDir("folder1"));
            Dir folder1 = rootDir.getSubDir("folder1");
            checkDir("folder1", false, 0, 0, folder1);

        } catch (IOException e) {
            fail("IOException shouldn't be thrown");
        } catch (IllegalNameException e) {
            fail("IllegalNameException shouldn't be thrown here");
        } catch (NotFoundException e) {
            fail("NotFoundException shouldn't be thrown");
        }
    }

    @Test
    void testReaderFileSystemWithNonEmptySubDirectories() {
        JsonReader reader = new JsonReader("./data/testReaderFileSystemWithNonEmptySubdirectories.json");
        try {
            Dir rootDir = reader.read();
            checkDir("root", true, 0, 1, rootDir);

            assertTrue(rootDir.containsSubDir("folder1"));
            Dir folder1 = rootDir.getSubDir("folder1");
            checkDir("folder1", false, 1, 0, folder1);

            assertTrue(folder1.containsFile("file1.txt"));
            File file1 = folder1.getFile("file1.txt");
            String date = "Fri Mar 08 13:21:20 PST 2024";
            checkFile("file1.txt", date, date, "", file1);
        } catch (IOException e) {
            fail("IOException shouldn't be thrown");
        } catch (IllegalNameException e) {
            fail("IllegalNameException shouldn't be thrown");
        } catch (NotFoundException e) {
            fail("NotFoundException shouldn't be thrown");
        }

    }

    @Test
    void testReaderFileSystemWithFiles() {
        JsonReader reader = new JsonReader("./data/testReaderFileSystemWithFiles.json");
        try {
            Dir rootDir = reader.read();
            checkDir("root", true, 1, 0, rootDir);

            assertTrue(rootDir.containsFile("file1.txt"));
            File file1 = rootDir.getFile("file1.txt");
            String date = "Fri Mar 08 13:19:37 PST 2024";
            checkFile("file1.txt", date, date, "", file1);
        } catch (IOException e) {
            fail("IOException shouldn't be thrown");
        } catch (IllegalNameException e) {
            fail("IllegalNameException shouldn't be thrown");
        } catch (NotFoundException e) {
            fail("NotFoundException shouldn't be thrown");
        }
    }
}
