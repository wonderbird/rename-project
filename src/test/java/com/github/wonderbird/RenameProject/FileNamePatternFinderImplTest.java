package com.github.wonderbird.RenameProject;

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
        FileNamePatternFinder finder = new FileNamePatternFinderImpl();
        final String pattern = "src";

        final List<Path> paths = finder.find(pattern);

        Path expected = Paths.get(pattern).normalize().toAbsolutePath();
        assertTrue(String.format("The directory '%s' should be found", expected.toString()), paths.contains(expected));
        assertEquals("Unexpected number of files and directories returned", 1, paths.size());
    }

    @Test
    public void find_PatternMatchesTwoDirectoriesInCurrentDir_ReturnsMatchedDirectories() throws IOException {
        FileNamePatternFinder finder = new FileNamePatternFinderImpl();
        final String pattern = "java";

        final List<Path> paths = finder.find(pattern);

        List<Path> expectedPaths = Arrays.asList(
                Paths.get("src", "main", pattern).toAbsolutePath(),
                Paths.get("src", "test", pattern).toAbsolutePath());

        for (Path expected : expectedPaths) {
            assertTrue(String.format("The directory '%s' should be found", expected.toString()), paths.stream().anyMatch(actual -> actual.compareTo(expected) == 0));
        }
        assertEquals("Too many entries returned", 2, paths.size());
    }

    @Test
    public void find_PatternMatchesThreeFilesInCurrentDir_ReturnsMatchedFiles() throws IOException {
        FileNamePatternFinder finder = new FileNamePatternFinderImpl();
        final String pattern = "FileNamePatternFinder*.java";

        final List<Path> paths = finder.find(pattern);

        List<Path> expectedPaths = Arrays.asList(
                Paths.get("src", "main", "java", "com", "github", "wonderbird", "RenameProject", "FileNamePatternFinder.java").toAbsolutePath(),
                Paths.get("src", "main", "java", "com", "github", "wonderbird", "RenameProject", "FileNamePatternFinderImpl.java").toAbsolutePath(),
                Paths.get("src", "test", "java", "com", "github", "wonderbird", "RenameProject", "FileNamePatternFinderImplTest.java").toAbsolutePath());

        for (Path expected : expectedPaths) {
            assertTrue(String.format("The file '%s' should be found", expected.toString()), paths.stream().anyMatch(actual -> actual.compareTo(expected) == 0));
        }
        assertEquals("Too many entries returned", 3, paths.size());
    }

    @Test
    public void find_FileVisitorThrowsIOException_throwsIOException() throws IOException {
        thrown.expect(IOException.class);

        FileNameMatchingVisitor visitor = mock(FileNameMatchingVisitor.class);
        when(visitor.preVisitDirectory(any(), any())).thenReturn(FileVisitResult.CONTINUE);
        when(visitor.visitFile(any(), any())).thenThrow(new IOException("Exception thrown by unit test"));

        FileNamePatternFinder finder = new FileNamePatternFinderImpl(visitor);
        finder.find("java");
    }
}
