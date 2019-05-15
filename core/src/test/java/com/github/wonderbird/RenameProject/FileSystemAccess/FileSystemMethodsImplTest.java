package com.github.wonderbird.RenameProject.FileSystemAccess;

import com.github.wonderbird.RenameProject.Models.Configuration;
import com.github.wonderbird.RenameProject.FileSystemAccess.Implementation.FileSystemMethodsImpl;
import org.junit.After;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static org.junit.Assert.*;

public class FileSystemMethodsImplTest {
    @After
    public void after() {
        Configuration.getConfiguration().reset();
    }

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

    @Test
    public void replaceInFile_FromArgumentIsFoundInFile_ReplacesFromByTo() throws IOException {
        executeReplaceInFileTest();
    }

    @Test
    public void replaceInFile_FromArgumentAtBufferBoundary_ReplacesFromByTo() throws IOException {
        Configuration.getConfiguration().setReadBufferSize(9);
        executeReplaceInFileTest();
    }

    private void executeReplaceInFileTest() throws IOException {
        Path path = Paths.get("target", "test-classes", "fileContentToBeReplaced.txt");

        FileSystemMethodsImpl fileSystemMethods = new FileSystemMethodsImpl();
        fileSystemMethods.replaceInFile(path, "FROM", "TO");

        boolean fileContentMatchesTo;
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line = reader.readLine();
            fileContentMatchesTo = line.matches(".*TO.*");
        }

        fileSystemMethods.replaceInFile(path, "TO", "FROM");

        boolean fileContentMatchesFrom;
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line = reader.readLine();
            fileContentMatchesFrom = line.matches(".*FROM.*");
        }

        assertTrue("'FROM' should be replaced by 'TO' in '" + path.toString() + "'", fileContentMatchesTo);
        assertTrue("Changes should be reverted - 'TO' should be replaced by 'FROM' in '" + path.toString() + "'", fileContentMatchesFrom);
    }
}