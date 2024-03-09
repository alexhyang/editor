package model;

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

    /*
     * REQUIRES:  file name must be a non-empty string
     * EFFECTS:   create an empty file with the given name and current time stamp
     */
    public File(String name) {
        this.name = name;
        this.dateCreated = Calendar.getInstance().getTime();
        this.dateModified = this.dateCreated;
        this.content = "";
        this.size = 0;
    }

    /*
     * REQUIRES:  filename must be a non-empty string
     * EFFECTS:   create a file with the given name, given content, and current time stamp
     */
    public File(String name, String content) {
        this.name = name;
        this.dateCreated = Calendar.getInstance().getTime();
        this.dateModified = this.dateCreated;
        this.content = content;
        this.size = content.length();
    }

    /*
     * REQUIRES:  filename must be a non-empty string
     * EFFECTS:   create a file with the given name, given content, and given time stamp
     */
    public File(String name, String content, Date dateCreated, Date dateModified) {
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
