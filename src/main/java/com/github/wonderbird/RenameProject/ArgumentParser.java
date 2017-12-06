package com.github.wonderbird.RenameProject;

public interface ArgumentParser
{
   void parse(String[] aArgs) throws WrongUsageException;
}
