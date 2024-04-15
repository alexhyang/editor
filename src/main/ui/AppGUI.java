package ui;

import model.Event;
import model.EventLog;
import model.exceptions.DuplicateException;
import model.exceptions.IllegalNameException;
import model.exceptions.NotFoundException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

// Represents an AppGUI
// Citation: class is based on CPSC210/AlarmSystem
public class AppGUI extends JFrame implements WindowListener {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 1000;
    private EditorUI editorUI;

    private final FileSystemManager fsManager;

    // EFFECTS: create a GUI
    public AppGUI() {
        // load file system
        fsManager = new FileSystemManager();

        // create and set up the window
        JPanel panel = new JPanel(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addWindowListener(this);

        setContentPane(panel);
        setTitle("Editor");
        setSize(WIDTH, HEIGHT);

        addMenu();
        addPanes();

        // Display the window
        pack();
        setVisible(true);
    }

    // MODIFIES: this
    // EFFECTS: Adds menu bar
    private void addMenu() {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic('F');
        addMenuItem(fileMenu, new NewFileAction(), KeyStroke.getKeyStroke("control N"), true);
        addMenuItem(fileMenu, new NewFolderAction(), KeyStroke.getKeyStroke("control F"), true, 'F');
        // addMenuItem(fileMenu, new OpenFileAction(), KeyStroke.getKeyStroke("control O"), true);
        addMenuItem(fileMenu, new SaveFileAction(), KeyStroke.getKeyStroke("control S"), true);
        menuBar.add(fileMenu);

        JMenu terminalMenu = new JMenu("Terminal (unfinished)");
        terminalMenu.setMnemonic('T');
        addMenuItem(terminalMenu, new OpenTerminalAction(), KeyStroke.getKeyStroke("control T"), false);
        addMenuItem(terminalMenu, new CloseTerminalAction(), null, false);
        menuBar.add(terminalMenu);

        JMenu helpMenu = new JMenu("Help");
        helpMenu.setMnemonic('H');
        addMenuItem(helpMenu, new ShowHelpAction(), KeyStroke.getKeyStroke("control H"), true);
        menuBar.add(helpMenu);

        setJMenuBar(menuBar);
    }

    // MODIFIES: theMenu
    // EFFECTS: Adds an item with given handler to the given menu
    private void addMenuItem(JMenu theMenu, AbstractAction action, KeyStroke accelerator, boolean enabled) {
        JMenuItem menuItem = new JMenuItem(action);
        menuItem.setMnemonic(menuItem.getText().charAt(0));
        menuItem.setAccelerator(accelerator);
        menuItem.setEnabled(enabled);
        theMenu.add(menuItem);
    }

    // MODIFIES: theMenu
    // EFFECTS: Adds an item with given handler to the given menu and set the mnenomic to the given mnemonic
    private void addMenuItem(JMenu theMenu, AbstractAction action,
                             KeyStroke accelerator, boolean enabled, char mnemonic) {
        JMenuItem menuItem = new JMenuItem(action);
        menuItem.setMnemonic(mnemonic);
        menuItem.setAccelerator(accelerator);
        menuItem.setEnabled(enabled);
        theMenu.add(menuItem);
    }

    // MODIFIERS: this
    // EFFECTS: Adds panes
    private void addPanes() {
        editorUI = new EditorUI(fsManager, WIDTH - 20);
        add(editorUI);

        // JPanel terminalPane = new TerminalUI();
        // JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        // splitPane.setTopComponent(editorPane);
        // splitPane.setTopComponent(terminalPane);

    }

    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {
        for (Event event: EventLog.getInstance()) {
            System.out.println(event);
        }
    }

    @Override
    public void windowClosed(WindowEvent e) {
    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

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

    // Represents the action to be taken when the user wants to open a file in the file system
    private class NewFileAction extends AbstractAction {
        NewFileAction() {
            super("New File");
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            String absPath = JOptionPane.showInputDialog(null,
                    "File absolute path? (e.g. ~/fileName)",
                    "Enter file absolution path",
                    JOptionPane.QUESTION_MESSAGE);
            try {
                fsManager.createFile(absPath);
                editorUI.updateTree();
                getContentPane().revalidate();
                getContentPane().repaint();
            } catch (NotFoundException | IllegalNameException | DuplicateException e) {
                JOptionPane.showMessageDialog(null, e.getMessage(), "System Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Represents the action to be taken when the user wants to open a file in the file system
    private class NewFolderAction extends AbstractAction {
        NewFolderAction() {
            super("New Folder");
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            String absPath = JOptionPane.showInputDialog(null,
                    "Folder absolute path? (e.g. ~/folderName)",
                    "Enter Folder absolution path",
                    JOptionPane.QUESTION_MESSAGE);
            try {
                fsManager.createDir(absPath);
                editorUI.updateTree();
                getContentPane().revalidate();
                getContentPane().repaint();
            } catch (NotFoundException | IllegalNameException | DuplicateException e) {
                JOptionPane.showMessageDialog(null, e.getMessage(), "System Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Represents the action to be taken when the user wants to save a file in the file system
    private class SaveFileAction extends AbstractAction {
        SaveFileAction() {
            super("Save File");
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            editorUI.saveFileContent();
            JOptionPane.showMessageDialog(null, "File saved!", "File saved!",
                    JOptionPane.INFORMATION_MESSAGE);
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

    // Represents the action to be taken when the user wants to close the terminal of the file system
    private class ShowHelpAction extends AbstractAction {
        ShowHelpAction() {
            super("Show Help");
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            String helpMessage = "ctrl + s:  save file";
            JOptionPane.showMessageDialog(null, helpMessage, "Help",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
