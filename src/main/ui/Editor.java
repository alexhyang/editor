package ui;

import model.DirNode;
import model.File;

import java.util.List;
import java.util.Scanner;

// represents the editor terminal ui
// the design of fields and some methods are based on FitLifeGymKiosk project from CPSC210 class
public class Editor {
    private static final String TERMINAL_NAME = "esh";
    private static final String HELP_COMMAND = "help";
    private static final String CREATE_FILE_COMMAND = "touch";
    private static final String VIEW_FILE_COMMAND = "cat";
    private static final String EDIT_FILE_COMMAND = "vim";
    private static final String REMOVE_FILE_COMMAND = "rm";
    private static final String LIST_ALL_FILES_COMMAND = "ls";
    private static final String QUIT_COMMAND = "q";

    private final Scanner input;
    private final DirNode rootDir;
    private boolean runProgram;

    // Citation: code of this method is based on FitLifeGymKiosk project
    // EFFECTS:  create an editor with empty root directory in the file system
    public Editor() {
        input = new Scanner(System.in);
        runProgram = true;
        rootDir = new DirNode();
    }

    // Citation: code of this method is based on FitLifeGymKiosk project
    // EFFECTS:  start the editor in terminal, add dummy files
    public void start() {
        printTermIntro();
        printHelp();

        String str;
        addDummyFiles();

        while (runProgram) {
            printPrompt();
            if (input.hasNext()) {
                str = input.nextLine();
                selectMenuOption(str);
            }
        }
    }

    // Citation: code of this method is based on FitLifeGymKiosk project
    // EFFECTS:  handle user command input
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
                case HELP_COMMAND:
                    printHelp();
                    break;
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
                    System.out.println(TERMINAL_NAME + ": command not found: " + cmd);
                    break;
            }
        }
    }

    // EFFECTS: prints help information in terminal
    private void printHelp() {
        System.out.println("Editor commands: ");
        System.out.println("   " + CREATE_FILE_COMMAND    + " <file name>    create a file");
        System.out.println("   " + VIEW_FILE_COMMAND      + " <file name>      view content of a file");
        System.out.println("   " + EDIT_FILE_COMMAND      + " <file name>      edit a file");
        System.out.println("   " + REMOVE_FILE_COMMAND    + " <file name>       remove a file");
        System.out.println("   " + LIST_ALL_FILES_COMMAND + "                   list all files");
        System.out.println("   " + QUIT_COMMAND + "                    quit editor");
    }

    // MODIFIES: this
    // EFFECTS:  create a file with the given file name in the current directory
    //               if file already exists, print error message
    private void createFile(String fileName) {
        if (fileName.length() == 0) {
            System.out.println("Please enter a valid file name");
        } else {
            File newFile = new File(fileName);
            if (rootDir.addFile(newFile)) {
                System.out.println("'" + fileName + "' was created successfully!");
            } else {
                System.out.println("'" + fileName + "' already exists!");
            }
        }
    }

    // EFFECTS: print content of file with the given file name in the current directory
    //              if the file doesn't exist, print error message
    private void viewFile(String fileName) {
        if (fileName.length() == 0) {
            System.out.println("Please enter a valid file name");
        } else {
            File file = rootDir.getFile(fileName);
            if (file != null) {
                String content = file.getContent();
                if (content.length() == 0) {
                    System.out.println("'" + fileName + "' is empty!");
                } else {
                    System.out.println(file.getContent());
                }
            } else {
                System.out.println("'" + fileName + "' doesn't exist!");
            }
        }
    }

    // MODIFIES:  this
    // EFFECTS:   edit and save file, if file doesn't exist, print error message
    private void editFile(String fileName) {
        if (fileName.length() == 0) {
            System.out.println("Please enter a valid file name");
        } else {
            System.out.println(fileName + " cannot be edited right now. Please finish implementation first");
        }
    }

    // MODIFIES:  this
    // EFFECTS:   remove file from current directory, if file doesn't exist, print error message
    private void removeFile(String fileName) {
        if (fileName.length() == 0) {
            System.out.println("Please enter a valid file name");
        } else {
            if (rootDir.deleteFile(fileName)) {
                System.out.println("'" + fileName + "' has been removed!");
            } else {
                System.out.println("'" + fileName + "' doesn't exist!");
            }
        }
    }

    // EFFECTS: list all files in dir
    private void listAllFiles() {
        List<String> fileNames = rootDir.getOrderedFileNames();
        fileNames.forEach(name -> System.out.print(name + "  "));
        System.out.println();
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

    // EFFECTS: print terminal introduction
    private void printTermIntro() {
        System.out.println("This is a terminal emulator. I call it " + TERMINAL_NAME + ". Type 'help' to get commands");
    }

    // EFFECTS: print prompt symbol
    private void printPrompt() {
        System.out.print("\n~ > ");
    }

    // EFFECTS: end the program
    public void endProgram() {
        System.out.println("Bye...");
        input.close();
    }
}
