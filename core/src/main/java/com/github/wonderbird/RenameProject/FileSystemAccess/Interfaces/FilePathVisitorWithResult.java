package com.github.wonderbird.RenameProject.FileSystemAccess.Interfaces;

import java.nio.file.FileVisitor;
import java.nio.file.Path;
import java.util.List;

public interface FilePathVisitorWithResult extends FileVisitor<Path> {
    List<Path> getResult();
}
