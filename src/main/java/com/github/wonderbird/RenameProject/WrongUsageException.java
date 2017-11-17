package com.github.wonderbird.RenameProject;

class WrongUsageException extends Throwable
{
   WrongUsageException(final String aUsageHelpMessage)
   {
      super(aUsageHelpMessage);
   }
}
