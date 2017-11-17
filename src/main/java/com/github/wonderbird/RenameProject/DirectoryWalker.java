package com.github.wonderbird.RenameProject;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public interface DirectoryWalker
{
   List<Path> findByName(String pattern) throws IOException;
}
