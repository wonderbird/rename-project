package com.github.wonderbird.RenameProject.FileSystemAccess.Implementation;

import com.github.wonderbird.RenameProject.FileSystemAccess.Interfaces.FileSystemMethods;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileSystemMethodsImpl implements FileSystemMethods {
    @Override
    public void move(Path source, Path target, CopyOption copyOption) throws IOException {
        Files.move(source, target, copyOption);
    }

    @Override
    public void replaceInFile(Path affectedFile, String aFrom, String aTo) {
        throw new NotImplementedException();
    }
}
