package com.github.wonderbird.RenameProject.FileSystemAccess;

import com.github.wonderbird.RenameProject.FileSystemAccess.Implementation.FileContentFinderImpl;
import com.github.wonderbird.RenameProject.FileSystemAccess.Interfaces.FilePathFinder;
import com.github.wonderbird.RenameProject.Models.Configuration;
import org.junit.After;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class FileContentFinderImplTest
{
   private final String uniqueSearchString = "Unique \"SearchString\" For Test Purpose";

   @After
   public void after()
   {
      Configuration.getConfiguration().reset();
   }

   @Test
   public void find_SearchStringMatchesSingleFileInCurrentDir_ReturnsMatchedFile() throws Exception
   {
      searchWithGivenConfiguration();
   }

   @Test
   public void find_SearchStringIsAtBufferBoundary_ReturnsMatchedFile() throws IOException
   {
      Configuration.getConfiguration().setReadBufferSize(32);
      searchWithGivenConfiguration();
   }

   private void searchWithGivenConfiguration() throws IOException
   {
      final FileContentFinderImpl finder = new FileContentFinderImpl();
      final String startDirectory = Paths.get("src", "test", "resources").toString();
      final List<Path> paths = finder.find(startDirectory, uniqueSearchString);

      final List<Path> expected = Collections.singletonList(
         Paths.get("src", "test", "resources", "fileWithUniqueSearchString.txt").normalize().toAbsolutePath());

      assertTrue("Find result does not contain all expected paths", paths.containsAll(expected));
      assertTrue("Find result contains other paths than expected", expected.containsAll(paths));
      assertEquals("Number of files found differs from expected number of files", paths.size(), expected.size());
   }

   /**
    * Bug-fix: If find is called twice, the pattern of the first call is also used in the second call.
    * @throws IOException is thrown by renameProject but not expected in this test.
    */
   @Test
   public void find_CalledTwiceWithDifferentPatterns_AppliesTheSecondPatternForTheSecondCall() throws IOException
   {
      final String secondSearchString = uniqueSearchString + "_WillNotBeFound";

      final FilePathFinder finder = new FileContentFinderImpl();

      final String startDirectory = Paths.get("src", "test", "resources").toString();
      finder.find(startDirectory, uniqueSearchString);
      final List<Path> paths = finder.find(startDirectory, secondSearchString);

      final Path unwantedPath = Paths.get(startDirectory, "fileWithUniqueSearchString.txt").toAbsolutePath();
      assertFalse("File containing the first search string should not be returned", paths.contains(unwantedPath));
      assertEquals("Applying the second search string should return 0 paths", 0, paths.size());
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
   public void find_PatternExcludesDotGitDirectory_DoNotIncludeFilesFromDotGitInResults() throws IOException
   {
      final FilePathFinder finder = new FileContentFinderImpl();

      final String startDirectory = Paths.get("src", "test", "resources").toString();
      final String searchForFile = "file";
      final List<Path> paths = finder.find(startDirectory, searchForFile, ".*Renamed.*");

      final Path unwantedPath = Paths.get(startDirectory, "fileToBeRenamed.txt").toAbsolutePath();
      assertFalse("'fileToBeRenamed.txt' should not be included in find results", paths.contains(unwantedPath));
      assertFalse("at least one entry should be returned", paths.isEmpty());
   }
}