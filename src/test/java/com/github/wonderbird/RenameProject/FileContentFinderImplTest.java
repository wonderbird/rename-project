package com.github.wonderbird.RenameProject;

import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.Assert.*;

public class FileContentFinderImplTest {
    @Test
    public void find_SearchStringMatchesSingleFileInCurrentDir_ReturnsMatchedFile() throws Exception {
        String searchString = "UniqueSearchStringForTestPurpose";

        FileContentFinderImpl finder = new FileContentFinderImpl();
        List<Path> paths = finder.find(searchString);

        Path expected = Paths.get("src", "test", "java", "com", "github", "wonderbird", "RenameProject", "FileContentFinderImplTest.java").normalize().toAbsolutePath();
        assertTrue(String.format("The file '%s' should be found", expected.toString()), paths.contains(expected));

        // Usually the .class file is also returned. Thus we expect 2 matching file paths.
        assertEquals("Unexpected number of files returned", 2, paths.size());
    }

}