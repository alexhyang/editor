package persistence;

import model.DirNode;
import model.File;
import model.exceptions.DuplicateException;
import model.exceptions.IllegalNameException;
import model.exceptions.NotFoundException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.fail;

// Cite: this class is based on CPCS210/JsonSerilizationDemo
public class JsonWriterTest extends JsonTest {

    @Test
    void testWriterInvalidFile() {
        try {
            DirNode dirNode = new DirNode();
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException should be thrown.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testWriterEmptyFileSystem() {
        try {
            DirNode rootDirNode = new DirNode();
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyFileSystem.json");
            writer.open();
            writer.write(rootDirNode);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyFileSystem.json");
            rootDirNode = reader.read();
            checkDir("root", true, 0, 0, rootDirNode);
        } catch (IOException e) {
            fail("IOException shouldn't be thrown.");
        }
    }

    @Test
    void testWriterFileSystemWithEmptySubdirectories() {
        try {
            DirNode rootDirNode = new DirNode();
            rootDirNode.addSubDir("folder1");
            JsonWriter writer = new JsonWriter("./data/testWriterFileSystemWithEmptySubdirectories.json");
            writer.open();
            writer.write(rootDirNode);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterFileSystemWithEmptySubdirectories.json");
            rootDirNode = reader.read();
            checkDir("root", true, 0, 1, rootDirNode);
            checkDir("folder1", false, 0, 0, rootDirNode.getSubDir("folder1"));
        } catch (IOException e) {
            fail("IOException shouldn't be thrown.");
        } catch (IllegalNameException e) {
            fail("IllegalNameException shouldn't be thrown here.");
        } catch (NotFoundException e) {
            fail("NotFoundException shouldn't be thrown");
        } catch (DuplicateException e) {
            fail("DuplicateException shouldn't be thrown.");
        }
    }

    @Test
    void testWriterFileSystemWithNonEmptySubDirectories() {
        try {
            DirNode rootDirNode = new DirNode();
            rootDirNode.addSubDir("folder1");
            DirNode folder1 = rootDirNode.getSubDir("folder1");
            String dateString = "Fri Mar 08 13:21:20 PST 2024";
            Date now = new Date(dateString);
            folder1.addFile(new File("file1.txt", "", now, now));
            JsonWriter writer = new JsonWriter("./data/testWriterFileSystemWithNonEmptySubdirectories.json");
            writer.open();
            writer.write(rootDirNode);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterFileSystemWithNonEmptySubdirectories.json");
            rootDirNode = reader.read();
            checkDir("root", true, 0, 1, rootDirNode);
            checkDir("folder1", false, 1, 0, rootDirNode.getSubDir("folder1"));
            File file1 = rootDirNode.getSubDir("folder1").getFile("file1.txt");
            checkFile("file1.txt", dateString, dateString, "", file1);
        } catch (IllegalNameException e) {
            fail("IllegalNameException shouldn't be thrown");
        } catch (NotFoundException e) {
            fail("NotFoundException shouldn't be thrown");
        } catch (DuplicateException e) {
            fail("DuplicateException shouldn't be thrown");
        } catch (IOException e) {
            fail("IOException shouldn't be thrown.");
        }
    }

    @Test
    void testWriterFileSystemWithFiles() {
        try {
            DirNode rootDirNode = new DirNode();
            String dateString = "Fri Mar 08 13:21:20 PST 2024";
            Date now = new Date(dateString);
            rootDirNode.addFile(new File("file1.txt", "", now, now));
            JsonWriter writer = new JsonWriter("./data/testWriterFileSystemWithFiles.json");
            writer.open();
            writer.write(rootDirNode);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterFileSystemWithFiles.json");
            rootDirNode = reader.read();
            checkDir("root", true, 1, 0, rootDirNode);
            File file1 = rootDirNode.getFile("file1.txt");
            checkFile("file1.txt", dateString, dateString, "", file1);
        } catch (IllegalNameException e) {
            fail("IllegalNameException shouldn't be thrown");
        } catch (NotFoundException e) {
            fail("NotFoundException shouldn't be thrown");
        } catch (DuplicateException e) {
            fail("DuplicateException shouldn't be thrown");
        } catch (IOException e) {
            fail("IOException shouldn't be thrown.");
        }

    }
}
