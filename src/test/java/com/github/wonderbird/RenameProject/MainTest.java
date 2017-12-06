package com.github.wonderbird.RenameProject;

import com.github.wonderbird.RenameProject.FileSystemAccess.Interfaces.FilePathFinder;
import com.github.wonderbird.RenameProject.FileSystemAccess.Interfaces.FileSystemMethods;
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

public class MainTest {
    private String[] args;

    private Configuration config;

    private ArgumentParser parser;

    private FilePathFinder fileNamePatternFinder;

    private FilePathFinder fileContentFinder;

    private Logger logger;

    private final String exceptionMessage = "Exception thrown by unit test";

    @Before
    public void before() throws WrongUsageException {
        final String fromPattern = "Main";
        final String toArgument = "Renamed";
        args = new String[]{"--from", fromPattern, "--to", toArgument};

        config = Configuration.getConfiguration();
        config.setFrom(fromPattern);
        config.setTo(toArgument);

        parser = mock(ArgumentParser.class);
        Main.setArgumentParser(parser);

        fileNamePatternFinder = mock(FilePathFinder.class);
        Main.setFileNamePatternFinder(fileNamePatternFinder);

        fileContentFinder = mock(FilePathFinder.class);
        Main.setFileContentFinder(fileContentFinder);

        logger = mock(Logger.class);
        Main.setLogger(logger);
    }

    @Test
    public void main_ArgumentsGiven_InvokesArgumentParserWithAllArguments() throws WrongUsageException {
        Main.main(args);

        verify(parser).parse(args);
    }

    @Test
    public void main_ArgumentsGiven_SearchesForFilenamesMatchingFrom() throws IOException {
        Main.main(args);

        verify(fileNamePatternFinder).find("*" + config.getFrom() + "*");
    }

    @Test
    public void main_ArgumentsGiven_SearchesForFilesContainingFrom() throws IOException {
        Main.main(args);

        verify(fileContentFinder).find(config.getFrom());
    }

    @Test
    public void main_ArgumentParserThrowsUsageException_HandlesException() throws WrongUsageException {
        parser = mock(ArgumentParser.class);
        doThrow(new WrongUsageException("Exception thrown by unit test")).when(parser).parse(any(String[].class));

        Main.main(args);
    }

    @Test
    public void main_DirectoryFinderThrowsException_HandlesException() throws IOException {
        fileNamePatternFinder = mock(FilePathFinder.class);
        when(fileNamePatternFinder.find(any())).thenThrow(new IOException(exceptionMessage));

        Main.main(args);
    }

    @Test
    public void main_DirectoryFinderReturnsFiles_RenamesEachFileToToPattern() throws IOException {
        fileNamePatternFinder = mock(FilePathFinder.class);
        List<Path> fromPaths = Arrays.asList(
                Paths.get("src", "main", "java", "com", "github", "wonderbird", "RenameProject", "Main.java"),
                Paths.get("src", "test", "java", "com", "github", "wonderbird", "RenameProject", "MainTest.java"),
                Paths.get("target", "classes", "com", "github", "wonderbird", "RenameProject", "Main.class")
        );
        when(fileNamePatternFinder.find(any())).thenReturn(fromPaths);
        Main.setFileNamePatternFinder(fileNamePatternFinder);

        FileSystemMethods fileSystemMethods = mock(FileSystemMethods.class);
        Main.setFileSystemMethods(fileSystemMethods);

        Main.main(args);

        for (Path fromPath : fromPaths) {
            Path toPath = Paths.get(fromPath.toString().replace(config.getFrom(), config.getTo()));
            verify(fileSystemMethods).move(fromPath, toPath, REPLACE_EXISTING);
        }
        verify(fileSystemMethods, times(3)).move(any(), any(), any());
    }

    @Test
    public void main_FileContentFinderReturnsFiles_ReplacesFromByToInEachFile() throws IOException {
        fileContentFinder = mock(FilePathFinder.class);
        List<Path> affectedFiles = Arrays.asList(
                Paths.get("src", "main", "java", "com", "github", "wonderbird", "RenameProject", "Main.java"),
                Paths.get("src", "test", "java", "com", "github", "wonderbird", "RenameProject", "MainTest.java")
        );
        when(fileContentFinder.find(any())).thenReturn(affectedFiles);
        Main.setFileContentFinder(fileContentFinder);

        FileSystemMethods fileSystemMethods = mock(FileSystemMethods.class);
        Main.setFileSystemMethods(fileSystemMethods);

        Main.main(args);

        for (Path affectedFile : affectedFiles) {
            verify(fileSystemMethods).replaceInFile(affectedFile, config.getFrom(), config.getTo());
        }
        verify(fileSystemMethods, times(2)).replaceInFile(any(), any(), any());
    }
}
