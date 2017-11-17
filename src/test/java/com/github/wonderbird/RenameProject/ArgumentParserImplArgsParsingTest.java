package com.github.wonderbird.RenameProject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class ArgumentParserImplArgsParsingTest
{
   @Parameterized.Parameters(name="{index}: parse(\"--from {0}\")")
   public static Collection<Object[]> data()
   {
      return Arrays.asList(new Object[][] {
         {"frompattern", new String[]{"--from", "frompattern"}},
         {"otherfrompattern", new String[]{"--from", "otherfrompattern"}},
      });
   }

   @Parameterized.Parameter
   public String expectedFromPattern;

   @Parameterized.Parameter(1)
   public String[] args;

   @Test
   public void parse_FromArgumentGiven_ThenSetsFromPatternInConfig() throws WrongUsageException
   {
      ArgumentParser parser = new ArgumentParserImpl();
      Configuration config = parser.parse(args);

      assertEquals(expectedFromPattern, config.getFromPattern());
   }
}