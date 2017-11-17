package com.github.wonderbird.RenameProject;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class Main
{
   private static DirectoryWalker directoryWalker = new DirectoryWalkerImpl();

   private static ArgumentParser argumentParser = new ArgumentParserImpl();

   public static void main(String[] args)
   {
      try
      {
         Configuration config = argumentParser.parse(args);
         List<Path> affectedPaths = directoryWalker.findByName(config.getFromPattern());
      }
      catch(WrongUsageException aException)
      {
         System.out.println(aException.getLocalizedMessage());
      }
      catch(IOException aE)
      {
         aE.printStackTrace();
      }
   }

   static void setArgumentParser(final ArgumentParser aArgumentParser)
   {
      argumentParser = aArgumentParser;
   }

   static void setDirectoryWalker(final DirectoryWalker aWalker)
   {
      directoryWalker = aWalker;
   }
}
