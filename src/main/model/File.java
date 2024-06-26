package model;

import model.exceptions.IllegalNameException;
import org.json.JSONObject;
import persistence.Writable;

import java.util.Calendar;
import java.util.Date;

/**
 * Represents a file in file system with a file name, file content, and meta
 * information of the file, such as date created, date modified, file size
 */
public class File implements Writable {
    private String name;
    private final Date dateCreated;
    private Date dateModified;
    private String content;
    private int size;
    private final String illegalFileNameMsg = "File name must be nonempty string.";
    private EventLog eventLog = EventLog.getInstance();

    /*
     * EFFECTS:   create an empty file with the given name and current time stamp;
     *     throws IllegalNameException if the given name is blank, i.e. name is empty
     *     or contains only white space
     */
    public File(String name) throws IllegalNameException {
        if (name.isBlank()) {
            throw new IllegalNameException("File.File_String: " + illegalFileNameMsg);
        }
        this.name = name;
        this.dateCreated = Calendar.getInstance().getTime();
        this.dateModified = this.dateCreated;
        this.content = "";
        this.size = 0;
    }

    /*
     * EFFECTS:   create a file with the given name, given content, and current time stamp;
     *     throws IllegalNameException if the given name is blank, i.e. name is empty
     *     or contains only white space
     */
    public File(String name, String content) throws IllegalNameException {
        if (name.isBlank()) {
            throw new IllegalNameException("File.File_String_String: " + illegalFileNameMsg);
        }
        this.name = name;
        this.dateCreated = Calendar.getInstance().getTime();
        this.dateModified = this.dateCreated;
        this.content = content;
        this.size = content.length();
    }

    /*
     * EFFECTS:   create a file with the given name, given content, and given time stamp;
     *     throws IllegalNameException if the given name is blank, i.e. name is empty
     *     or contains only white space
     */
    public File(String name, String content, Date dateCreated, Date dateModified)
            throws IllegalNameException {
        if (name.isBlank()) {
            throw new IllegalNameException("File.File_String_String_Date_Date: " + illegalFileNameMsg);
        }
        this.name = name;
        this.dateCreated = dateCreated;
        this.dateModified = dateModified;
        this.content = content;
        this.size = content.length();
    }

    /*
     * EFFECTS:   return name of file
     */
    public String getName() {
        return name;
    }

    /*
     * EFFECTS:   return created date of file
     */
    public Date getDateCreated() {
        return dateCreated;
    }

    /*
     * EFFECTS:   return created date of file
     */
    public Date getDateModified() {
        return dateModified;
    }

    /*
     * EFFECTS:   return content of file
     */
    public String getContent() {
        return content;
    }

    /*
     * EFFECTS:   return size of file
     */
    public int getSize() {
        return size;
    }

    /*
     * MODIFIES:  this
     * EFFECTS:   save given content to file
     */
    public void update(String content, Date now) {
        this.content = content;
        this.size = content.length();
        this.dateModified = now;
        eventLog.logEvent(new Event("updated file: " + name));
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("dateCreated", dateCreated);
        json.put("dateModified", dateModified);
        json.put("content", content);
        return json;
    }

    /*
     * EFFECTS:   returns string representation of a file
     */
    @Override
    public String toString() {
        return name + " (size: " + getSize() + ")";
    }

}
