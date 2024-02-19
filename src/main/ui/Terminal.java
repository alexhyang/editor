package ui;

import model.DirNode;
import model.File;

import java.util.List;
import java.util.Scanner;

// represents the editor terminal ui
// the design of fields and some methods are based on FitLifeGymKiosk project from CPSC210 class
public class Terminal {
    private static final String TERMINAL_NAME = "esh";
    private static final String HELP_COMMAND = "help";
    private static final String CREATE_FILE_COMMAND = "touch";
    private static final String VIEW_FILE_COMMAND = "cat";
    private static final String EDIT_FILE_COMMAND = "vim";
    private static final String REMOVE_FILE_COMMAND = "rm";
    private static final String LIST_ALL_COMMAND = "ls";
    private static final String TREE_COMMAND = "tree";
    private static final String PRINT_WORKING_DIRECTORY_COMMAND = "pwd";
    private static final String CHANGE_DIRECTORY_COMMAND = "cd";
    private static final String CREATE_DIRECTORY_COMMAND = "mkdir";
    private static final String REMOVE_DIRECTORY_COMMAND = "rmdir";
    private static final String QUIT_COMMAND = "q";

    private final Scanner input;
    private final DirNode rootDir;
    private DirNode currentDir;
    private boolean runProgram;

    // Citation: code of this method is based on FitLifeGymKiosk project
    // EFFECTS:  create an editor with empty root directory in the file system
    public Terminal() {
        input = new Scanner(System.in);
        runProgram = true;
        rootDir = new DirNode();
        currentDir = rootDir;
    }

    // Citation: code of this method is based on FitLifeGymKiosk project
    // EFFECTS:  start the editor in terminal, add dummy files
    public void start() {
        printTermIntro();

        String str;
        addDummyFiles();

        while (runProgram) {
            printPrompt();
            str = input.nextLine();
            handleUserInput(str);
        }
    }

