package ui;

import model.Dir;
import model.exceptions.IllegalNameException;
import model.exceptions.NotFoundException;

import javax.swing.*;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;

// Represents an editor ui with file tree and editor pane
// Citation: Java Tutorial
//     https://docs.oracle.com/javase/tutorial/uiswing/components/tree.html
public class EditorUI extends JPanel implements TreeSelectionListener {
    private static int WIDTH;
    private static int HEIGHT = 500;
    private static final int DIVIDER_SIZE = 4;

    private final JSplitPane splitPane;
    private final JEditorPane editorPane;
    private final JScrollPane editorView;
    private JScrollPane treeView;
    private JTree tree;

    private final FileSystemManager fsManager;
    private String currentAbsPath;

    public EditorUI(FileSystemManager fsManager, int width) {
        super(new GridLayout(0, 1));
        WIDTH = width;
        this.fsManager = fsManager;

        // create and setup tree
        tree = generateTree();
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.addTreeSelectionListener(this);

        // create the scroll pane and add the tree to it
        editorPane = new JEditorPane();
        editorPane.setEditable(false);
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
        tree.addTreeSelectionListener(this);
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
}
