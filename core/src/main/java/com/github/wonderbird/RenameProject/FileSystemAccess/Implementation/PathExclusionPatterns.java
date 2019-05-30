package com.github.wonderbird.RenameProject.FileSystemAccess.Implementation;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PathExclusionPatterns
{
   private final List<Pattern> exclusionPatterns = new ArrayList<>();

   public PathExclusionPatterns(final String[] aExclusions)
   {
      for(final String exclusionPattern : aExclusions)
      {
         exclusionPatterns.add(Pattern.compile(exclusionPattern));
      }
   }

   public boolean isExcluded(final Path aNormalizedPath)
   {
      boolean isExcluded = false;
      int index = 0;
      while(!isExcluded && index < exclusionPatterns.size())
      {
         final Pattern exclusionPattern = exclusionPatterns.get(index);
         final Matcher matcher = exclusionPattern.matcher(aNormalizedPath.toString());
         isExcluded = matcher.matches();
         index += 1;
      }
      return isExcluded;
   }
}
