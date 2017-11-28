package com.github.wonderbird.RenameProject.FileSystemAccess.Interfaces;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public interface FilePathFinder {
    List<Path> find(String from) throws IOException;
}
