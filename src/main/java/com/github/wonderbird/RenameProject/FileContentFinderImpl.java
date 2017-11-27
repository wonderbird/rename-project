package com.github.wonderbird.RenameProject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class FileContentFinderImpl implements FilePathFinder {
    private FileContentMatchingVisitorImpl visitor;

    /**
     * Find all files containing the search string.
     *
     * @param searchString is the word which should be searched in all files.
     * @return List of all files beneath the current directory which contain searchString.
     */
    public List<Path> find(String searchString) throws IOException {
        if (visitor == null) {
            visitor = new FileContentMatchingVisitorImpl(searchString);
        }

        Files.walkFileTree(Paths.get("."), visitor);

        return visitor.getResult();
    }
}
