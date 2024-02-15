package ui;

import model.DirNode;
import model.File;

import java.util.Scanner;

// represents the editor ui
public class Editor {
    private static final String CREATE_FILE_COMMAND = "touch";
    private static final String VIEW_FILE_COMMAND = "cat";
    private static final String EDIT_FILE_COMMAND = "vim";
    private static final String REMOVE_FILE_COMMAND = "rm";
    private static final String LIST_ALL_FILES_COMMAND = "ls";
    private static final String QUIT_COMMAND = "q";

    private final Scanner input;
    private final DirNode rootDir;
    private boolean runProgram;

    public Editor() {
        input = new Scanner(System.in);
        runProgram = true;
        rootDir = new DirNode();
    }

    public void start() {
        String str;
        addDummyFiles();

        while (runProgram) {
            printMenu();
            if (input.hasNext()) {
                str = input.nextLine();
                selectMenuOption(str);
            }
        }
    }

    @SuppressWarnings("methodlength")
    private void selectMenuOption(String str) {
        if (str.length() > 0) {
            String[] args = str.split(" ");
            String cmd = args[0];
            String fileName = "";
            if (args.length == 2) {
                fileName = args[1];
            }
            switch (cmd) {
                case CREATE_FILE_COMMAND:
                    createFile(fileName);
                    break;
                case VIEW_FILE_COMMAND:
                    viewFile(fileName);
                    break;
                case EDIT_FILE_COMMAND:
                    editFile(fileName);
                    break;
                case REMOVE_FILE_COMMAND:
                    removeFile(fileName);
                    break;
                case LIST_ALL_FILES_COMMAND:
                    listAllFiles();
                    break;
                case QUIT_COMMAND:
                    runProgram = false;
                    break;
                default:
                    System.out.println("Unknown command. Please try again.");
                    break;
            }
        }
    }

    // EFFECTS: prints menu of editor
    private void printMenu() {
        System.out.println("\nPlease select the following options: ");
        System.out.println("Enter '" + CREATE_FILE_COMMAND + " <file name>' to create a file");
        System.out.println("Enter '" + VIEW_FILE_COMMAND + " <file name>' to view content of a file");
        System.out.println("Enter '" + EDIT_FILE_COMMAND + " <file name>' to edit a file");
        System.out.println("Enter '" + REMOVE_FILE_COMMAND + " <file name>' to remove a file");
        System.out.println("Enter '" + LIST_ALL_FILES_COMMAND + "' to list all files");
        System.out.println("Enter '" + QUIT_COMMAND + "' to quit editor");
        System.out.println(rootDir);
    }

    // EFFECTS: create a file
    private void createFile(String fileName) {
        if (fileName.length() == 0) {
            System.out.println("Please enter a valid file name");
        } else {
            File newFile = new File(fileName);
            if (rootDir.addFile(newFile)) {
                System.out.println(fileName + " was created successfully!");
            } else {
                System.out.println(fileName + " already exists");
            }
        }
    }

    // EFFECTS: print file content
    private void viewFile(String fileName) {
        if (fileName.length() == 0) {
            System.out.println("Please enter a valid file name");
        } else {
            File file = rootDir.getFile(fileName);
            if (file != null) {
                System.out.println(file.getContent());
            } else {
                System.out.println(fileName + " doesn't exist!");
            }
        }
    }

    // EFFECTS: edit file content and save to dir
    private void editFile(String fileName) {
        if (fileName.length() == 0) {
            System.out.println("Please enter a valid file name");
        } else {
            System.out.println(fileName + " cannot be edited right now. Please finish implementation first");
        }
    }

    // EFFECTS: remove file from dir
    private void removeFile(String fileName) {
        if (fileName.length() == 0) {
            System.out.println("Please enter a valid file name");
        } else {
            if (rootDir.deleteFile(fileName)) {
                System.out.println(fileName + " has been removed!");
            } else {
                System.out.println(fileName + " doesn't exist!");
            }
        }
    }

    // EFFECTS: list all files in dir
    private void listAllFiles() {
        System.out.println(rootDir.getOrderedFileNames().toString());
    }

    // EFFECTS: add dummy files with some content
    private void addDummyFiles() {
        String longStr1 = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has "
                + "been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley "
                + "of type and scrambled it to make a type specimen book. It has survived not only five centuries, but "
                + "also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in "
                + "the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently "
                + "with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.";
        String longStr2 = "Contrary to popular belief, Lorem Ipsum is not simply random text. It has roots in a piece "
                + "of classical Latin literature from 45 BC, making it over 2000 years old. Richard McClintock, a "
                + "Latin professor at Hampden-Sydney College in Virginia, looked up one of the more obscure Latin "
                + "words, consectetur, from a Lorem Ipsum passage, and going through the cites of the word in "
                + "classical literature, discovered the undoubtable source.";
        rootDir.addFile(new File("dummy1.txt", longStr1));
        rootDir.addFile(new File("dummy2.txt", longStr2));
        rootDir.addFile(new File("dummy3.txt", longStr1));
    }

    // EFFECTS: end the program
    public void endProgram() {
        System.out.println("Bye...");
        input.close();
    }

}
