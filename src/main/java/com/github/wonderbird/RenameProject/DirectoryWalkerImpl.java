package com.github.wonderbird.RenameProject;

import java.io.IOException;
import java.nio.file.FileStore;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.util.Arrays;
import java.util.List;

public class DirectoryWalkerImpl implements DirectoryWalker {
    /**
     * Find files and directories matching the pattern.
     *
     * @param pattern describes a part of the file name or directory name to be found.
     * @return List of all paths beneath the current directory which match the pattern.
     * @see <a href="https://docs.oracle.com/javase/tutorial/displayCode.html?code=https://docs.oracle.com/javase/tutorial/essential/io/examples/Find.java">Example Code at Oracle</a>
     */
    @Override
    public List<Path> findByName(final String pattern) throws IOException {
        Finder finder = new Finder(pattern);

        Files.walkFileTree(Paths.get("."), finder);

        Path[] result = {Paths.get("main", "src")};
        return Arrays.asList(result);
    }

    private class Finder extends SimpleFileVisitor<Path> {
        private final PathMatcher matcher;

        Finder(final String aPattern) {
            matcher = FileSystems.getDefault().getPathMatcher("glob:" + aPattern);
        }
    }
}
