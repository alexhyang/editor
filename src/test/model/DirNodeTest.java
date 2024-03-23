package model;

import model.exceptions.DuplicateException;
import model.exceptions.IllegalNameException;
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
        try {
            dirNonRoot = new DirNode("sub_dir");
        } catch (IllegalNameException e) {
            fail("IllegalNameException shouldn't be thrown.");
        }
        files = new ArrayList<>();
        try {
            files.add(new File("DirNode.java"));
            files.add(new File("File.java"));
            files.add(new File("README.md", "this is readme file"));
            files.add(new File("data.json"));
            files.add(new File("Editor.java"));
        } catch (IllegalNameException e) {
            fail("IllegalNameException shouldn't be thrown");
        }
    }

    @Test
    public void testConstructorNothingThrown() {
        assertEquals("root", dirRoot.getName());
        assertTrue(dirRoot.isRootDir());
        assertEquals(0, dirRoot.getNumFiles());

        assertEquals("sub_dir", dirNonRoot.getName());
        assertFalse(dirNonRoot.isRootDir());
        assertEquals(0, dirNonRoot.getNumFiles());
    }

    @Test
    public void testConstructorExpectIllegalNameException() {
        try {
            DirNode dirNode = new DirNode("");
            fail("IllegalNameException expected");
        } catch (IllegalNameException e) {
            assertEquals("DirNode.DirNode: Directory name must be nonblank string.", e.getMessage());
        }

        try {
            DirNode dirNode = new DirNode(" ");
            fail("IllegalNameException expected");
        } catch (IllegalNameException e) {
            assertEquals("DirNode.DirNode: Directory name must be nonblank string.", e.getMessage());
        }

        try {
            DirNode dirNode = new DirNode("\t");
            fail("IllegalNameException shouldn't be thrown");
        } catch (IllegalNameException e) {
            assertEquals("DirNode.DirNode: Directory name must be nonblank string.", e.getMessage());
        }
    }

    @Test
    public void testAddFileWithGivenFileNothingThrown() {
        try {
            assertTrue(dirRoot.addFile(files.get(0)));
            assertTrue(dirRoot.containsFile("DirNode.java"));

            assertTrue(dirRoot.addFile(files.get(1)));
            assertTrue(dirRoot.containsFile("File.java"));
        } catch (DuplicateException e) {
            fail("DuplicateException shouldn't be thrown");
        }
    }

    @Test
    public void testAddFileWithGivenFileExpectDuplicateException() {
        try {
            assertTrue(dirRoot.addFile(files.get(0)));
            assertTrue(dirRoot.addFile(files.get(0)));
            fail("DuplicateException expected");
        } catch (DuplicateException e) {
            assertEquals("DirNode.addFile_File: File already exists.", e.getMessage());
        }
    }

    @Test
    public void testAddFileWithFileNameNothingThrown() {
        try {
            assertTrue(dirRoot.addFile("DirNodeTest.java"));
            assertTrue(dirRoot.containsFile("DirNodeTest.java"));

            assertTrue(dirRoot.addFile("FileTest.java"));
            assertTrue(dirRoot.containsFile("FileTest.java"));
        } catch (IllegalNameException e) {
            fail("IllegalNameException shouldn't be thrown");
        } catch (DuplicateException e) {
            fail("DuplicateException shouldn't be thrown");
        }
    }

    @Test
    public void testAddFileWithFileNameExpectIllegalNameException() {
        try {
            assertTrue(dirRoot.addFile(""));
            fail("IllegalNameException expected");
        } catch (IllegalNameException e) {
            assertEquals("DirNode.addFile_String: File name must be nonblank string.", e.getMessage());
        } catch (DuplicateException e) {
            fail("DuplicateException shouldn't be thrown");
        }

        try {
            assertTrue(dirRoot.addFile(" "));
            fail("IllegalNameException expected");
        } catch (IllegalNameException e) {
            assertEquals("DirNode.addFile_String: File name must be nonblank string.", e.getMessage());
        } catch (DuplicateException e) {
            fail("DuplicateException shouldn't be thrown");
        }

        try {
            assertTrue(dirRoot.addFile("\t"));
            fail("IllegalNameException expected");
        } catch (IllegalNameException e) {
            assertEquals("DirNode.addFile_String: File name must be nonblank string.", e.getMessage());
        } catch (DuplicateException e) {
            fail("DuplicateException shouldn't be thrown");
        }
    }

    @Test
    public void testAddFileWithFileNameExpectDuplicateException() {
        try {
            assertTrue(dirRoot.addFile("DirNodeTest.java"));
            assertTrue(dirRoot.containsFile("DirNodeTest.java"));

            assertTrue(dirRoot.addFile("DirNodeTest.java"));
            fail("DuplicateException expected");
        } catch (IllegalNameException e) {
            fail("IllegalNameException shouldn't be thrown");
        } catch (DuplicateException e) {
            assertEquals("DirNode.addFile_String: File already exists.", e.getMessage());
        }
    }


    @Test
    public void testGetFileNothingThrown() {
        try {
            dirRoot.addFile(files.get(0));
            dirRoot.addFile(files.get(1));
            assertEquals(files.get(0), dirRoot.getFile("DirNode.java"));
            assertEquals(files.get(1), dirRoot.getFile("File.java"));

            assertNull(dirRoot.getFile("SomeClass.java"));
        } catch (IllegalNameException e) {
            fail("IllegalNameException shouldn't be thrown");
        } catch (DuplicateException e) {
            fail("DuplicateException shouldn't be thrown");
        }
    }

    @Test
    public void testGetFileExpectIllegalNameException() {
        try {
            dirRoot.getFile("");
            fail("IllegalNameException expected");
        } catch (IllegalNameException e) {
            assertEquals("DirNode.getFile: File name must be nonblank string.", e.getMessage());
        }

        try {
            dirRoot.getFile(" ");
            fail("IllegalNameException expected");
        } catch (IllegalNameException e) {
            assertEquals("DirNode.getFile: File name must be nonblank string.", e.getMessage());
        }

        try {
            dirRoot.getFile("\t");
            fail("IllegalNameException expected");
        } catch (IllegalNameException e) {
            assertEquals("DirNode.getFile: File name must be nonblank string.", e.getMessage());
        }
    }

    @Test
    public void testDeleteFileNothingThrown() {
        try {
            dirRoot.addFile(files.get(0));
            dirRoot.addFile(files.get(1));

            assertTrue(dirRoot.containsFile("DirNode.java"));
            assertTrue(dirRoot.deleteFile("DirNode.java"));
            assertFalse(dirRoot.containsFile("DirNode.java"));

            assertTrue(dirRoot.containsFile("File.java"));
            assertTrue(dirRoot.deleteFile("File.java"));
            assertFalse(dirRoot.containsFile("DirNode.java"));

            assertFalse(dirRoot.deleteFile("SomeClass.java"));
        } catch (IllegalNameException e) {
            fail("IllegalNameException shouldn't be thrown");
        } catch (DuplicateException e) {
            fail("DuplicateException shouldn't be thrown");
        }
    }

    @Test
    public void testDeleteFileExpectIllegalNameException() {
        try {
            dirRoot.deleteFile("");
            fail("IllegalNameException expected");
        } catch (IllegalNameException e) {
            assertEquals("DirNode.deleteFile: File name must be nonblank string.", e.getMessage());
        }

        try {
            dirRoot.deleteFile(" ");
            fail("IllegalNameException expected");
        } catch (IllegalNameException e) {
            assertEquals("DirNode.deleteFile: File name must be nonblank string.", e.getMessage());
        }

        try {
            dirRoot.deleteFile("\t");
            fail("IllegalNameException expected");
        } catch (IllegalNameException e) {
            assertEquals("DirNode.deleteFile: File name must be nonblank string.", e.getMessage());
        }
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
        try {
            dirRoot.addFile(new File("notes.md"));
            assertEquals(nameList, dirRoot.getOrderedFileNames());
        } catch (IllegalNameException e) {
            fail("IllegalNameException shouldn't be thrown");
        } catch (DuplicateException e) {
            fail("DuplicateException shouldn't be thrown");
        }
    }

    @Test
    public void testGetOrderFileNameMultipleFiles() {
        ArrayList<String> nameList = new ArrayList<>();

        nameList.add("data.json");
        nameList.add("DirNode.java");
        nameList.add("Editor.java");
        nameList.add("File.java");
        nameList.add("README.md");

        files.forEach(file -> {
            try {
                dirRoot.addFile(file);
            } catch (DuplicateException e) {
                fail("DuplicateException shouldn't be thrown");
            }
        });
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
    public void testAddSubDirWithGivenDirNode() {
        assertTrue(dirRoot.addSubDir(dirNonRoot));
        assertTrue(dirRoot.containsSubDir(dirNonRoot.getName()));
        assertEquals(1, dirRoot.getNumSubDirs());

        assertFalse(dirRoot.addSubDir(dirNonRoot));
        assertEquals(1, dirRoot.getNumSubDirs());
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
    public void testGetTotalNumFilesRootDirOnly() {
        assertEquals(0, dirRoot.getTotalNumFiles());
        dirRoot.addFile(files.get(0));
        assertEquals(1, dirRoot.getTotalNumFiles());
    }

    @Test
    public void testGetTotalNumFilesWithSubDirs() {
        dirRoot.addSubDir("subdir");
        DirNode subdir = dirRoot.getSubDir("subdir");
        assertEquals(0, dirRoot.getTotalNumFiles());

        subdir.addFile("file2");
        assertEquals(1, dirRoot.getTotalNumFiles());

        dirRoot.addFile("file1");
        assertEquals(2, dirRoot.getTotalNumFiles());
    }

    @Test
    public void testGetTotalNumSubDirsRootDirOnly() {
        assertEquals(0, dirRoot.getTotalNumSubDirs());
        dirRoot.addSubDir("subdir");
        assertEquals(1, dirRoot.getTotalNumSubDirs());
    }

    @Test
    public void testGetTotalNumSubDirsWithSubDirs() {
        dirRoot.addSubDir("subdir");
        DirNode subdir = dirRoot.getSubDir("subdir");
        subdir.addSubDir("subsubdir");
        assertEquals(2, dirRoot.getTotalNumSubDirs());
    }


    @Test
    public void testGetGetAbsPath() {
        dirRoot.addSubDir("folder1");
        DirNode folder1 = dirRoot.getSubDir("folder1");
        folder1.addSubDir("folder2");
        DirNode folder2 = folder1.getSubDir("folder2");

        assertEquals("~", dirRoot.getAbsPath());
        assertEquals("~/folder1", folder1.getAbsPath());
        assertEquals("~/folder1/folder2", folder2.getAbsPath());
    }

    @Test
    public void testToString() {
        assertTrue(dirRoot.toString().contains("root (0 files)"));
    }
}