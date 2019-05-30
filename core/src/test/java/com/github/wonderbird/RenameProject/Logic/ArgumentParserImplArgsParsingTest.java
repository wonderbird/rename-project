package com.github.wonderbird.RenameProject.Logic;

import com.github.wonderbird.RenameProject.Models.Configuration;
import com.github.wonderbird.RenameProject.Models.RenameFromToPair;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class ArgumentParserImplArgsParsingTest
{
   @Parameter
   public String expectedFromPattern;

   @Parameter(1)
   public String expectedToArgument;

   @Parameter(2)
   public String expectedStartDir;

   @Parameter(3)
   public String[] args;

   @Before
   public void before()
   {
      Configuration.getConfiguration().reset();
   }

   @Test
   public void parse_GivenArguments_SetsExpectedConfigProperties() throws WrongUsageException
   {
      final ArgumentParser parser = new ArgumentParserImpl();
      parser.parse(args);

      final Configuration config = Configuration.getConfiguration();
      final List<RenameFromToPair> fromToPairs = config.getFromToPairs();
      assertEquals("Invalid number of from/to pairs", 1, fromToPairs.size());
      final RenameFromToPair pair = fromToPairs.get(0);
      assertEquals("Invalid from", expectedFromPattern, pair.getFrom());
      assertEquals("Invalid to", expectedToArgument, pair.getTo());
      assertEquals("Invalid start directory", expectedStartDir, config.getStartDir());
   }

   /**
    * Expected values and args array for the test cases.
    * <p>
    * The format of the elements in this list can be derived from the sequence of @Parameterized.Parameter members
    * defined below.
    */
   @Parameters(name = "{index}: parse(\"--from {0}\")")
   public static Collection<Object[]> data()
   {
      return Arrays.asList(new Object[][]{{"frompattern", "toargument", "./src",
                                           new String[]{"--from", "frompattern", "--to", "toargument", "--dir",
                                                        "./src"}}, {"otherfrompattern", "othertoargument", ".",
                                                                    new String[]{"--to", "othertoargument", "--from",
                                                                                 "otherfrompattern"}},});
   }
}