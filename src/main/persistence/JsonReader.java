package persistence;

import model.DirNode;
import model.File;
import model.exceptions.DuplicateException;
import model.exceptions.IllegalNameException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.stream.Stream;

// Represents a reader that reads JSON representation to terminal
// Cite: this class is based on CPSC210/JsonSerializationDemo
public class JsonReader {
    private String source;

    // EFFECTS:  constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS:  reads file system from file and returns its root directory
    //     throws IOException if an error occurs reading data from file
    public DirNode read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseRootDirNode(jsonObject);
    }

    // EFFECTS:  reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try  (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS:  parse root directory from JSON object and returns it
    private DirNode parseRootDirNode(JSONObject jsonObject) {
        DirNode rootDir = new DirNode();
        addSubdirs(rootDir, jsonObject);
        addFiles(rootDir, jsonObject);
        return rootDir;
    }

    // EFFECTS:  parse a nonroot directory from JSON object and returns it
    private DirNode parseDirNode(JSONObject jsonObject) {
        try {
            DirNode dirNode = new DirNode(jsonObject.getString("name"));
            addSubdirs(dirNode, jsonObject);
            addFiles(dirNode, jsonObject);
            return dirNode;
        } catch (IllegalNameException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    // MODIFIES: dirNode
    // EFFECTS:  add subdirectories to the given dirNode
    private void addSubdirs(DirNode dirNode, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("subDirs");

        for (Object dirJsonObject: jsonArray) {
            try {
                dirNode.addSubDir(parseDirNode((JSONObject) dirJsonObject));
            } catch (DuplicateException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    // MODIFIES: dirNode
    // EFFECTS:  add files to the given dirNode
    private void addFiles(DirNode dirNode, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("files");

        for (Object fileJsonObject : jsonArray) {
            try {
                dirNode.addFile(parseFile((JSONObject) fileJsonObject));
            } catch (DuplicateException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    // EFFECTS:  parse a file from JSON object and returns it
    private File parseFile(JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        String content = jsonObject.getString("content");
        Date dateCreated = new Date(jsonObject.getString("dateCreated"));
        Date dateModified = new Date(jsonObject.getString("dateModified"));

        File returnFile = null;

        try {
            returnFile = new File(name, content, dateCreated, dateModified);
        } catch (IllegalNameException e) {
            System.out.println(e.getMessage());
        }

        return returnFile;
    }
}
