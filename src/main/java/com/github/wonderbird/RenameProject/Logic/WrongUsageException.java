package com.github.wonderbird.RenameProject.Logic;

public class WrongUsageException extends Throwable
{
   public WrongUsageException(final String aUsageHelpMessage)
   {
      super(aUsageHelpMessage);
   }
}
