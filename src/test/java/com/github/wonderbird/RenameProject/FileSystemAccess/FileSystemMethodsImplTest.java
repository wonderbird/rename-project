package com.github.wonderbird.RenameProject.FileSystemAccess;

import com.github.wonderbird.RenameProject.FileSystemAccess.Implementation.FileSystemMethodsImpl;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static org.junit.Assert.*;

public class FileSystemMethodsImplTest {

    @Test
    public void move_SourceArgumentIsFoundInResources_RenamesSourceToTarget() throws IOException {
        Path sourcePath = Paths.get("target", "test-classes", "fileToBeRenamed.txt");
        Path targetPath = sourcePath.resolveSibling("targetfile.txt");

        FileSystemMethodsImpl fileSystemMethods = new FileSystemMethodsImpl();
        fileSystemMethods.move(sourcePath, targetPath, REPLACE_EXISTING);

        File sourceFile = new File(sourcePath.toString());
        assertFalse("Source file still exists after moving", sourceFile.exists());

        File targetFile = new File(targetPath.toString());
        assertTrue("Target file does not exist after moving", targetFile.exists());

        fileSystemMethods.move(targetPath, sourcePath, REPLACE_EXISTING);

        assertTrue("Source file does not exist after moving back", sourceFile.exists());
        assertFalse("Target file still exists after moving back", targetFile.exists());
    }
}