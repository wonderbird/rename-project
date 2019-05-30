package com.github.wonderbird.RenameProject.FileSystemAccess.Implementation;

import com.github.wonderbird.RenameProject.FileSystemAccess.Interfaces.FileSystemMethods;
import com.github.wonderbird.RenameProject.Models.Configuration;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class FileSystemMethodsImpl implements FileSystemMethods
{
   @Override
   public void move(final Path source, final Path target, final CopyOption copyOption) throws IOException
   {
      Files.move(source, target, copyOption);
   }

   @Override
   public void replaceInFile(final Path affectedFile, final String aFrom, final String aTo) throws IOException
   {
      replaceInFileWithOffset(affectedFile, 0, aFrom, aTo);
      replaceInFileWithOffset(affectedFile, aFrom.length(), aFrom, aTo);
   }

   private void replaceInFileWithOffset(final Path affectedFile, final int aSkipBytes, final String aFrom,
                                        final String aTo) throws IOException
   {
      final int BUFFER_SIZE = Configuration.getConfiguration().getReadBufferSize();

      final InputStream inputStream = Files.newInputStream(affectedFile);
      final Path tempFile = Files.createTempFile("rename-project_", ".tmp");

      try
      {
         try(final InputStreamReader reader = new InputStreamReader(
            inputStream); final BufferedWriter writer = Files.newBufferedWriter(tempFile))
         {
            final char[] buffer = new char[BUFFER_SIZE];
            String bufferString;
            int readBytes;

            if(0 < aSkipBytes && aSkipBytes < BUFFER_SIZE)
            {
               readBytes = reader.read(buffer, 0, aSkipBytes);
               bufferString = new String(buffer, 0, readBytes);
               if(readBytes > 0)
               {
                  writer.write(bufferString);
               }
            }

            while((readBytes = reader.read(buffer, 0, BUFFER_SIZE)) > 0)
            {
               bufferString = new String(buffer, 0, readBytes);
               final String bufferWithReplacement = bufferString.replace(aFrom, aTo);
               writer.write(bufferWithReplacement);
            }
         }

         Files.copy(tempFile, affectedFile, REPLACE_EXISTING);
      }
      finally
      {
         Files.delete(tempFile);
      }
   }
}
