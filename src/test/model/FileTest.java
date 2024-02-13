package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileTest {
    private File emptyFile;
    private File nonEmptyFile;
    private ArrayList<String> testString;

    @BeforeEach
    public void setUp() {
        testString = new ArrayList<>();
        testString.add("Lorem Ipsum is simply dummy text of the printing and typesetting industry.");
        testString.add("Lorem Ipsum has been the industry's standard dummy text ever since the 1500s");
        testString.add("when an unknown printer took a galley of type and scrambled it to make a type specimen book");

        emptyFile = new File("file1");
        nonEmptyFile = new File("file2", testString.get(0));
    }

    @Test
    public void testConstructor() {
        assertEquals("file1", emptyFile.getName());
        assertEquals("", emptyFile.getContent());
        assertEquals(0, emptyFile.getDateCreated().compareTo(emptyFile.getDateModified()));
        assertEquals(0, emptyFile.getSize());

        assertEquals("file2", nonEmptyFile.getName());
        assertEquals(testString.get(0), nonEmptyFile.getContent());
        assertEquals(0, nonEmptyFile.getDateCreated().compareTo(nonEmptyFile.getDateModified()));
        assertEquals(testString.get(0).length(), nonEmptyFile.getSize());
    }

    @Test
    public void testSave() {
        assertEquals("", emptyFile.getContent());
        emptyFile.save(testString.get(1));
        assertEquals(testString.get(1), emptyFile.getContent());
        emptyFile.save(testString.get(2));
        assertEquals(testString.get(2), emptyFile.getContent());
    }

    // TODO: do I need to test toString()?
}
