package com.github.wonderbird.RenameProject;

import java.nio.file.FileVisitor;
import java.nio.file.Path;
import java.util.List;

public interface FileNameMatchingVisitor extends FileVisitor<Path> {
    List<Path> getResult();
}
