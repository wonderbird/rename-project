package com.github.wonderbird.RenameProject;

import java.nio.file.Path;
import java.util.List;

public interface FileContentFinder {
    List<Path> find(String from);
}
