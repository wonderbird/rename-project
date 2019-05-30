package com.github.wonderbird.RenameProject.FileSystemAccess.Implementation;

import com.github.wonderbird.RenameProject.FileSystemAccess.Interfaces.FilePathFinder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class FileContentFinderImpl implements FilePathFinder
{

   /**
    * Find all files containing the search string.
    * @param aStartDirectory is the directory below which the pattern shall be searched.
    * @param aPattern is the word which should be searched in all files.
    * @param aExclusions optional list of patterns listing files and directories to exclude from the result.
    * @return List of all files beneath the current directory which contain searchString.
    */
   public List<Path> find(final String aStartDirectory, final String aPattern, final String... aExclusions)
      throws IOException
   {
      final FileContentMatchingVisitorImpl visitor = new FileContentMatchingVisitorImpl(aPattern, aExclusions);

      Files.walkFileTree(Paths.get(aStartDirectory), visitor);

      return visitor.getResult();
   }
}
