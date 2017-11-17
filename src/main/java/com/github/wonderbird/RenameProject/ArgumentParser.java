package com.github.wonderbird.RenameProject;

public interface ArgumentParser
{
   Configuration parse(String[] aArgs) throws WrongUsageException;
}
