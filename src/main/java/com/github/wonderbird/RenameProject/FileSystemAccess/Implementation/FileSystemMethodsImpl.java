package com.github.wonderbird.RenameProject.FileSystemAccess.Implementation;

import com.github.wonderbird.RenameProject.FileSystemAccess.Interfaces.FileSystemMethods;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileSystemMethodsImpl implements FileSystemMethods {
    // TODO: Move BUFFER_SIZE constant to configuration file.
    private static final int BUFFER_SIZE = 1024 * 1024;

    @Override
    public void move(Path source, Path target, CopyOption copyOption) throws IOException {
        Files.move(source, target, copyOption);
    }

    @Override
    public void replaceInFile(Path affectedFile, String aFrom, String aTo) throws IOException {
        InputStream inputStream = Files.newInputStream(affectedFile);
        try (InputStreamReader reader = new InputStreamReader(inputStream)) {
            char[] buffer = new char[BUFFER_SIZE];
            while ((reader.read(buffer, 0, BUFFER_SIZE)) > 0) {
                // TODO: Continue here ...
            }
        }
    }
}
