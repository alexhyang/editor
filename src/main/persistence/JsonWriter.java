package persistence;

import model.Dir;
import org.json.JSONObject;

import java.io.*;

// Represents a writer that writes JSON representation of Directory to file
// Cite: this class is based on JsonSerializationDemo course repository
public class JsonWriter {
    private static final int TAB = 4;
    private PrintWriter writer;
    private String destination;

    // EFFECTS:  constructs writer to write to destination file
    public JsonWriter(String destination) {
        this.destination = destination;
    }

    // MODIFIES: this
    // EFFECTS:  opens writer, throws FileNotFoundException if destination file
    //     cannot be opened for writing
    public void open() throws FileNotFoundException {
        writer = new PrintWriter(destination);
    }

    // MODIFIES: this
    // EFFECTS:  writes JSON representation of given directory to file
    public void write(Dir dir) {
        JSONObject json = dir.toJson();
        writer.print(json.toString(TAB));
    }

    // MODIFIES: this
    // EFFECTS:  closes writer
    public void close() {
        writer.close();
    }

}
