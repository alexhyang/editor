# My personal project - A Text Editor
This project is a ***light-weight text editor*** similar to Notepad++ intended 
for computer users. The editor allows users to create new files in computer, 
open existing files, update the content, and save them to system. 

The reason I decide to create this project is that some seemingly simple desktop
software could have a complex logic behind the screen, such as the Windows' 
built-in Notepad software. Although reinventing the wheel is not a good approach
in practical software construction, it provides an ideal chance to understand 
the problems that will occur under similar circumstances.

## User Stories
### Phase 0 requirements:
- be able to add X to Y
- be able to view a list of X in Y
- (and two more user stories)

### Phase 2 requirements:
- be able to save the entire state of the application
- be able to reload the saved state

### User Stories of this project:
- As a user, I want to be able to add new files in a directory
- As a user, I want to be able to read the content of any file in the directory
- As a user, I want to be able to load/open any file in the directory in editor
- As a user, I want to be able to edit and save files in the editor
- As a user, I want to be able to view a list of all files in the directory

### Phase 3 requirements:
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

## References
1.  [startScreen1 image](./data/startScreen1.jpg): https://pbs.twimg.com/media/BnI4JxfCEAA5fZA.jpg
1.  [startScreen2 image](./data/startScreen2.jpg): https://i.imgur.com/Ofj8dxE.jpeg