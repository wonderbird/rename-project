package com.github.wonderbird.RenameProject;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.nio.file.CopyOption;
import java.nio.file.Path;

public class FileSystemMethodsImpl implements FileSystemMethods {
    @Override
    public void move(Path source, Path target, CopyOption copyOption) {
        throw new NotImplementedException();
    }
}
