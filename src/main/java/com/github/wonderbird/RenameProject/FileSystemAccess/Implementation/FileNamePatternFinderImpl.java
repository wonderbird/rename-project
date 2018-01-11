package com.github.wonderbird.RenameProject.FileSystemAccess.Implementation;

import com.github.wonderbird.RenameProject.FileSystemAccess.Interfaces.FilePathFinder;
import com.github.wonderbird.RenameProject.FileSystemAccess.Interfaces.FilePathVisitorWithResult;

import java.io.IOException;
import java.nio.file.*;
import java.util.Collections;
import java.util.List;

public class FileNamePatternFinderImpl implements FilePathFinder {
    private FilePathVisitorWithResult visitorMock;

    public FileNamePatternFinderImpl() {
        this(null);
    }

    /**
     * Constructor for unit tests allowing to set a visitor mock.
     *
     * @param aVisitorMock Mock for the FilePathVisitorWithResult used in the {@link #find(String, String)} method.
     */
    public FileNamePatternFinderImpl(FilePathVisitorWithResult aVisitorMock) {
        visitorMock = aVisitorMock;
    }

    /**
     * Find files and directories matching the pattern.
     *
     * @param aStartDirectory is the directory below which all occurrences of aPattern will be searched.
     * @param aPattern describes a part of the file name or directory name to be found.
     * @return List of all paths beneath the current directory which match the pattern.
     * @see <a href="https://docs.oracle.com/javase/tutorial/displayCode.html?code=https://docs.oracle.com/javase/tutorial/essential/io/examples/Find.java">Example Code at Oracle</a>
     */
    @Override
    public List<Path> find(String aStartDirectory, final String aPattern) throws IOException {
        FilePathVisitorWithResult visitor;
        if (visitorMock == null) {
            visitor = new FileNameMatchingVisitorImpl(aPattern);
        } else {
            visitor = visitorMock;
        }

        Files.walkFileTree(Paths.get(aStartDirectory), visitor);

        List<Path> result = visitor.getResult();

        sortDepthFirst(result);
        removeStartDirFromPaths(result, aStartDirectory);

        return result;
    }

    private void sortDepthFirst(List<Path> result) {
        Collections.sort(result);
        Collections.reverse(result);
    }

    private void removeStartDirFromPaths(List<Path> aPaths, String aStartDirectory) {
        Path startDirPath = Paths.get(aStartDirectory).toAbsolutePath();
        aPaths.remove(startDirPath);
    }
}
