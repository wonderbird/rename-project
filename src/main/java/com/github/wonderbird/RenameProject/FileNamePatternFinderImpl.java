package com.github.wonderbird.RenameProject;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;

public class FileNamePatternFinderImpl implements FileNamePatternFinder {
    private FileNameMatchingVisitor visitor;

    FileNamePatternFinderImpl() {
        this(null);
    }

    FileNamePatternFinderImpl(FileNameMatchingVisitor aVisitor) {
        visitor = aVisitor;
    }

    /**
     * Find files and directories matching the pattern.
     *
     * @param pattern describes a part of the file name or directory name to be found.
     * @return List of all paths beneath the current directory which match the pattern.
     * @see <a href="https://docs.oracle.com/javase/tutorial/displayCode.html?code=https://docs.oracle.com/javase/tutorial/essential/io/examples/Find.java">Example Code at Oracle</a>
     */
    @Override
    public List<Path> find(final String pattern) throws IOException {
        if (visitor == null) {
            visitor = new FileNameMatchingVisitorImpl(pattern);
        }

        Files.walkFileTree(Paths.get("."), visitor);

        return visitor.getResult();
    }
}
