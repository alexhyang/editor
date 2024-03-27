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

    private JEditorPane editorPane;
    private JTree tree;

    public EditorUI(Dir rootDir, int width) {
        super(new GridLayout(0, 1));
        this.WIDTH = width;

        // create the nodes
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(".");
        createNodes(root, rootDir);

        // create a tree that allows one selection at a time
        tree = new JTree(root);
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);


        // create the scroll pane and add the tree to it
        JScrollPane treeView = new JScrollPane(tree);
        editorPane = new JEditorPane();
        editorPane.setEditable(false);
        JScrollPane editorView = new JScrollPane(editorPane);

        // add the scroll panes to a split pane
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

        // add the split pane to this panel
        add(splitPane);
    }

    private void createNodes(DefaultMutableTreeNode treeNode, Dir dir) {
        addDirectoryNodes(treeNode, dir);
        addFileNodes(treeNode, dir);
    }

    private void addDirectoryNodes(DefaultMutableTreeNode treeNode, Dir dir) {
        for (String subDirName : dir.getOrderedSubDirNames()) {
            try {
                Dir subDir = dir.getSubDir(subDirName);
                DefaultMutableTreeNode subDirTreeNode = new DefaultMutableTreeNode(subDirName);
                createNodes(subDirTreeNode, subDir);
                treeNode.add(subDirTreeNode);
            } catch (IllegalNameException | NotFoundException e) {
                // TODO: handle exception later
                throw new RuntimeException(e);
            }
        }
    }

    private void addFileNodes(DefaultMutableTreeNode treeNode, Dir dir) {
        for (String fileName : dir.getOrderedFileNames()) {
            DefaultMutableTreeNode fileTreeNode = new DefaultMutableTreeNode(fileName);
            treeNode.add(fileTreeNode);
        }
    }

    @Override
    public void valueChanged(TreeSelectionEvent e) {
        // TODO: implement method
    }
}
