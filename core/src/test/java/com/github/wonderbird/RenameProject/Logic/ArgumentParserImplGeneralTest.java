package com.github.wonderbird.RenameProject.Logic;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class ArgumentParserImplGeneralTest
{
   @Test
   public void parse_FromArgumentMissing_ThrowsWrongUsageException()
   {
      final ArgumentParserImpl parser = new ArgumentParserImpl();

      final String[] args = {};
      try
      {
         parser.parse(args);
      }
      catch(final WrongUsageException aException)
      {
         assertTrue("Usage information is missing", aException.getLocalizedMessage().contains("usage:"));
      }
   }
}
