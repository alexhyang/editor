package ui;

import model.Dir;
import model.File;
import model.exceptions.DuplicateException;
import model.exceptions.IllegalNameException;
import model.exceptions.NotFoundException;

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
    private static final String LAUNCH_EDITOR_GUI_COMMAND = "editor";
    private static final String QUIT_COMMAND = "q";

    private static final String CONSOLE_TEXT_RESET = "\033[0m";
    private static final String CONSOLE_TEXT_BLACK = "\033[0;30m";
    private static final String CONSOLE_TEXT_CYAN = "\033[0;36m";
    private static final String CONSOLE_TEXT_BRIGHT_BLUE_BOLD = "\033[1;94m";

    private final Scanner input;
    private final FileSystemManager fsManager;
    private Dir currentDir;
    private boolean runProgram;

    // Citation: code of this method is based on FitLifeGymKiosk project
    // EFFECTS:  create a terminal and load file system using FileSystemManager
    public Terminal() {
        input = new Scanner(System.in);
        runProgram = true;
        fsManager = new FileSystemManager();
        currentDir = fsManager.getRootDir();
    }

    // Citation: code of this method is based on FitLifeGymKiosk project
    // EFFECTS:  start the editor in terminal, add dummy files
    public void start() {
        printTermIntro();

        String str;
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
                    saveFileSystem();
                    break;
                case VIEW_FILE_COMMAND:
                    viewFile(arg);
                    break;
                case EDIT_FILE_COMMAND:
                    editFile(arg);
                    break;
                case REMOVE_FILE_COMMAND:
                    removeFile(arg);
                    saveFileSystem();
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
                    saveFileSystem();
                    break;
                case REMOVE_DIRECTORY_COMMAND:
                    removeDirectory(arg);
                    saveFileSystem();
                    break;
                case LAUNCH_EDITOR_GUI_COMMAND:
                    launchGUI();
                    break;
                case QUIT_COMMAND:
                    saveFileSystem();
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
        System.out.println("   " + LAUNCH_EDITOR_GUI_COMMAND + "             launch editor GUI");
        System.out.println("   " + QUIT_COMMAND + "                  quit terminal");
    }

    // MODIFIES: this
    // EFFECTS:  create a file with the given file name in the current directory
    //               print error message if filename is blank or file exists
    private void createFile(String fileName) {
        try {
            currentDir.addFile(fileName);
            System.out.println("'" + fileName + "' was created successfully!");
        } catch (IllegalNameException e) {
            System.out.println("touch: file name must be nonblank string.");
        } catch (DuplicateException e) {
            System.out.println("touch: failed to create '" + fileName + "': file already exists!");
        }
    }

    // EFFECTS: print content of file with the given file name in the current directory
    //              if the file doesn't exist, print error message
    private void viewFile(String fileName) {
        try {
            File file = currentDir.getFile(fileName);
            System.out.println(file.getContent());
        } catch (IllegalNameException e) {
            System.out.println("cat: file name must be nonblank string.");
        } catch (NotFoundException e) {
            System.out.println("cat: failed to open '" + fileName + "': No such file");
        }
    }

    // MODIFIES:  this
    // EFFECTS:   edit and save file, if file doesn't exist, print error message
    private void editFile(String fileName) {
        try {
            File file = currentDir.getFile(fileName);
            System.out.println(fileName + " cannot be edited right now. Please finish implementation first");
            // TODO: implement editing file
        } catch (IllegalNameException e) {
            System.out.println("vim: file name must be nonblank string.");
        } catch (NotFoundException e) {
            System.out.println("vim: failed to open '" + fileName + "': No such file");
        }
    }

    // MODIFIES:  this
    // EFFECTS:   remove file from current directory, if file doesn't exist, print error message
    private void removeFile(String fileName) {
        try {
            currentDir.deleteFile(fileName);
            System.out.println("'" + fileName + "' has been removed!");
        } catch (IllegalNameException e) {
            System.out.println("rm: file name must be nonblank string.");
        } catch (NotFoundException e) {
            System.out.println("rm: failed to remove '" + fileName + "': No such file");
        }
    }

    // EFFECTS:  print out current working directory
    private void printWorkingDirectory() {
        System.out.println(currentDir.getAbsPath());
    }

    // MODIFIES: this
    // EFFECTS:  change directory to given directory if it exists, print error message if the process fails
    private void changeDirectory(String dirStr) {
        if (validateDirStr(dirStr)) {
            try {
                currentDir = fsManager.findDirectory(currentDir, dirStr.split("/"));
            } catch (NotFoundException e) {
                System.out.println("cd: no such directory: " + dirStr);
            }
        } else {
            System.out.println("cd: no such file or directory: " + dirStr);
        }
    }

    // EFFECTS:  checks if a dir string is valid, returns true if it's valid, false otherwise
    private boolean validateDirStr(String dirStr) {
        // TODO: implement method later
        return true;
    }


    // MODIFIES: this
    // EFFECTS:  create a subdirectory in current directory if it doesn't exist,
    //               do nothing otherwise
    private void createDirectory(String dirName) {
        try {
            currentDir.addSubDir(dirName);
        } catch (IllegalNameException e) {
            System.out.println("mkdir: dir name must be nonblank string.");
        } catch (DuplicateException e) {
            System.out.println("mkdir: failed to create '" + dirName + "': directory already exists!");
        }
    }

    // MODIFIES: this
    // EFFECTS:  remove a subdirectory in current directory if it exists,
    //               do nothing otherwise
    private void removeDirectory(String dirName) {
        try {
            currentDir.deleteSubDir(dirName);
        } catch (IllegalNameException e) {
            System.out.println("rmdir: dir name must be nonblank string.");
        } catch (NotFoundException e) {
            System.out.println("rmdir: failed to remove '" + dirName + "': No such directory");
        }
    }

    // EFFECTS: launch Editor GUI
    private void launchGUI() {
        new AppGUI();
    }

    // EFFECTS: list all subdirectories and files in current directory
    private void listAll() {
        list(CONSOLE_TEXT_CYAN, currentDir.getOrderedSubDirNames());
        list("", currentDir.getOrderedFileNames());
    }

    // EFFECTS: print list head and names in given name list
    private void list(String consoleTextCode, List<String> nameList) {
        if (nameList.size() != 0) {
            System.out.print(consoleTextCode);
            nameList.forEach(name -> System.out.print(name + "  "));
            System.out.print("\033[0m");
        }
    }

    // EFFECTS: print contents of the current directory as a tree
    private void tree() {
        tree(currentDir, 0);
        System.out.print("\n" + currentDir.getTotalNumSubDirs() + " directories, ");
        System.out.println(currentDir.getTotalNumFiles() + " files");
    }

    // EFFECTS: print contents of the given directory as a tree
    private void tree(Dir dir, int depth) {
        String fileIndent = getChildrenLineHead(depth);

        if (dir == currentDir) {
            System.out.println(".");
        } else {
            String selfIndent = getChildrenLineHead(depth - 1);
            System.out.println(selfIndent + CONSOLE_TEXT_CYAN + dir.getName() + "\033[0m");
        }
        dir.getOrderedSubDirNames().forEach(name -> {
            try {
                tree(dir.getSubDir(name), depth + 1);
            } catch (IllegalNameException | NotFoundException e) {
                System.err.println(e.getMessage());
            }
        });
        dir.getOrderedFileNames().forEach(name -> System.out.println(fileIndent + name));
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

    // EFFECTS:  save the current directory tree state
    private void saveFileSystem() {
        fsManager.save();
    }

    // EFFECTS: print terminal introduction
    private void printTermIntro() {
        System.out.println("This is a terminal emulator. I call it " + TERMINAL_NAME + ". Type 'help' to get commands");
        printHelp();
    }

    // EFFECTS: print command line prompt
    private void printPrompt() {
        System.out.print("\n" + CONSOLE_TEXT_BRIGHT_BLUE_BOLD + currentDir.getAbsPath() + CONSOLE_TEXT_BLACK + " > "
                + CONSOLE_TEXT_RESET);
    }

    // EFFECTS: end the program
    public void endProgram() {
        System.out.println("Bye...");
        input.close();
    }
}
