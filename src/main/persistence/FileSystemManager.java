package persistence;

import model.Dir;

import java.io.FileNotFoundException;
import java.io.IOException;

public class FileSystemManager {
    private static final String JSON_STORE = "./data/fileSystem.json";
    private static JsonWriter jsonWriter;
    private static JsonReader jsonReader;

    // EFFECTS: fetch the saved file system from ./data/fileSystem.json
    //   and return the root dir
    public static Dir getFileSystemState() {
        Dir rootDir;
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
        try {
            rootDir = jsonReader.read();
        } catch (IOException e) {
            System.out.println("IOException caught...");
            rootDir = new Dir();
        }
        return rootDir;
    }

    // EFFECTS: save the current state of file system to ./data/fileSystem.json
    //   with the given root dir
    public static void saveFileSystemState(Dir rootDir) {
        try {
            jsonWriter.open();
            jsonWriter.write(rootDir);
            jsonWriter.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
