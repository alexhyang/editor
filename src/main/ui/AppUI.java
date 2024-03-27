package ui;

import model.Dir;
import model.File;
import persistence.FileSystemManager;

import javax.swing.*;
import java.awt.event.ActionEvent;

// Represents an AppUI
// Citation: class is based on CPSC210/AlarmSystem
public class AppUI extends JFrame {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 800;
    private JTree fileTree;
    private Terminal terminal;

    private final Dir rootDir;
    private Dir currentDir;
    private File editingFile;
    private boolean isEditingFileSaved;

    public AppUI() {
        // load file system
        rootDir = loadFileSystem();
        currentDir = rootDir;

        // create and set up the window
        JPanel panel = new JPanel();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setContentPane(panel);
        setTitle("Editor");
        setSize(WIDTH, HEIGHT);

        addMenu();
        addPanes();

        // Display the window
        setVisible(true);
    }

    private Dir loadFileSystem() {
        return FileSystemManager.getFileSystemState();
    }

    // Adds menu bar
    private void addMenu() {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic('F');
        addMenuItem(fileMenu, new OpenFileAction(), KeyStroke.getKeyStroke("control O"));
        addMenuItem(fileMenu, new SaveFileAction(), KeyStroke.getKeyStroke("control S"));
        menuBar.add(fileMenu);

        JMenu terminalMenu = new JMenu("Terminal");
        terminalMenu.setMnemonic('T');
        addMenuItem(terminalMenu, new OpenTerminalAction(), KeyStroke.getKeyStroke("control T"));
        addMenuItem(terminalMenu, new CloseTerminalAction(), null);
        menuBar.add(terminalMenu);

        setJMenuBar(menuBar);
    }

    // Adds an item with given handler to the given menu
    private void addMenuItem(JMenu theMenu, AbstractAction action, KeyStroke accelerator) {
        JMenuItem menuItem = new JMenuItem(action);
        menuItem.setMnemonic(menuItem.getText().charAt(0));
        menuItem.setAccelerator(accelerator);
        theMenu.add(menuItem);
    }

    // Adds panes
    private void addPanes() {
        JPanel editorPane = new EditorUI(rootDir, WIDTH - 20);
        add(editorPane);

        // JPanel terminalPane = new TerminalUI();
        // JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        // splitPane.setTopComponent(editorPane);
        // splitPane.setTopComponent(terminalPane);

    }

    // Represents the action to be taken when the user wants to open a file in the file system
    private class OpenFileAction extends AbstractAction {
        OpenFileAction() {
            super("Open File");
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            // TODO: add open file action
        }
    }

    // Represents the action to be taken when the user wants to save a file in the file system
    private class SaveFileAction extends AbstractAction {
        SaveFileAction() {
            super("Save File");
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            // TODO: add save file action
        }
    }

    // Represents the action to be taken when the user wants to open the terminal of the file system
    private class OpenTerminalAction extends AbstractAction {
        OpenTerminalAction() {
            super("Open Terminal");
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            // TODO: add save file action
        }
    }

    // Represents the action to be taken when the user wants to close the terminal of the file system
    private class CloseTerminalAction extends AbstractAction {
        CloseTerminalAction() {
            super("Close Terminal");
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            // TODO: add save file action
        }
    }
}
