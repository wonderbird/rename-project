package com.github.wonderbird.RenameProject.FileSystemAccess.Interfaces;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public interface FilePathFinder
{
   List<Path> find(final String aStartDirectory, final String aPattern, final String... aExclusions) throws IOException;
}
