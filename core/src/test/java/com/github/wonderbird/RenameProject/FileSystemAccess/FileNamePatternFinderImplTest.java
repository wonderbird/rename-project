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

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FileNamePatternFinderImplTest
{
   @Rule
   public ExpectedException thrown = ExpectedException.none();

   @Test
   public void find_PatternMatchesSingleDirInCurrentDir_ReturnsMatchedFile() throws IOException
   {
      final FilePathFinder finder = new FileNamePatternFinderImpl();
      final String pattern = "src";

      final List<Path> paths = finder.find(".", pattern);

      final Path expected = Paths.get(pattern).normalize().toAbsolutePath();
      assertTrue(String.format("The directory '%s' should be found", expected.toString()), paths.contains(expected));
      assertEquals("Unexpected number of files and directories returned", 1, paths.size());
   }

   @Test
   public void find_PatternMatchesThreeDirectoriesInCurrentDir_ReturnsMatchedDirectories() throws IOException
   {
      final FilePathFinder finder = new FileNamePatternFinderImpl();
      final String pattern = "FileSystemAccess";

      final List<Path> paths = finder.find(".", pattern);

      final List<Path> expectedPaths = Arrays.asList(
         Paths.get("target", "test-classes", "com", "github", "wonderbird", "RenameProject", "FileSystemAccess")
              .toAbsolutePath(),
         Paths.get("target", "classes", "com", "github", "wonderbird", "RenameProject", "FileSystemAccess")
              .toAbsolutePath(),
         Paths.get("src", "test", "java", "com", "github", "wonderbird", "RenameProject", "FileSystemAccess")
              .toAbsolutePath(),
         Paths.get("src", "main", "java", "com", "github", "wonderbird", "RenameProject", "FileSystemAccess")
              .toAbsolutePath());

      for(final Path expected : expectedPaths)
      {
         assertTrue(String.format("The directory '%s' should be found", expected.toString()),
                    paths.stream().anyMatch(actual -> actual.compareTo(expected) == 0));
      }
      assertEquals("Too many entries returned", expectedPaths.size(), paths.size());
   }

   @Test
   public void find_PatternMatchesFourFilesInCurrentDir_ReturnsMatchedFiles() throws IOException
   {
      final FilePathFinder finder = new FileNamePatternFinderImpl();
      final String pattern = "ArgumentParser*.java";

      final List<Path> paths = finder.find(".", pattern);

      final List<Path> expectedPaths = Arrays.asList(
         Paths.get("src", "main", "java", "com", "github", "wonderbird", "RenameProject", "Logic",
                   "ArgumentParser.java").toAbsolutePath(),
         Paths.get("src", "main", "java", "com", "github", "wonderbird", "RenameProject", "Logic",
                   "ArgumentParserImpl.java").toAbsolutePath(),
         Paths.get("src", "test", "java", "com", "github", "wonderbird", "RenameProject", "Logic",
                   "ArgumentParserImplArgsParsingTest.java").toAbsolutePath(),
         Paths.get("src", "test", "java", "com", "github", "wonderbird", "RenameProject", "Logic",
                   "ArgumentParserImplGeneralTest.java").toAbsolutePath());

      for(final Path expected : expectedPaths)
      {
         assertTrue(String.format("The file '%s' should be found", expected.toString()),
                    paths.stream().anyMatch(actual -> actual.compareTo(expected) == 0));
      }
      assertEquals("Too many entries returned", 4, paths.size());
   }

   @Test
   public void find_FileVisitorThrowsIOException_throwsIOException() throws IOException
   {
      thrown.expect(IOException.class);

      final FilePathVisitorWithResult visitor = mock(FilePathVisitorWithResult.class);
      when(visitor.preVisitDirectory(any(), any())).thenReturn(FileVisitResult.CONTINUE);
      when(visitor.postVisitDirectory(any(), any())).thenReturn(FileVisitResult.CONTINUE);
      when(visitor.visitFileFailed(any(), any())).thenReturn(FileVisitResult.CONTINUE);
      when(visitor.visitFile(any(), any())).thenThrow(new IOException("Exception thrown by unit test"));

      final FilePathFinder finder = new FileNamePatternFinderImpl(visitor);
      finder.find(".", "java");
   }

   @Test
   public void find_PatternMatchesFilesAndDirectories_SortsResultDepthFirst() throws IOException
   {
      final FilePathFinder finder = new FileNamePatternFinderImpl();
      final String pattern = "*resources*";

      final List<Path> paths = finder.find(".", pattern);

      final int directoryIndex = paths.indexOf(Paths.get("src", "test", "resources").toAbsolutePath());
      final int fileIndex = paths.indexOf(
         Paths.get("src", "test", "resources", "fileNameContainsParentDirName_resources.txt").toAbsolutePath());

      assertTrue("Sequence of find results is wrong: Deeper paths must be first", fileIndex < directoryIndex);
   }

   /**
    * Bug-fix: If find is called twice, the pattern of the first call is also used in the second call.
    * @throws IOException is thrown by renameProject but not expected in this test.
    */
   @Test
   public void find_CalledTwiceWithDifferentPatterns_AppliesTheSecondPatternForTheSecondCall() throws IOException
   {
      final FilePathFinder finder = new FileNamePatternFinderImpl();

      finder.find(".", "*resources*");
      final List<Path> paths = finder.find(".", "FileNamePatternFinderImplTest\\.java");

      final Path unwantedPath = Paths.get("src", "main", "resources").toAbsolutePath();
      assertFalse("Unwanted path is contained in find results", paths.contains(unwantedPath));

      final Path expectedPath =
         Paths.get("src", "test", "java", "com", "github", "wonderbird", "RenameProject", "FileSystemAccess",
                   "FileNamePatternFinderImplTest.java").toAbsolutePath();
      assertTrue("Expected path is not contained in find results", paths.contains(expectedPath));
   }

   /**
    * Bug-fix: Find must not return the start directory.
    * @throws IOException is thrown by renameProject but not expected in this test.
    */
   @Test
   public void find_PatternMatchesStartDir_RemovesStartDirFromResults() throws IOException
   {
      final FilePathFinder finder = new FileNamePatternFinderImpl();

      final List<Path> paths = finder.find("src", "src");

      final Path unwantedPath = Paths.get("src").toAbsolutePath();
      assertFalse("Start directory should be removed from find results", paths.contains(unwantedPath));
   }

   /**
    * Feature: Ignore ".git" folder recursively.
    * <p>
    * For test purposes we make sure that skipping the 'core' folder is working. This allows to check that some files
    * survived the filtering process. If we'd use a filter addressing the '.git' folder, then we wouldn't be able to
    * check for surviving files.
    * @throws IOException is thrown by renameProject but not expected in this test.
    */
   @Test
   public void find_PatternExcludesCoreDirectory_DoNotIncludeFilesFromCoreInResults() throws IOException
   {
      final FilePathFinder finder = new FileNamePatternFinderImpl();

      final List<Path> paths = finder.find("..", "*pom*", ".*core.*");

      final Path unwantedPath = Paths.get("../core/pom.xml").toAbsolutePath().normalize();
      assertFalse("'core' directory should not be included in find results", paths.contains(unwantedPath));
      assertFalse("at least one entry should be returned", paths.isEmpty());
   }
}
