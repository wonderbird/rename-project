package com.github.wonderbird.RenameProject.FileSystemAccess.Implementation;

import com.github.wonderbird.RenameProject.FileSystemAccess.Interfaces.FilePathVisitorWithResult;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

class FileNameMatchingVisitorImpl extends SimpleFileVisitor<Path> implements FilePathVisitorWithResult {
    private final PathMatcher matcher;
    private final PathExclusionPatterns exclusionPatterns;
    private List<Path> result= new ArrayList<>();

    /**
     * @param aFileNamePattern describes a part of the file name or directory name to be found.
     * @param aExclusions optional list of patterns listing files and directories to exclude from the result.
     */
    FileNameMatchingVisitorImpl(final String aFileNamePattern, final String[] aExclusions) {
        matcher = FileSystems.getDefault().getPathMatcher("glob:" + aFileNamePattern);
        exclusionPatterns = new PathExclusionPatterns(aExclusions);
    }

    @Override
    public List<Path> getResult() {
        return result;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        return match(file);
    }

    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
        return match(dir);
    }

    private FileVisitResult match(Path path) {
        Path normalizedPath = path.toAbsolutePath().normalize();

        boolean isExcluded = exclusionPatterns.isExcluded(normalizedPath);
        if (!isExcluded && matcher.matches(path.getFileName())) {
            result.add(normalizedPath);
        }

        return FileVisitResult.CONTINUE;
    }
}