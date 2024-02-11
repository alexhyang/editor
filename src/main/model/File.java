package model;

import java.util.Calendar;
import java.util.Date;

// File class represents a file in file system
public class File {
    private String name;
    private Date dateCreated;
    private Date dateModified;
    private String content;
    private int size;

    // REQUIRES:  file name must be a non-empty string
    // EFFECTS:   create an empty file with the given name and current time stamp
    public File(String name) {
        this.name = name;
        this.dateCreated = Calendar.getInstance().getTime();
        this.dateModified = this.dateCreated;
    }

    // REQUIRES:  filename must be a non-empty string
    // EFFECTS:   create a file with the given name, given content, and current time stamp
    public File(String name, String content) {
        this.name = name;
        this.dateCreated = Calendar.getInstance().getTime();
        this.dateModified = this.dateCreated;
        this.content = content;
        this.size = content.length();
    }

    // EFFECTS:   return name of file
    public String getName() {
        return name;
    }

    // EFFECTS:   return content of file
    public String getContent() {
        return content;
    }

    // EFFECTS:   return size of file
    public int getSize() {
        return size;
    }

    // MODIFIES:  this
    // EFFECTS:   save given content to file
    public void saveContent(String content) {
        this.content = content;
        this.size = content.length();
    }

    // TODO: how should I write the specification of this method
    // EFFECTS:   override toString() method
    @Override
    public String toString() {
        return name + " modified at: " + dateModified;
    }

}
