package com.github.wonderbird.RenameProject.FileSystemAccess.Interfaces;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public interface FileNamePatternFinder {
    List<Path> find(String pattern) throws IOException;
}
