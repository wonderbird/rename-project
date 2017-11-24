package com.github.wonderbird.RenameProject;

import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileSystemMethodsImpl implements FileSystemMethods {
    @Override
    public void move(Path source, Path target, CopyOption copyOption) throws IOException {
        Files.move(source, target, copyOption);
    }
}
