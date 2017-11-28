package com.github.wonderbird.RenameProject;

import com.github.wonderbird.RenameProject.FileSystemAccess.Interfaces.FilePathFinder;
import com.github.wonderbird.RenameProject.FileSystemAccess.Interfaces.FileSystemMethods;
import org.junit.Before;
import org.junit.Test;

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

    private final String exceptionMessage = "Exception thrown by unit test";

    @Before
    public void before() throws WrongUsageException {
        final String fromPattern = "Main";
        final String toArgument = "Renamed";
        args = new String[]{"--from", fromPattern, "--to", toArgument};

        config = new Configuration();
        config.setFrom(fromPattern);
        config.setTo(toArgument);

        parser = mock(ArgumentParser.class);
        when(parser.parse(any(String[].class))).thenReturn(config);
        Main.setArgumentParser(parser);

        fileNamePatternFinder = mock(FilePathFinder.class);
        Main.setFileNamePatternFinder(fileNamePatternFinder);

        fileContentFinder = mock(FilePathFinder.class);
        Main.setFileContentFinder(fileContentFinder);
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
        when(parser.parse(any(String[].class))).thenThrow(new WrongUsageException("Exception thrown by unit test"));

        Main.main(args);
    }

    @Test
    public void main_DirectoryfinderThrowsException_HandlesException() throws IOException {
        fileNamePatternFinder = mock(FilePathFinder.class);
        when(fileNamePatternFinder.find(any())).thenThrow(new IOException(exceptionMessage));

        Main.main(args);
    }

    @Test
    public void main_DirectoryfinderReturnsFiles_RenamesEachFileToToPattern() throws IOException {
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
}
