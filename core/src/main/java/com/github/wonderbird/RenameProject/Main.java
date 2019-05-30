package com.github.wonderbird.RenameProject;

import com.github.wonderbird.RenameProject.Logic.ArgumentParser;
import com.github.wonderbird.RenameProject.Logic.ArgumentParserImpl;
import com.github.wonderbird.RenameProject.Logic.RenameProjectManager;
import com.github.wonderbird.RenameProject.Logic.RenameProjectManagerImpl;
import com.github.wonderbird.RenameProject.Logic.WrongUsageException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class Main
{

   private static final Logger logger = LoggerFactory.getLogger(Main.class);

   private static ArgumentParser argumentParser = new ArgumentParserImpl();

   private static RenameProjectManager renameProjectManager = new RenameProjectManagerImpl();

   public static void main(final String[] args)
   {
      try
      {
         argumentParser.parse(args);

         renameProjectManager.renameProject();
      }
      catch(final WrongUsageException aException)
      {
         System.out.println(aException.getLocalizedMessage());
      }
      catch(final IOException aException)
      {
         logger.error("Error in I/O operation:", aException);
      }
   }

   static void setArgumentParser(final ArgumentParser aArgumentParser)
   {
      argumentParser = aArgumentParser;
   }

   static void setRenameProjectManager(final RenameProjectManager aRenameProjectManager)
   {
      renameProjectManager = aRenameProjectManager;
   }
}
