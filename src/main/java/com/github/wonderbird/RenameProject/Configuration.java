package com.github.wonderbird.RenameProject;

class Configuration
{
   private String fromPattern;
   private String toArgument;

   String getFromPattern()
   {
      return fromPattern;
   }

   String getToArgument() {
      return toArgument;
   }

   void setFromPattern(final String aFromPattern)
   {
      fromPattern = aFromPattern;
   }

   void setToArgument(String toArgument) {
      this.toArgument = toArgument;
   }
}
