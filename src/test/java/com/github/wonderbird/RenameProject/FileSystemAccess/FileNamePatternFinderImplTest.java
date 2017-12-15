package com.github.wonderbird.RenameProject.FileSystemAccess;

import com.github.wonderbird.RenameProject.FileSystemAccess.Implementation.FileNamePatternFinderImpl;
import com.github.wonderbird.RenameProject.FileSystemAccess.Interfaces.FilePathFinder;
import com.github.wonderbird.RenameProject.FileSystemAccess.Interfaces.FilePathVisitorWithResult;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FileNamePatternFinderImplTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void find_PatternMatchesSingleDirInCurrentDir_ReturnsMatchedFile() throws IOException {
        FilePathFinder finder = new FileNamePatternFinderImpl();
        final String pattern = "src";

        final List<Path> paths = finder.find(".", pattern);

        Path expected = Paths.get(pattern).normalize().toAbsolutePath();
        assertTrue(String.format("The directory '%s' should be found", expected.toString()), paths.contains(expected));
        assertEquals("Unexpected number of files and directories returned", 1, paths.size());
    }

    @Test
    public void find_PatternMatchesTwoDirectoriesInCurrentDir_ReturnsMatchedDirectories() throws IOException {
        FilePathFinder finder = new FileNamePatternFinderImpl();
        final String pattern = "java";

        final List<Path> paths = finder.find(".", pattern);

        List<Path> expectedPaths = Arrays.asList(
                Paths.get("src", "main", pattern).toAbsolutePath(),
                Paths.get("src", "test", pattern).toAbsolutePath());

        for (Path expected : expectedPaths) {
            assertTrue(String.format("The directory '%s' should be found", expected.toString()), paths.stream().anyMatch(actual -> actual.compareTo(expected) == 0));
        }
        assertEquals("Too many entries returned", 2, paths.size());
    }

    @Test
    public void find_PatternMatchesFourFilesInCurrentDir_ReturnsMatchedFiles() throws IOException {
        FilePathFinder finder = new FileNamePatternFinderImpl();
        final String pattern = "ArgumentParser*.java";

        final List<Path> paths = finder.find(".", pattern);

        List<Path> expectedPaths = Arrays.asList(
                Paths.get("src", "main", "java", "com", "github", "wonderbird", "RenameProject", "Logic", "ArgumentParser.java").toAbsolutePath(),
                Paths.get("src", "main", "java", "com", "github", "wonderbird", "RenameProject", "Logic", "ArgumentParserImpl.java").toAbsolutePath(),
                Paths.get("src", "test", "java", "com", "github", "wonderbird", "RenameProject", "Logic", "ArgumentParserImplArgsParsingTest.java").toAbsolutePath(),
                Paths.get("src", "test", "java", "com", "github", "wonderbird", "RenameProject", "Logic", "ArgumentParserImplGeneralTest.java").toAbsolutePath());

        for (Path expected : expectedPaths) {
            assertTrue(String.format("The file '%s' should be found", expected.toString()), paths.stream().anyMatch(actual -> actual.compareTo(expected) == 0));
        }
        assertEquals("Too many entries returned", 4, paths.size());
    }

    @Test
    public void find_FileVisitorThrowsIOException_throwsIOException() throws IOException {
        thrown.expect(IOException.class);

        FilePathVisitorWithResult visitor = mock(FilePathVisitorWithResult.class);
        when(visitor.preVisitDirectory(any(), any())).thenReturn(FileVisitResult.CONTINUE);
        when(visitor.postVisitDirectory(any(), any())).thenReturn(FileVisitResult.CONTINUE);
        when(visitor.visitFileFailed(any(), any())).thenReturn(FileVisitResult.CONTINUE);
        when(visitor.visitFile(any(), any())).thenThrow(new IOException("Exception thrown by unit test"));

        FilePathFinder finder = new FileNamePatternFinderImpl(visitor);
        finder.find(".", "java");
    }

    @Test
    public void find_PatternMatchesFilesAndDirectories_SortsResultDepthFirst() throws IOException {
        FilePathFinder finder = new FileNamePatternFinderImpl();
        final String pattern = "*resources*";

        final List<Path> paths = finder.find(".", pattern);

        int directoryIndex = paths.indexOf(Paths.get("src", "test", "resources").toAbsolutePath());
        int fileIndex = paths.indexOf(Paths.get("src", "test", "resources", "fileNameContainsParentDirName_resources.txt").toAbsolutePath());

        assertTrue("Sequence of find results is wrong: Deeper paths must be first", fileIndex < directoryIndex);
    }
}
