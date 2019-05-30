package com.github.wonderbird.RenameProject.FileSystemAccess.Implementation;

import com.github.wonderbird.RenameProject.FileSystemAccess.Interfaces.FilePathFinder;
import com.github.wonderbird.RenameProject.FileSystemAccess.Interfaces.FilePathVisitorWithResult;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

public class FileNamePatternFinderImpl implements FilePathFinder
{
   private final FilePathVisitorWithResult visitorMock;

   public FileNamePatternFinderImpl()
   {
      this(null);
   }

   /**
    * Constructor for unit tests allowing to set a visitor mock.
    * @param aVisitorMock Mock for the FilePathVisitorWithResult used in the {@link #find(String, String, String...)}
    * method.
    */
   public FileNamePatternFinderImpl(final FilePathVisitorWithResult aVisitorMock)
   {
      visitorMock = aVisitorMock;
   }

   /**
    * Find files and directories matching the pattern.
    * @param aStartDirectory is the directory below which all occurrences of aPattern will be searched.
    * @param aFileNamePattern describes a part of the file name or directory name to be found.
    * @param aExclusions optional list of patterns listing files and directories to exclude from the result.
    * @return List of all paths beneath the current directory which match the pattern and don't match the exclusions.
    * @see <a href="https://docs.oracle.com/javase/tutorial/displayCode.html?code=https://docs.oracle.com/javase/tutorial/essential/io/examples/Find.java">Example
    * Code at Oracle</a>
    */
   @Override
   public List<Path> find(final String aStartDirectory, final String aFileNamePattern, final String... aExclusions)
      throws IOException
   {
      final FilePathVisitorWithResult visitor;
      if(visitorMock == null)
      {
         visitor = new FileNameMatchingVisitorImpl(aFileNamePattern, aExclusions);
      }
      else
      {
         visitor = visitorMock;
      }

      Files.walkFileTree(Paths.get(aStartDirectory), visitor);

      final List<Path> result = visitor.getResult();

      sortDepthFirst(result);
      removeStartDirFromPaths(result, aStartDirectory);

      return result;
   }

   private void sortDepthFirst(final List<Path> result)
   {
      Collections.sort(result);
      Collections.reverse(result);
   }

   private void removeStartDirFromPaths(final List<Path> aPaths, final String aStartDirectory)
   {
      final Path startDirPath = Paths.get(aStartDirectory).toAbsolutePath();
      aPaths.remove(startDirPath);
   }
}
