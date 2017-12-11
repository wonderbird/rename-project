package com.github.wonderbird.RenameProject.Logic;

import com.github.wonderbird.RenameProject.FileSystemAccess.Interfaces.FilePathFinder;
import com.github.wonderbird.RenameProject.FileSystemAccess.Interfaces.FileSystemMethods;
import com.github.wonderbird.RenameProject.Models.Configuration;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class RenameProjectManagerImplTest {

    private Configuration config;

    private FilePathFinder fileNamePatternFinder;

    private FilePathFinder fileContentFinder;

    private FileSystemMethods fileSystemMethods;

    private Logger logger;

    private final String exceptionMessage = "Exception thrown by unit test";

    private RenameProjectManagerImpl renameProjectManager;

    @Before
    public void before() throws WrongUsageException {
        final String fromPattern = "Main";
        final String toArgument = "Renamed";

        config = Configuration.getConfiguration();
        config.setFrom(fromPattern);
        config.setTo(toArgument);

        renameProjectManager = new RenameProjectManagerImpl();

        fileSystemMethods = mock(FileSystemMethods.class);
        renameProjectManager.setFileSystemMethods(fileSystemMethods);

        fileNamePatternFinder = mock(FilePathFinder.class);
        renameProjectManager.setFileNamePatternFinder(fileNamePatternFinder);

        fileContentFinder = mock(FilePathFinder.class);
        renameProjectManager.setFileContentFinder(fileContentFinder);

        logger = mock(Logger.class);
        renameProjectManager.setLogger(logger);
    }

    @Test
    public void renameProject_ConfigurationGiven_SearchesForFilenamesMatchingFrom() throws IOException {
        renameProjectManager.renameProject();

        verify(fileNamePatternFinder).find(".", "*" + config.getFrom() + "*");
    }

    @Test
    public void renameProject_ConfigurationGiven_SearchesForFilesContainingFrom() throws IOException {
        renameProjectManager.renameProject();

        verify(fileContentFinder).find(".", config.getFrom());
    }

    @Test
    public void renameProject_ConfigurationContainsStartDirectory_SearchesFilesBelowStartDirectory() throws IOException {
        config.setStartDir("src");

        verify(fileNamePatternFinder).find(config.getStartDir(), "*" + config.getFrom() + "*");
    }

    @Test(expected = IOException.class)
    public void renameProject_FileNamePatternFinderThrowsException_RaisesException() throws IOException {
        fileNamePatternFinder = mock(FilePathFinder.class);
        when(fileNamePatternFinder.find(eq("."), any())).thenThrow(new IOException(exceptionMessage));
        renameProjectManager.setFileNamePatternFinder(fileNamePatternFinder);

        renameProjectManager.renameProject();
    }

    @Test
    public void renameProject_FileNamePatternFinderReturnsFiles_RenamesEachFileToToPattern() throws IOException {
        fileNamePatternFinder = mock(FilePathFinder.class);
        List<Path> fromPaths = Arrays.asList(
                Paths.get("src", "main", "java", "com", "github", "wonderbird", "RenameProject", "Main.java"),
                Paths.get("src", "test", "java", "com", "github", "wonderbird", "RenameProject", "MainTest.java"),
                Paths.get("target", "classes", "com", "github", "wonderbird", "RenameProject", "Main.class")
        );
        when(fileNamePatternFinder.find(eq("."), any())).thenReturn(fromPaths);
        renameProjectManager.setFileNamePatternFinder(fileNamePatternFinder);

        renameProjectManager.renameProject();

        for (Path fromPath : fromPaths) {
            Path toPath = Paths.get(fromPath.toString().replace(config.getFrom(), config.getTo()));
            verify(fileSystemMethods).move(fromPath, toPath, REPLACE_EXISTING);
        }
        verify(fileSystemMethods, times(3)).move(any(), any(), any());
    }

    @Test
    public void renameProject_FileContentFinderReturnsFiles_ReplacesFromByToInEachFile() throws IOException {
        fileContentFinder = mock(FilePathFinder.class);
        List<Path> affectedFiles = Arrays.asList(
                Paths.get("src", "main", "java", "com", "github", "wonderbird", "RenameProject", "Main.java"),
                Paths.get("src", "test", "java", "com", "github", "wonderbird", "RenameProject", "MainTest.java")
        );
        when(fileContentFinder.find(eq("."), any())).thenReturn(affectedFiles);

        renameProjectManager.setFileContentFinder(fileContentFinder);

        renameProjectManager.renameProject();

        for (Path affectedFile : affectedFiles) {
            verify(fileSystemMethods).replaceInFile(affectedFile, config.getFrom(), config.getTo());
        }
        verify(fileSystemMethods, times(2)).replaceInFile(any(), any(), any());
    }
}
