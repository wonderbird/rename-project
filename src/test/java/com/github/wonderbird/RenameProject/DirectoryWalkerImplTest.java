package com.github.wonderbird.RenameProject;

import org.junit.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DirectoryWalkerImplTest
{
   @Test
   public void findByName_PatternMatchesSingleDirInCurrentDir_ReturnsMatchedFile() throws IOException
   {
      DirectoryWalker walker = new DirectoryWalkerImpl();
      final String pattern = "src";

      final List<Path> paths = walker.findByName(pattern);

      Path expected = Paths.get("main", pattern);
      assertTrue(String.format("The directory '%s' should be found", expected.toAbsolutePath().toString()), paths.contains(expected));
      assertEquals("Too many directories returned", 1, paths.size());
   }

   @Test
   public void findByName_PatternMatchesTwoDirectoriesInCurrentDir_ReturnsMatchedDirectories() throws IOException
   {
      DirectoryWalker walker = new DirectoryWalkerImpl();
      final String pattern = "java";

      final List<Path> paths = walker.findByName(pattern);

      List<Path> expectedPaths = Arrays.asList(
         Paths.get("main", "src", pattern),
         Paths.get("main", "test", pattern));

      for(Path expected : expectedPaths)
      {
         assertTrue(String.format("The directory '%s' should be found", expected.toAbsolutePath().toString()), paths.contains(expected));
      }
      assertEquals("Too many directories returned", 2, paths.size());
   }
}
