package com.github.wonderbird.RenameProject;

import org.junit.Test;

import java.nio.file.Paths;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static org.junit.Assert.*;

public class FileSystemMethodsImplTest {

    @Test
    public void move_SourceArgumentIsFoundInResources_RenamesSourceToTarget() {
        FileSystemMethodsImpl fileSystemMethods = new FileSystemMethodsImpl();
        fileSystemMethods.move(Paths.get("somefile"), Paths.get("targetfile"), REPLACE_EXISTING);
    }
}