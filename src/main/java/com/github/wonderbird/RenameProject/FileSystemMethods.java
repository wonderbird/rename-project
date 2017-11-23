package com.github.wonderbird.RenameProject;

import java.nio.file.CopyOption;
import java.nio.file.Path;

public interface FileSystemMethods {
    void move(Path source, Path target, CopyOption copyOption);
}
