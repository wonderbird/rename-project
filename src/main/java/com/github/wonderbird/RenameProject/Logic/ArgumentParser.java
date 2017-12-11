package com.github.wonderbird.RenameProject.Logic;

public interface ArgumentParser
{
   void parse(String[] aArgs) throws WrongUsageException;
}
