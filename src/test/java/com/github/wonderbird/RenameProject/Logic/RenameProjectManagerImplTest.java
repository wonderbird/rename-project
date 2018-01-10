package com.github.wonderbird.RenameProject.Logic;

import com.github.wonderbird.RenameProject.FileSystemAccess.Implementation.FileNamePatternFinderImpl;
import com.github.wonderbird.RenameProject.FileSystemAccess.Interfaces.FilePathFinder;
import com.github.wonderbird.RenameProject.FileSystemAccess.Interfaces.FileSystemMethods;
import com.github.wonderbird.RenameProject.Models.Configuration;
import com.github.wonderbird.RenameProject.Models.RenameFromToPair;
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
    public void before() {
        final String fromPattern = "Main";
        final String toArgument = "Renamed";
        final String startDir = ".";

        config = Configuration.getConfiguration();
        config.reset();
        config.addFromToPair(fromPattern, toArgument);
        config.setStartDir(startDir);

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
    public void renameProject_MultipleFromToPairsConfigured_SearchesForAllFilenamesMatchingFrom() throws IOException {
        config.reset();
        config.addFromToPair("From1", "To1");
        config.addFromToPair("From2", "To2");
        config.addFromToPair("From3", "To3");

        renameProjectManager.renameProject();

        for (RenameFromToPair pair : config.getFromToPairs()) {
            verify(fileNamePatternFinder).find(config.getStartDir(), "*" + pair.getFrom() + "*");
        }
    }
    @Test
    public void renameProject_ConfigurationGiven_SearchesForFilesContainingFrom() throws IOException {
        renameProjectManager.renameProject();

        RenameFromToPair fromToPair = config.getFromToPairs().get(0);
        verify(fileContentFinder).find(".", fromToPair.getFrom());
    }

    @Test
    public void renameProject_ConfigurationContainsStartDirectory_SearchesFilesBelowStartDirectory() throws IOException {
        config.setStartDir("src");

        renameProjectManager.renameProject();

        RenameFromToPair fromToPair = config.getFromToPairs().get(0);
        verify(fileNamePatternFinder).find(config.getStartDir(), "*" + fromToPair.getFrom() + "*");
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
        config.reset();
        config.addFromToPair("RenameProject", "Renamed");

        fileNamePatternFinder = mock(FilePathFinder.class);
        List<Path> fromPaths = Arrays.asList(
                Paths.get("src", "main", "java", "com", "github", "wonderbird", "RenameProject", "Logic", "RenameProjectManager.java"),
                Paths.get("src", "main", "java", "com", "github", "wonderbird", "RenameProject")
        );
        when(fileNamePatternFinder.find(eq("."), any())).thenReturn(fromPaths);
        renameProjectManager.setFileNamePatternFinder(fileNamePatternFinder);

        renameProjectManager.renameProject();

        List<Path> toPaths = Arrays.asList(
                Paths.get("src", "main", "java", "com", "github", "wonderbird", "RenameProject", "Logic", "RenamedManager.java"),
                Paths.get("src", "main", "java", "com", "github", "wonderbird", "Renamed")
        );


        for (int i = 0; i < fromPaths.size(); ++i) {
            Path fromPath = fromPaths.get(i);
            Path toPath = toPaths.get(i);

            verify(fileSystemMethods).move(fromPath, toPath, REPLACE_EXISTING);
        }
        verify(fileSystemMethods, times(2)).move(any(), any(), any());
    }

    @Test
    public void renameProject_FileNamePatternFinderReturnsFileWithTwoFromPatternInstances_ReplacesAllFromPatternsWithToPattern() throws IOException {
        config.reset();
        config.addFromToPair("RenameProject", "Renamed");

        fileNamePatternFinder = mock(FilePathFinder.class);
        Path fromPath = Paths.get("src", "main", "java", "com", "github", "wonderbird", "RenameProject", "Logic", "RenameProjectManagerRenameProject.java");
        when(fileNamePatternFinder.find(eq("."), any())).thenReturn(Arrays.asList(fromPath));
        renameProjectManager.setFileNamePatternFinder(fileNamePatternFinder);

        renameProjectManager.renameProject();

        Path toPath = Paths.get("src", "main", "java", "com", "github", "wonderbird", "RenameProject", "Logic", "RenamedManagerRenamed.java");
        verify(fileSystemMethods).move(fromPath, toPath, REPLACE_EXISTING);
    }

    /**
     * Bug-fix: renameProject applies the first from/to pair multiple times.
     *
     * If several from/to pairs are configured, then renameProject applies the FileSystemMethods.move operation
     * for the results of the first from/to pattern for each configured pair.
     *
     * @throws IOException is thrown by renameProject but not expected in this test.
     */
    @Test
    public void renameProject_TwoDifferentFromPatternsConfigured_RenamesFirstAndSecondFromPattern() throws IOException {
        config.reset();
        config.addFromToPair("RenameProjectManagerImplTest", "RenameProjectManagerImplTest");
        config.addFromToPair("ArgumentParserImplArgsParsingTest", "ArgumentParserImplArgsParsingTest");

        renameProjectManager.setFileNamePatternFinder(new FileNamePatternFinderImpl());
        renameProjectManager.renameProject();

        Path expectedFromPath = Paths.get("src", "test", "java", "com", "github", "wonderbird", "RenameProject", "Logic", "RenameProjectManagerImplTest.java").toAbsolutePath();
        verify(fileSystemMethods, atLeastOnce()).move(expectedFromPath, expectedFromPath, REPLACE_EXISTING);

        // Check bug-fix
        expectedFromPath = Paths.get("src", "test", "java", "com", "github", "wonderbird", "RenameProject", "Logic", "ArgumentParserImplArgsParsingTest.java").toAbsolutePath();
        verify(fileSystemMethods, atLeastOnce()).move(expectedFromPath, expectedFromPath, REPLACE_EXISTING);
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

        RenameFromToPair fromToPair = config.getFromToPairs().get(0);
        for (Path affectedFile : affectedFiles) {
            verify(fileSystemMethods).replaceInFile(affectedFile, fromToPair.getFrom(), fromToPair.getTo());
        }
        verify(fileSystemMethods, times(2)).replaceInFile(any(), any(), any());
    }
}
