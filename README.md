# My personal project - A Text Editor
This project is a ***light-weight text editor*** similar to Notepad++ intended 
for computer users. The editor allows users to create new files in computer, 
open existing files, update the content, and save them to system. 

The reason I decide to create this project is that some seemingly simple desktop
software could have a complex logic behind the screen, such as the Windows' 
built-in Notepad software. Although reinventing the wheel is not a good approach
in practical software construction, it provides an ideal chance to understand 
the problems that will occur under similar circumstances.

## Phase 0 requirements:
### User Stories
- be able to add X to Y
- be able to view a list of X in Y
- (and two more user stories)

## Phase 2 requirements:
- be able to save the entire state of the application
- be able to reload the saved state

### User Stories of this project:
- As a user, I want to be able to add new files in a directory
- As a user, I want to be able to read the content of any file in the directory
- As a user, I want to be able to load/open any file in the directory in editor
- As a user, I want to be able to edit and save files in the editor
- As a user, I want to be able to view a list of all files in the directory

## Phase 3 requirements:
Stories supported in GUI:
- As a user, I want to be able to add multiple files to a directory (root directory or any subdirectories)
- As a user, I want to be able to read the content of any file in the directory
- As a user, I want to be able to load/open any file in the directory in editor
- As a user, I want to be able to edit and save files in the editor
- As a user, I want to be able to view a list of all files in a directory

Visual component in GUI:
- display an image

### Instructions for Graders:
1.  The image is shown as a background of the editor pane on the right when the program starts
1.  The list of all files and folders added to the file system is shown on the left (similar to layout of IntelliJ)
    as a file system tree. Click folders to show their metadata. Click files to show their contents. Double click
    folders to show or hide their children.
1.  To add a new file, click "File" in menu bar and then click "New File". Or simply use the shortcut "Ctrl + N". **The
    absolute path is required**. Adding a new folder is similar to adding a new file. Click "File" then "New Folder", or
    use the shortcut "Ctrl + F". Again, the absolute path is required. The absolute path must start with "~", which
    represents the root directory.
1.  After selecting a file, the editor pane will load its file content and become editable. Users can make any changes
    from now on. To save the updated content, navigate to "File" -> "Save File" or use the shortcut "Ctrl + s". If the
    save action is not performed, all of changes will be lost.
1.  Due to the nature of an text editor, the state of this application will be loaded when the program is started.

## Phase 4 requirements:
- log event when a directory or file is added to and removed from an existing directory

### Task 2
```
Sun Apr 07 14:49:05 PDT 2024
added file to root: file2
Sun Apr 07 14:49:05 PDT 2024
added file to root: fileToTestEventLog
Sun Apr 07 14:49:09 PDT 2024
updated file: fileToTestEventLog
```

### Task 3
1. UML diagram:
![UML Design Diagram](./UML_Design_Diagram.png)

1.  Project Potential Improvements and Refactoring:
    1.  The composite design pattern can be applied to the model.Dir and model.File. The custom file system supporting
        the editor is a tree structure. Each directory in the file system can contain files or subdirectories. It would
        be ideal to apply the composite design pattern for the two classes. An interface named "DirNode" can be added to
        model package and be implemented by the two classes.
    1.  The current implementation of Dir deals with the CRUD operations of files and subdirectories in the same class.
        It violates the single responsibility rule. Future development will need to separate the class into two task
        managers, e.g. file manager and directory manager.
    1.  The graphical user interface (specifically EditorUI class) regenerates JTree every time a file or directory is
        created or deleted. This implementation mirrors the directory tree structure to the DefaultTreeModel. When the
        file system is small and simple, the time it takes to recreate the JTree can be ignored. However, when the file
        structure gets a lot more complicated, it would be a better idea to implement a custom tree model and custom
        node type based on the directory tree structure.

## References
1.  [startScreen1 image](./data/startScreen1.jpg): https://pbs.twimg.com/media/BnI4JxfCEAA5fZA.jpg
1.  [startScreen2 image](./data/startScreen2.jpg): https://i.imgur.com/Ofj8dxE.jpeg