    // Citation: code of this method is based on FitLifeGymKiosk project
    // EFFECTS:  handle user command input
    @SuppressWarnings("methodlength")
    private void handleUserInput(String str) {
        if (str.length() > 0) {
            String[] args = str.split(" ");
            String cmd = args[0];
            String arg = "";
            if (args.length == 2) {
                arg = args[1];
            }

            switch (cmd) {
                case HELP_COMMAND:
                    printHelp();
                    break;
                case CREATE_FILE_COMMAND:
                    createFile(arg);
                    break;
                case VIEW_FILE_COMMAND:
                    viewFile(arg);
                    break;
                case EDIT_FILE_COMMAND:
                    editFile(arg);
                    break;
                case REMOVE_FILE_COMMAND:
                    removeFile(arg);
                    break;
                case LIST_ALL_COMMAND:
                    listAll();
                    break;
                case TREE_COMMAND:
                    tree();
                    break;
                case PRINT_WORKING_DIRECTORY_COMMAND:
                    printWorkingDirectory();
                    break;
                case CHANGE_DIRECTORY_COMMAND:
                    changeDirectory(arg);
                    break;
                case CREATE_DIRECTORY_COMMAND:
                    createDirectory(arg);
                    break;
                case REMOVE_DIRECTORY_COMMAND:
                    removeDirectory(arg);
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
        System.out.println("Terminal commands: ");
        System.out.println("   " + CREATE_FILE_COMMAND    + " <file name>    create a file");
        System.out.println("   " + VIEW_FILE_COMMAND      + " <file name>      view content of a file");
        System.out.println("   " + EDIT_FILE_COMMAND      + " <file name>      edit a file");
        System.out.println("   " + REMOVE_FILE_COMMAND    + " <file name>       remove a file");
        System.out.println("   " + CHANGE_DIRECTORY_COMMAND + " <dir name>        change directory");
        System.out.println("   " + CREATE_DIRECTORY_COMMAND + " <dir name>     create new directory");
        System.out.println("   " + REMOVE_DIRECTORY_COMMAND + " <dir name>     remove directory");
        System.out.println("   " + PRINT_WORKING_DIRECTORY_COMMAND + "                print current working directory");
        System.out.println("   " + LIST_ALL_COMMAND + "                 list all directories and files");
        System.out.println("   " + TREE_COMMAND + "               print content of current directory as tree");
        System.out.println("   " + QUIT_COMMAND + "                  quit terminal");
    }

    // MODIFIES: this
    // EFFECTS:  create a file with the given file name in the current directory
    //               if file already exists, print error message
    private void createFile(String fileName) {
        if (fileName.length() == 0) {
            System.out.println("Please enter a valid file name");
        } else {
            File newFile = new File(fileName);
            if (currentDir.addFile(newFile)) {
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
            File file = currentDir.getFile(fileName);
            if (file != null) {
                String content = file.getContent();
                if (content.length() == 0) {
                    System.out.println("'" + fileName + "' is empty!");
                } else {
                    System.out.println(file.getContent());
                }
            } else {
                System.out.println("cat: '" + fileName + "' No such file");
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
            if (currentDir.deleteFile(fileName)) {
                System.out.println("'" + fileName + "' has been removed!");
            } else {
                System.out.println("rm: cannot remove '" + fileName + "': No such file");
            }
        }
    }

    // EFFECTS:  print out current working directory
    private void printWorkingDirectory() {
        System.out.println(currentDir.getAbsPath());
    }

    // MODIFIES: this
    // EFFECTS:  change directory to given directory if it exists, do nothing otherwise
    private void changeDirectory(String dirName) {
        String[] dirs = dirName.split("/");
        if (dirs.length == 1) {
            if (dirName.equals("..")) {
                if (currentDir.isRootDir()) {
                    System.out.println("Already at root directory");
                } else {
                    currentDir = currentDir.getParentDir();
                }
            } else if (dirName.equals("~")) {
                currentDir = rootDir;
            } else if (currentDir.containsSubDir(dirName)) {
                currentDir = currentDir.getSubDir(dirName);
            } else {
                System.out.println("cd: no such directory: " + dirName);
            }
        } else {
            for (String dir: dirs) {
                changeDirectory(dir);
            }
        }
    }

    // MODIFIES: this
    // EFFECTS:  create a subdirectory in current directory if it doesn't exist,
    //               do nothing otherwise
    private void createDirectory(String dirName) {
        if (dirName.length() == 0) {
            System.out.println("mkdir: missing operand");
        } else {
            if (!currentDir.containsSubDir(dirName)) {
                currentDir.addSubDir(dirName);
            } else {
                System.out.println("mkdir: cannot create directory '" + dirName + "': Directory exists");
            }
        }
    }

    // MODIFIES: this
    // EFFECTS:  remove a subdirectory in current directory if it exists,
    //               do nothing otherwise
    private void removeDirectory(String dirName) {
        if (currentDir.containsSubDir(dirName)) {
            currentDir.deleteSubDir(dirName);
        } else {
            System.out.println("rmdir: failed to remove '" + dirName + "': No such directory");
        }
    }


    // EFFECTS: list all subdirectories and files in current directory
    private void listAll() {
        list("\033[0;36m", currentDir.getOrderedSubDirNames());
        list("", currentDir.getOrderedFileNames());
    }

    // EFFECTS: print list head and names in given name list
    private void list(String textColorCode, List<String> nameList) {
        if (nameList.size() != 0) {
            System.out.print(textColorCode);
            nameList.forEach(name -> System.out.print(name + "  "));
            System.out.print("\033[0m");
        }
    }

    // EFFECTS: print contents of the current directory as a tree
    private void tree() {
        tree(currentDir, 0);
    }

    // EFFECTS: print contents of the given directory as a tree
    private void tree(DirNode dirNode, int depth) {
        String fileIndent = getChildrenLineHead(depth);

        if (dirNode == currentDir) {
            System.out.println(".");
        } else {
            String selfIndent = getChildrenLineHead(depth - 1);
            System.out.println(selfIndent + "\033[1;36m" + dirNode.getName() + "\033[0m");
        }
        dirNode.getOrderedSubDirNames().forEach(name -> tree(dirNode.getSubDir(name), depth + 1));
        dirNode.getOrderedFileNames().forEach(name -> System.out.println(fileIndent + name));
    }

    // EFFECTS: return leading string for folders and files with the given depth
    private String getChildrenLineHead(int depth) {
        String childrenLineHead;
        childrenLineHead = "|-- ";
        String grandChildrenLineHead = "|   ";
        String spaceFiller = "    ";
        if (depth == 0) {
            return childrenLineHead;
        } else {
            return grandChildrenLineHead + spaceFiller.repeat(depth - 1) + childrenLineHead;
        }

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
        printHelp();
    }

    // EFFECTS: print command line prompt
    private void printPrompt() {
        System.out.print("\n" + "\033[1;94m" + currentDir.getAbsPath() + "\033[1;30m > " + "\033[0m");
    }

    // EFFECTS: end the program
    public void endProgram() {
        System.out.println("Bye...");
        input.close();
    }
}
