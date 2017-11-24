package com.github.wonderbird.RenameProject;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public interface FileNamePatternFinder {
    List<Path> find(String pattern) throws IOException;
}
