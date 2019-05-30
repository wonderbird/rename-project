package com.github.wonderbird.RenameProject.FileSystemAccess;

import com.github.wonderbird.RenameProject.FileSystemAccess.Implementation.FileSystemMethodsImpl;
import com.github.wonderbird.RenameProject.Models.Configuration;
import org.junit.After;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class FileSystemMethodsImplTest
{
   @After
   public void after()
   {
      Configuration.getConfiguration().reset();
   }

   @Test
   public void move_SourceArgumentIsFoundInResources_RenamesSourceToTarget() throws IOException
   {
      final Path sourcePath = Paths.get("target", "test-classes", "fileToBeRenamed.txt");
      final Path targetPath = sourcePath.resolveSibling("targetfile.txt");

      final FileSystemMethodsImpl fileSystemMethods = new FileSystemMethodsImpl();
      fileSystemMethods.move(sourcePath, targetPath, REPLACE_EXISTING);

      final File sourceFile = new File(sourcePath.toString());
      assertFalse("Source file still exists after moving", sourceFile.exists());

      final File targetFile = new File(targetPath.toString());
      assertTrue("Target file does not exist after moving", targetFile.exists());

      fileSystemMethods.move(targetPath, sourcePath, REPLACE_EXISTING);

      assertTrue("Source file does not exist after moving back", sourceFile.exists());
      assertFalse("Target file still exists after moving back", targetFile.exists());
   }

   @Test
   public void replaceInFile_FromArgumentIsFoundInFile_ReplacesFromByTo() throws IOException
   {
      executeReplaceInFileTest();
   }

   @Test
   public void replaceInFile_FromArgumentAtBufferBoundary_ReplacesFromByTo() throws IOException
   {
      Configuration.getConfiguration().setReadBufferSize(9);
      executeReplaceInFileTest();
   }

   private void executeReplaceInFileTest() throws IOException
   {
      final Path path = Paths.get("target", "test-classes", "fileContentToBeReplaced.txt");

      final FileSystemMethodsImpl fileSystemMethods = new FileSystemMethodsImpl();
      fileSystemMethods.replaceInFile(path, "FROM", "TO");

      final boolean fileContentMatchesTo;
      try(final BufferedReader reader = Files.newBufferedReader(path))
      {
         final String line = reader.readLine();
         fileContentMatchesTo = line.matches(".*TO.*");
      }

      fileSystemMethods.replaceInFile(path, "TO", "FROM");

      final boolean fileContentMatchesFrom;
      try(final BufferedReader reader = Files.newBufferedReader(path))
      {
         final String line = reader.readLine();
         fileContentMatchesFrom = line.matches(".*FROM.*");
      }

      assertTrue("'FROM' should be replaced by 'TO' in '" + path.toString() + "'", fileContentMatchesTo);
      assertTrue("Changes should be reverted - 'TO' should be replaced by 'FROM' in '" + path.toString() + "'",
                 fileContentMatchesFrom);
   }
}