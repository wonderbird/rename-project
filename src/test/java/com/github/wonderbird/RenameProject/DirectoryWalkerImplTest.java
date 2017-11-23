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

public class DirectoryWalkerImplTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void findByName_PatternMatchesSingleDirInCurrentDir_ReturnsMatchedFile() throws IOException {
        DirectoryWalker walker = new DirectoryWalkerImpl();
        final String pattern = "src";

        final List<Path> paths = walker.findByName(pattern);

        Path expected = Paths.get(pattern).normalize().toAbsolutePath();
        assertTrue(String.format("The directory '%s' should be found", expected.toString()), paths.contains(expected));
        assertEquals("Too many directories returned", 1, paths.size());
    }

    @Test
    public void findByName_PatternMatchesTwoDirectoriesInCurrentDir_ReturnsMatchedDirectories() throws IOException {
        DirectoryWalker walker = new DirectoryWalkerImpl();
        final String pattern = "java";

        final List<Path> paths = walker.findByName(pattern);

        List<Path> expectedPaths = Arrays.asList(
                Paths.get("src", "main", pattern).toAbsolutePath(),
                Paths.get("src", "test", pattern).toAbsolutePath());

        for (Path expected : expectedPaths) {
            assertTrue(String.format("The directory '%s' should be found", expected.toString()), paths.stream().anyMatch(actual -> actual.compareTo(expected) == 0));
        }
        assertEquals("Too many entries returned", 2, paths.size());
    }

    @Test
    public void findByName_PatternMatchesThreeFilesInCurrentDir_ReturnsMatchedFiles() throws IOException {
        DirectoryWalker walker = new DirectoryWalkerImpl();
        final String pattern = "DirectoryWalker*.java";

        final List<Path> paths = walker.findByName(pattern);

        List<Path> expectedPaths = Arrays.asList(
                Paths.get("src", "main", "java", "com", "github", "wonderbird", "RenameProject", "DirectoryWalker.java").toAbsolutePath(),
                Paths.get("src", "main", "java", "com", "github", "wonderbird", "RenameProject", "DirectoryWalkerImpl.java").toAbsolutePath(),
                Paths.get("src", "test", "java", "com", "github", "wonderbird", "RenameProject", "DirectoryWalkerImplTest.java").toAbsolutePath());

        for (Path expected : expectedPaths) {
            assertTrue(String.format("The file '%s' should be found", expected.toString()), paths.stream().anyMatch(actual -> actual.compareTo(expected) == 0));
        }
        assertEquals("Too many entries returned", 3, paths.size());
    }

    @Test
    public void findByName_FileVisitorThrowsIOException_throwsIOException() throws IOException {
        thrown.expect(IOException.class);

        FileNameMatchingVisitor visitor = mock(FileNameMatchingVisitor.class);
        when(visitor.preVisitDirectory(any(), any())).thenReturn(FileVisitResult.CONTINUE);
        when(visitor.visitFile(any(), any())).thenThrow(new IOException("Exception thrown by unit test"));

        DirectoryWalker walker = new DirectoryWalkerImpl(visitor);
        walker.findByName("java");
    }
}
