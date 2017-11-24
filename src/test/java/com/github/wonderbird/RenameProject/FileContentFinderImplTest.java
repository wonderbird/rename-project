package com.github.wonderbird.RenameProject;

import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.Assert.*;

public class FileContentFinderImplTest {
    @Test
    public void find_SearchStringMatchesSingleFileInCurrentDir_ReturnsMatchedFile() throws Exception {
        String searchString = "FileContentFinderImpl";

        FileContentFinderImpl finder = new FileContentFinderImpl();
        List<Path> paths = finder.find(searchString);

        Path expected = Paths.get("src", "main", "java", "com", "github", "wonderbird", "RenameProject", searchString + ".java").normalize().toAbsolutePath();
        assertTrue(String.format("The file '%s' should be found", expected.toString()), paths.contains(expected));
        assertEquals("Unexpected number of files returned", 1, paths.size());
    }

}