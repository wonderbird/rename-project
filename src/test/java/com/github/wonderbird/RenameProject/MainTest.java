package com.github.wonderbird.RenameProject;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class MainTest
{
   private String[] args;

   private Configuration config;

   private ArgumentParser parser;

   private DirectoryWalker walker;

   private final String exceptionMessage = "Exception thrown by unit test";

   @Before
   public void before() throws WrongUsageException
   {
      final String fromPattern = "frompattern";
      args = new String[] {"--from", fromPattern };

      config = new Configuration();
      config.setFromPattern(fromPattern);

      parser = mock(ArgumentParser.class);
      when(parser.parse(any(String[].class))).thenReturn(config);
      Main.setArgumentParser(parser);

      walker = mock(DirectoryWalker.class);
      Main.setDirectoryWalker(walker);
   }

   @Test
   public void main_ArgumentsGiven_InvokesArgumentParserWithAllArguments() throws WrongUsageException
   {
      Main.main(args);

      verify(parser).parse(args);
   }

   @Test
   public void main_FromArgumentGiven_SearchesForFilenamesMatchingFrom() throws IOException
   {
      Main.main(args);

      verify(walker).findByName(config.getFromPattern());
   }

   @Test
   public void main_ArgumentParserThrowsUsageException_HandlesException() throws WrongUsageException
   {
      parser = mock(ArgumentParser.class);
      when(parser.parse(any(String[].class))).thenThrow(new WrongUsageException("Exception thrown by unit test"));

      Main.main(args);
   }

   @Test
   public void main_DirectoryWalkerThrowsException_HandlesException() throws IOException
   {
      walker = mock(DirectoryWalker.class);
      when(walker.findByName(anyString())).thenThrow(new IOException(exceptionMessage));

      Main.main(args);
   }
}
