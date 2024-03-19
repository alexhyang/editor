package model;

import model.exceptions.IllegalNameException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class FileTest {
    private File emptyFile;
    private File nonEmptyFile;
    private File datedNonEmptyFile;
    private ArrayList<String> testString;
    private Date date = Calendar.getInstance().getTime();

    @BeforeEach
    public void setUp() {
        testString = new ArrayList<>();
        testString.add("Lorem Ipsum is simply dummy text of the printing and typesetting industry.");
        testString.add("Lorem Ipsum has been the industry's standard dummy text ever since the 1500s");
        testString.add("when an unknown printer took a galley of type and scrambled it to make a type specimen book");

        try {
            emptyFile = new File("file1");
            nonEmptyFile = new File("file2", testString.get(0));
            datedNonEmptyFile = new File("file3", testString.get(1), date, date);
        } catch (IllegalNameException e) {
            fail("IllegalNameException shouldn't be thrown");
        }
    }

    @Test
    public void testConstructorNothingThrown() {
        assertEquals("file1", emptyFile.getName());
        assertEquals("", emptyFile.getContent());
        assertEquals(0, emptyFile.getDateCreated().compareTo(emptyFile.getDateModified()));
        assertEquals(0, emptyFile.getSize());

        assertEquals("file2", nonEmptyFile.getName());
        assertEquals(testString.get(0), nonEmptyFile.getContent());
        assertEquals(0, nonEmptyFile.getDateCreated().compareTo(nonEmptyFile.getDateModified()));
        assertEquals(testString.get(0).length(), nonEmptyFile.getSize());

        assertEquals("file3", datedNonEmptyFile.getName());
        assertEquals(testString.get(1), datedNonEmptyFile.getContent());
        assertEquals(date, datedNonEmptyFile.getDateCreated());
        assertEquals(date, datedNonEmptyFile.getDateModified());
        assertEquals(testString.get(1).length(), datedNonEmptyFile.getSize());
    }

    @Test
    public void testConstructorStringExpectIllegalNameException() {
        try {
            File file = new File("");
            fail("IllegalNameException expected");
        } catch (IllegalNameException e) {
            assertEquals("File.File_String: File name must be nonempty string.", e.getMessage());
        }
    }

    @Test
    public void testConstructorStringStringExpectIllegalNameException() {
        try {
            File file = new File("", "");
            fail("IllegalNameException expected");
        } catch (IllegalNameException e) {
            assertEquals("File.File_String_String: File name must be nonempty string.", e.getMessage());
        }
    }

    @Test
    public void testConstructorStringStringDateDateExpectIllegalNameException() {
        try {
            File file = new File("", "", date, date);
            fail("IllegalNameException expected");
        } catch (IllegalNameException e) {
            assertEquals("File.File_String_String_Date_Date: File name must be nonempty string.", e.getMessage());
        }
    }

    @Test
    public void testSave() {
        assertEquals("", emptyFile.getContent());

        Date now = Calendar.getInstance().getTime();
        emptyFile.update(testString.get(1), now);
        assertEquals(testString.get(1), emptyFile.getContent());
        assertEquals(now, emptyFile.getDateModified());

        Date later = Calendar.getInstance().getTime();
        emptyFile.update(testString.get(2), later);
        assertEquals(testString.get(2), emptyFile.getContent());
        assertEquals(later, emptyFile.getDateModified());
    }

    @Test
    public void testToString() {
        String containStr = "file2 (size: " + testString.get(0).length() + ")";
        assertTrue(nonEmptyFile.toString().contains(containStr));
    }
}
