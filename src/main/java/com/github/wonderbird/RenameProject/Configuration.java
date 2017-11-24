package com.github.wonderbird.RenameProject;

class Configuration
{
   private String from;
   private String to;

   String getFrom()
   {
      return from;
   }

   String getTo() {
      return to;
   }

   void setFrom(final String aFromPattern)
   {
      from = aFromPattern;
   }

   void setTo(String to) {
      this.to = to;
   }
}
