package com.github.wonderbird.RenameProject.FileSystemAccess.Implementation;

import com.github.wonderbird.RenameProject.FileSystemAccess.Interfaces.FilePathFinder;

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
     * @param aStartDirectory is the directory below which the pattern shall be searched.
     * @param aPattern is the word which should be searched in all files.
     * @return List of all files beneath the current directory which contain searchString.
     */
    public List<Path> find(String aStartDirectory, String aPattern) throws IOException {
        if (visitor == null) {
            visitor = new FileContentMatchingVisitorImpl(aPattern);
        }

        Files.walkFileTree(Paths.get(aStartDirectory), visitor);

        return visitor.getResult();
    }
}
