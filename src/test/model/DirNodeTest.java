package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class DirNodeTest {
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

    @Test
    public void testGetOrderedFileNamesEmptyDirectory() {
        assertEquals(new ArrayList<String>(), dirRoot.getOrderedFileNames());
    }

    @Test
    public void testGetOrderedFileNamesSingleFile() {
        ArrayList<String> nameList = new ArrayList<>();
        nameList.add("notes.md");

        assertEquals(new ArrayList<String>(), dirRoot.getOrderedFileNames());
        dirRoot.addFile(new File("notes.md"));
        assertEquals(nameList, dirRoot.getOrderedFileNames());
    }

    @Test
    public void testGetOrderFileNameMultipleFiles() {
        ArrayList<String> nameList = new ArrayList<>();

        nameList.add("data.json");
        nameList.add("DirNode.java");
        nameList.add("Editor.java");
        nameList.add("File.java");
        nameList.add("README.md");

        files.forEach(file -> dirRoot.addFile(file));
        assertEquals(nameList, dirRoot.getOrderedFileNames());
    }

    @Test
    public void testAddAndDeleteSubDir() {
        assertTrue(dirRoot.addSubDir("subdir1"));
        assertTrue(dirRoot.containsSubDir("subdir1"));
        assertEquals(1, dirRoot.getNumSubDirs());

        assertTrue(dirRoot.addSubDir("subdir2"));
        assertTrue(dirRoot.containsSubDir("subdir2"));
        assertEquals(2, dirRoot.getNumSubDirs());

        assertFalse(dirRoot.addSubDir("subdir1"));
        assertFalse(dirRoot.addSubDir("subdir2"));

        dirRoot.deleteSubDir("subdir1");
        assertFalse(dirRoot.containsSubDir("subdir1"));
        assertEquals(1, dirRoot.getNumSubDirs());

        dirRoot.deleteSubDir("subdir2");
        assertFalse(dirRoot.containsSubDir("subdir2"));
        assertEquals(0, dirRoot.getNumSubDirs());

        assertFalse(dirRoot.deleteSubDir("subdir1"));
        assertFalse(dirRoot.deleteSubDir("subdir2"));
    }

    @Test
    public void testGetSubDir() {
        assertNull(dirRoot.getSubDir("no subdir"));

        dirRoot.addSubDir("subdir1");
        dirRoot.addSubDir("subdir2");
        DirNode subdir1 = dirRoot.getSubDir("subdir1");
        DirNode subdir2 = dirRoot.getSubDir("subdir2");

        assertEquals("subdir1", subdir1.getName());
        assertEquals(0, subdir1.getNumFiles());
        assertEquals(dirRoot, subdir1.getParentDir());

        assertEquals("subdir2", subdir2.getName());
        assertEquals(0, subdir2.getNumFiles());
        assertEquals(dirRoot, subdir2.getParentDir());
    }

    @Test
    public void testGetOrderedSubDirNames() {
        dirRoot.addSubDir("model");
        dirRoot.addSubDir("ui");
        dirRoot.addSubDir("data");
        dirRoot.addSubDir("Settings");

        List<String> nameList = new ArrayList<>();
        nameList.add("data");
        nameList.add("model");
        nameList.add("Settings");
        nameList.add("ui");
        assertEquals(nameList, dirRoot.getOrderedSubDirNames());
    }

    @Test
    public void testToString() {
        assertTrue(dirRoot.toString().contains("root (0 files)"));
    }
}