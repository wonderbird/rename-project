package com.github.wonderbird.RenameProject;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

class FileNameMatchingVisitorImpl extends SimpleFileVisitor<Path> implements FileNameMatchingVisitor {
    private final PathMatcher matcher;
    private List<Path> result= new ArrayList<>();

    FileNameMatchingVisitorImpl(final String aPattern) {
        matcher = FileSystems.getDefault().getPathMatcher("glob:" + aPattern);
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
        if (matcher.matches(path.getFileName())) {
            Path normalizedPath = path.normalize().toAbsolutePath();
            result.add(normalizedPath);
        }

        return FileVisitResult.CONTINUE;
    }

}