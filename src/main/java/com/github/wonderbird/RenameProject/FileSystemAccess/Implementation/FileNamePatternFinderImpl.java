package com.github.wonderbird.RenameProject.FileSystemAccess.Implementation;

import com.github.wonderbird.RenameProject.FileSystemAccess.Interfaces.FilePathFinder;
import com.github.wonderbird.RenameProject.FileSystemAccess.Interfaces.FilePathVisitorWithResult;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;

public class FileNamePatternFinderImpl implements FilePathFinder {
    private FilePathVisitorWithResult visitor;

    public FileNamePatternFinderImpl() {
        this(null);
    }

    public FileNamePatternFinderImpl(FilePathVisitorWithResult aVisitor) {
        visitor = aVisitor;
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
        if (visitor == null) {
            visitor = new FileNameMatchingVisitorImpl(aPattern);
        }

        Files.walkFileTree(Paths.get(aStartDirectory), visitor);

        return visitor.getResult();
    }
}
