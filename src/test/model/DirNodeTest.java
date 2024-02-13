package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class DirNodeTest {
    // delete or rename this class!
    private DirNode dirRoot;
    private DirNode dirNonRoot;
    private List<File> files;

    @BeforeEach
    public void setUp() {
        dirRoot = new DirNode();
        dirNonRoot = new DirNode("sub_dir");
        files = new ArrayList<>();
        files.add(new File("DirNode.java"));
        files.add(new File("File.java"));
        files.add(new File("README.md", "this is readme file"));
        files.add(new File("data.json"));
        files.add(new File("Editor.java"));
    }

    @Test
    public void testConstructor() {
        assertEquals("root", dirRoot.getName());
        assertTrue(dirRoot.isRootDir());
        assertEquals(0, dirRoot.getNumFiles());

        assertEquals("sub_dir", dirNonRoot.getName());
        assertFalse(dirNonRoot.isRootDir());
        assertEquals(0, dirNonRoot.getNumFiles());
    }

    @Test
    public void testAddFile() {
        assertTrue(dirRoot.addFile(files.get(0)));
        assertTrue(dirRoot.containsFile("DirNode.java"));

        assertTrue(dirRoot.addFile(files.get(1)));
        assertTrue(dirRoot.containsFile("File.java"));
        assertFalse(dirRoot.addFile(files.get(1)));
    }

    @Test
    public void testGetFile() {
        dirRoot.addFile(files.get(0));
        dirRoot.addFile(files.get(1));
        assertEquals(files.get(0), dirRoot.getFile("DirNode.java"));
        assertEquals(files.get(1), dirRoot.getFile("File.java"));

        assertNull(dirRoot.getFile("SomeClass.java"));
    }

    @Test
    public void testDeleteFile() {
        dirRoot.addFile(files.get(0));
        dirRoot.addFile(files.get(1));

        assertTrue(dirRoot.containsFile("DirNode.java"));
        assertTrue(dirRoot.deleteFile("DirNode.java"));
        assertFalse(dirRoot.containsFile("DirNode.java"));

        assertTrue(dirRoot.containsFile("File.java"));
        assertTrue(dirRoot.deleteFile("File.java"));
        assertFalse(dirRoot.containsFile("DirNode.java"));

        assertFalse(dirRoot.deleteFile("SomeClass.java"));
    }
}