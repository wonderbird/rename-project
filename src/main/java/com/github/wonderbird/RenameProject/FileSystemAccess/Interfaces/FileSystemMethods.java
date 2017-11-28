package com.github.wonderbird.RenameProject.FileSystemAccess.Interfaces;

import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Path;

public interface FileSystemMethods {
    void move(Path source, Path target, CopyOption copyOption) throws IOException;
}
