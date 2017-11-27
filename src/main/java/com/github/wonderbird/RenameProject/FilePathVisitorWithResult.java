package com.github.wonderbird.RenameProject;

import java.nio.file.FileVisitor;
import java.nio.file.Path;
import java.util.List;

public interface FilePathVisitorWithResult extends FileVisitor<Path> {
    List<Path> getResult();
}
