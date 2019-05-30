package com.github.wonderbird.RenameProject.Logic;

import com.github.wonderbird.RenameProject.FileSystemAccess.Implementation.FileContentFinderImpl;
import com.github.wonderbird.RenameProject.FileSystemAccess.Implementation.FileNamePatternFinderImpl;
import com.github.wonderbird.RenameProject.FileSystemAccess.Implementation.FileSystemMethodsImpl;
import com.github.wonderbird.RenameProject.FileSystemAccess.Interfaces.FilePathFinder;
import com.github.wonderbird.RenameProject.FileSystemAccess.Interfaces.FileSystemMethods;
import com.github.wonderbird.RenameProject.Models.Configuration;
import com.github.wonderbird.RenameProject.Models.DefaultExclusionPatterns;
import com.github.wonderbird.RenameProject.Models.RenameFromToPair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class RenameProjectManagerImpl implements RenameProjectManager
{
   private final Configuration config = Configuration.getConfiguration();

   private FilePathFinder fileContentFinder = new FileContentFinderImpl();

   private FilePathFinder fileNamePatternFinder = new FileNamePatternFinderImpl();

   private FileSystemMethods fileSystemMethods = new FileSystemMethodsImpl();

   private Logger logger = LoggerFactory.getLogger(RenameProjectManagerImpl.class);

   public void renameProject() throws IOException
   {
      for(final RenameFromToPair fromToPair : config.getFromToPairs())
      {
         logger.info("Renaming from '{}' to '{}' ...", fromToPair.getFrom(), fromToPair.getTo());

         final List<Path> affectedPaths = findPathsToRename(fromToPair);

         renameFilesAndDirectories(affectedPaths, fromToPair);

         replaceFileContents(fromToPair);
      }
   }

   private List<Path> findPathsToRename(final RenameFromToPair aFromToPair) throws IOException
   {
      final String from = aFromToPair.getFrom();
      final String filePattern = "*" + from + "*";
      return fileNamePatternFinder.find(config.getStartDir(), filePattern, DefaultExclusionPatterns.DOT_GIT_FOLDERS);
   }

   private void renameFilesAndDirectories(final List<Path> aAffectedPaths, final RenameFromToPair aFromToPair)
      throws IOException
   {

      for(final Path sourcePath : aAffectedPaths)
      {
         final String targetPathString = replaceLastPathSibling(sourcePath, aFromToPair);
         final Path targetPath = Paths.get(targetPathString);

         logger.info("{} -> {}", sourcePath.toString(), targetPath.toString());

         fileSystemMethods.move(sourcePath, targetPath, REPLACE_EXISTING);
      }
   }

   private String replaceLastPathSibling(final Path aSourcePath, final RenameFromToPair aFromToPair)
   {
      final String lastSibling = aSourcePath.getFileName().toString();
      final String lastSiblingWithReplacement = lastSibling.replaceAll(aFromToPair.getFrom(), aFromToPair.getTo());
      final Path result = aSourcePath.getParent().resolve(lastSiblingWithReplacement);

      return result.toString();
   }

   private void replaceFileContents(final RenameFromToPair aFromToPair) throws IOException
   {
      final List<Path> affectedPaths =
         fileContentFinder.find(config.getStartDir(), aFromToPair.getFrom(), DefaultExclusionPatterns.DOT_GIT_FOLDERS);

      for(final Path path : affectedPaths)
      {
         logger.info("Replace contents: {}", path.toString());

         fileSystemMethods.replaceInFile(path, aFromToPair.getFrom(), aFromToPair.getTo());
      }
   }

   void setFileNamePatternFinder(final FilePathFinder aFinder)
   {
      fileNamePatternFinder = aFinder;
   }

   void setFileContentFinder(final FilePathFinder aFinder)
   {
      fileContentFinder = aFinder;
   }

   void setFileSystemMethods(final FileSystemMethods aFileSystemMethods)
   {
      fileSystemMethods = aFileSystemMethods;
   }

   void setLogger(final Logger aLogger)
   {
      logger = aLogger;
   }
}
