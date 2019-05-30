package com.github.wonderbird.RenameProject.Models;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class DefaultExclusionPatternsDotGitFoldersTest
{
   private static final boolean MUST_MATCH = true;

   private static final boolean MUST_NOT_MATCH = false;

   @Parameter
   public boolean isMatchExpected;

   @Parameter(1)
   public String input;

   @Test
   public void test()
   {
      final boolean result = Pattern.matches(DefaultExclusionPatterns.DOT_GIT_FOLDERS, input);

      final String message =
         String.format("DOT_GIT_FOLDERS %s '%s'", isMatchExpected ? "must match" : "must not match", input);
      assertEquals(message, isMatchExpected, result);
   }

   @Parameters(name = "{index}: {0} for \"{1}\"")
   public static Collection<Object[]> data()
   {
      return Arrays.asList(new Object[][]{{MUST_MATCH, ".git"}, {MUST_MATCH, ".git"}, {MUST_MATCH, ".git/matches"},
                                          {MUST_MATCH, "something/.git/else matches"}, {MUST_MATCH, ".git/matches"},
                                          {MUST_MATCH, "something/.git"}, {MUST_MATCH, "something/.git/matches"},
                                          {MUST_NOT_MATCH, "something/.git-doesnotmatch"},
                                          {MUST_NOT_MATCH, "something/.git-doesnotmatch/still"},
                                          {MUST_NOT_MATCH, "something/.git doesnotmatch"},
                                          {MUST_NOT_MATCH, ".git-nomatch"}});
   }
}
