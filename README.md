# Product Overview

RenameProject gives you the power to create template projects in Java,
from which you can easily create new Java Projects.

## Key Functions

* RenameProject facilitates changing the project name for a Java Code base.
* You enter the original and the new project name. RenameProject
  changes the name in all files, directories and file contents accordingly.
* RenameProject automatically finds out different versions of the
  project names like camel case writing, space and dash separated versions
  and others.
* RenameProject has been tested with IntelliJ and Maven based projects.

## Usage Instructions

These instructions assumes that you would like to change the name of
your Java project from **Original Project** to **My New Project**.

<div style="text-align:center">
  <img alt="Screenshot of the RenameProject Graphical User Interface" src="https://github.com/wonderbird/rename-project/blob/master/doc/screenshot.png" width="400" />
</div>

1. Open RenameProject and browse to the directory containing the
   **Original Project** files (e.g. /Users/*username*/projects/original-project).

1. In the **From** edit field enter the original project name:
   **OriginalProject**

1. In the **To** edit field enter the desired new project name:
   **MyNewProject**

1. Use the checkboxes to select the text occurrences to replace in
   file names, directory names and file contents

1. Press the **Rename** Button to start the renaming process.

1. After the renaming process has finished, the application window
   disappears.

1. Manually rename the top level project directory.

## Behind the Scenes

RenameProject prints out the steps it performs to the console window. If
you would like to see this work log, then launch RenameProject from
the **Terminal** program:

```
$ /Applications/RenameProject.app/Contents/MacOS/JavaAppLauncher
Missing required options: f, t

usage: java RenameProject [-d <arg>] -f <arg> -t <arg>
-d,--dir <arg>    directory benath which to replace the project name
-f,--from <arg>   project name to replace
-t,--to <arg>     replacement project name
```

As you can see, it is possible to pass the **project directory** as
well as the **from** and **to** project names on the command line.
In that case RenameProject will not generate derived project names but
it will replace all **from** values by **to** in file names, directory
names and file contents.

If you omit a required option, then RenameProject launches the graphical
user interface. If you omit the **project directory**, then RenameProject
will use the current directory as a starting point.

After the renaming process has been started, RenameProject prints out
the steps it performs and the file and directory names affected by
that step:

```
INFORMATION: Renaming from 'OriginalProject' to 'MyNewProject' ...
...
Jan 18, 2018 5:43:31 AM com.github.wonderbird.RenameProject.Logic.RenameProjectManagerImpl renameFilesAndDirectories
INFORMATION: /Users/username/original-project/src/main/java/.../Logic/OriginalProjectManagerImpl.java -> /Users/username/original-project/src/main/java/.../Logic/MyNewProjectManagerImpl.java
...
Jan 18, 2018 5:43:40 AM com.github.wonderbird.RenameProject.Logic.RenameProjectManagerImpl replaceFileContents
INFORMATION: Replace contents: /Users/username/original-project/dependency-reduced-pom.xml
...
```
