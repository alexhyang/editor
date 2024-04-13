package ui;

import model.Dir;
import model.exceptions.DuplicateException;
import model.exceptions.IllegalNameException;
import model.exceptions.NotFoundException;

import javax.swing.*;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Random;

// Represents an editor ui with file tree and editor pane
// Citation: Java Tutorial
//     https://docs.oracle.com/javase/tutorial/uiswing/components/tree.html
public class EditorUI extends JPanel implements TreeSelectionListener {
    private static int WIDTH;
    private static int HEIGHT = 700;
    private static final int DIVIDER_SIZE = 4;

    private final JSplitPane splitPane;
    private final JEditorPane editorPane;
    private final JScrollPane editorView;
    private JScrollPane treeView;
    private JTree tree;
    private TreePopup treePopup;

    private final FileSystemManager fsManager;
    private String currentAbsPath;

    public EditorUI(FileSystemManager fsManager, int width) {
        super(new GridLayout(0, 1));
        WIDTH = width;
        this.fsManager = fsManager;

        tree = generateTree();
        treePopup = new TreePopup(tree);
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.addTreeSelectionListener(this);
        tree.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    treePopup.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });

        editorPane = initializeEditorPane();
        editorView = new JScrollPane(editorPane);
        treeView = new JScrollPane(tree);
        splitPane = generateSplitPane(treeView, editorView);
        add(splitPane);
    }

    // EFFECTS: generate tree based on file system and return it as JTree
    private JTree generateTree() {
        Dir rootDir = fsManager.getRootDir();
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(
                new NodeInfo(rootDir.getName(), rootDir.getAbsPath()));
        createNodes(root, rootDir);
        return new JTree(root, true);
    }

    // EFFECTS: generate, set up, and return a new editor pane
    private JEditorPane initializeEditorPane() {
        JEditorPane editorPane = new JEditorPane();
        editorPane.setContentType("text/html");

        Random random = new Random();
        int randomNum = random.nextInt(2) + 1;
        String filePath = "./data/startScreen" + randomNum + ".jpg";
        File background = new File(filePath);
        String imagePath = background.toURI().toString();
        String htmlContent = "<html><body><img src='" + imagePath + "'</body></html>";
        editorPane.setText(htmlContent);
        editorPane.setEditable(false);

        return editorPane;
    }

    // EFFECTS: generate and set up split pane and return it
    private JSplitPane generateSplitPane(JScrollPane treeView, JScrollPane editorView) {
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setLeftComponent(treeView);
        splitPane.setRightComponent(editorView);

        Dimension editorMinimumSize = new Dimension(300, 50);
        Dimension treeMinimumSize = new Dimension(100, 50);
        editorView.setMinimumSize(editorMinimumSize);
        treeView.setMinimumSize(treeMinimumSize);
        splitPane.setDividerSize(DIVIDER_SIZE);
        splitPane.setDividerLocation(150);
        splitPane.setPreferredSize(new Dimension(WIDTH, HEIGHT));

        return splitPane;
    }

    // MODIFIES: treeNode
    // EFFECTS:  create treeNode based on its corresponding directory
    private void createNodes(DefaultMutableTreeNode treeNode, Dir dir) {
        addDirectoryNodes(treeNode, dir);
        addFileNodes(treeNode, dir);
    }

    // MODIFIES: treeNode
    // EFFECTS:  add subdirectories in the given dir as children treeNode that allows children
    private void addDirectoryNodes(DefaultMutableTreeNode treeNode, Dir dir) {
        for (String subDirName : dir.getOrderedSubDirNames()) {
            try {
                Dir subDir = dir.getSubDir(subDirName);
                DefaultMutableTreeNode subDirTreeNode = new DefaultMutableTreeNode(
                        new NodeInfo(subDirName, subDir.getAbsPath()),
                        true);
                createNodes(subDirTreeNode, subDir);
                treeNode.add(subDirTreeNode);
            } catch (IllegalNameException | NotFoundException e) {
                // TODO: handle exception later
                throw new RuntimeException(e);
            }
        }
    }

    // MODIFIES: treeNode
    // EFFECTS:  add files in the given dir as children treeNode that doesn't allow children
    private void addFileNodes(DefaultMutableTreeNode treeNode, Dir dir) {
        for (String fileName : dir.getOrderedFileNames()) {
            DefaultMutableTreeNode fileTreeNode = new DefaultMutableTreeNode(
                    new NodeInfo(fileName, dir.getAbsPath() + "/" + fileName),
                    false);
            treeNode.add(fileTreeNode);
        }
    }

    @Override
    public void valueChanged(TreeSelectionEvent e) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();

        if (node == null) {
            return;
        }

        Object nodeInfo = node.getUserObject();
        if (node.isLeaf() && !node.getAllowsChildren()) {
            NodeInfo fileNode = (NodeInfo) nodeInfo;
            currentAbsPath = fileNode.absPath;
            editorPane.setEditable(true);
            editorPane.setFont(new Font("Arial", Font.PLAIN, 14));
            editorPane.setText("");
            editorPane.setContentType("text");
            editorPane.setText(fsManager.getFileContent(currentAbsPath));
        } else {
            NodeInfo dirNode = (NodeInfo) nodeInfo;
            currentAbsPath = dirNode.absPath;
            editorPane.setEditable(false);
            editorPane.setText(fsManager.getDirInfo(currentAbsPath));
        }
    }

    // EFFECTS: save content inside editor to file
    public void saveFileContent() {
        fsManager.updateFileContent(currentAbsPath, editorPane.getText());
        fsManager.save();
    }

    // EFFECTS: update the tree after creating new file or directory
    public void updateTree() {
        tree = generateTree();
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        treePopup = new TreePopup(tree);
        tree.addTreeSelectionListener(this);
        tree.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    treePopup.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });

        editorPane.setText("");
        treeView = new JScrollPane(tree);
        splitPane.setLeftComponent(treeView);
        splitPane.setDividerSize(DIVIDER_SIZE);
        splitPane.setDividerLocation(150);
        splitPane.setPreferredSize(new Dimension(WIDTH, HEIGHT));
    }

    // represents information of a tree node
    private static class NodeInfo {
        private final String name;
        private final String absPath;

        public NodeInfo(String name, String absPath) {
            this.name = name;
            this.absPath = absPath;
        }

        public String toString() {
            return name;
        }
    }

    // represents TreePopup menu
    private class TreePopup extends JPopupMenu {
        public TreePopup(JTree tree) {
            addNewFileMenuItems();
            addNewFolderMenuItems();
            addDeleteMenuItem();
        }

        // EFFECTS: generate and initialize the "new file" operation for non-leaf tree node
        private void addNewFileMenuItems() {
            JMenuItem newFile = new JMenuItem("New File");
            newFile.addActionListener(ae -> {
                TreePath selectedPath = tree.getSelectionPath();
                String fileName = JOptionPane.showInputDialog(null,
                        "Enter File Name",
                        "Create a new file",
                        JOptionPane.QUESTION_MESSAGE);

                if (selectedPath != null) {
                    DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) selectedPath.getLastPathComponent();
                    try {
                        createLeafNode(selectedNode, fileName);
                    } catch (NotFoundException | IllegalNameException | DuplicateException e) {
                        JOptionPane.showMessageDialog(null, e.getMessage(), "System Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            });
            add(newFile);
        }

        // EFFECTS: add file with the given nodePath
        private void createLeafNode(DefaultMutableTreeNode node, String fileName)
                throws DuplicateException, NotFoundException, IllegalNameException {
            String parentNodePath = getAbsPathFromNode(node);
            if (!isLeafNode(node)) {
                String leafNodePath = parentNodePath + "/" + fileName;
                fsManager.createFile(leafNodePath);
                fsManager.save();
                updateTree();
                JOptionPane.showMessageDialog(null, "File Created!", "File Created!",
                        JOptionPane.INFORMATION_MESSAGE);
            }

        }

        // EFFECTS: generate and initialize the "new file" operation for non-leaf tree node
        private void addNewFolderMenuItems() {
            JMenuItem newFile = new JMenuItem("New Folder");
            newFile.addActionListener(ae -> {
                TreePath selectedPath = tree.getSelectionPath();
                String dirName = JOptionPane.showInputDialog(null,
                        "Enter Folder Name",
                        "Create a new folder",
                        JOptionPane.QUESTION_MESSAGE);

                if (selectedPath != null) {
                    DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) selectedPath.getLastPathComponent();
                    try {
                        createNonLeafNode(selectedNode, dirName);
                    } catch (NotFoundException | IllegalNameException | DuplicateException e) {
                        JOptionPane.showMessageDialog(null, e.getMessage(), "System Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            });
            add(newFile);
        }

        // EFFECTS: add file with the given nodePath
        private void createNonLeafNode(DefaultMutableTreeNode node, String dirName)
                throws DuplicateException, NotFoundException, IllegalNameException {
            String parentNodePath = getAbsPathFromNode(node);
            if (!isLeafNode(node)) {
                String leafNodePath = parentNodePath + "/" + dirName;
                fsManager.createDir(leafNodePath);
                fsManager.save();
                updateTree();
                JOptionPane.showMessageDialog(null, "File Created!", "File Created!",
                        JOptionPane.INFORMATION_MESSAGE);
            }

        }

        // EFFECTS: generate and initialize the "delete" operation for tree nodes
        private void addDeleteMenuItem() {
            JMenuItem delete = new JMenuItem("Delete");
            delete.addActionListener(ae -> {
                TreePath selectedPath = tree.getSelectionPath();
                if (selectedPath != null) {
                    DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) selectedPath.getLastPathComponent();
                    try {
                        deleteNode(selectedNode);
                    } catch (NotFoundException | IllegalNameException e) {
                        JOptionPane.showMessageDialog(null, e.getMessage(), "System Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            });
            add(delete);
        }

        // EFFECTS: delete file or subdirectories with the given nodePath
        private void deleteNode(DefaultMutableTreeNode node) throws NotFoundException, IllegalNameException {
            String msg = "";
            String nodePath = getAbsPathFromNode(node);
            if (isLeafNode(node)) {
                fsManager.deleteFile(nodePath);
                msg = "Deleted file: " + nodePath;
            } else {
                fsManager.deleteDir(nodePath);
                msg = "Deleted directory: " + nodePath;
            }
            fsManager.save();
            updateTree();
            JOptionPane.showMessageDialog(null, msg, "Deleted!", JOptionPane.INFORMATION_MESSAGE);
        }

        // EFFECTS: return true if the given node is a leaf node
        private boolean isLeafNode(DefaultMutableTreeNode node) {
            return node.isLeaf() && !node.getAllowsChildren();
        }

        // EFFECTS: return absPath in file system given the DefaultTreeNode
        private String getAbsPathFromNode(DefaultMutableTreeNode node) {
            return ((NodeInfo) node.getUserObject()).absPath;
        }
    }
}
