package com.github.wonderbird.RenameProject.FileSystemAccess;

import com.github.wonderbird.RenameProject.Configuration;
import com.github.wonderbird.RenameProject.FileSystemAccess.Implementation.FileContentFinderImpl;
import org.junit.After;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FileContentFinderImplTest {
    @After
    public void after() {
        Configuration.getConfiguration().reset();
    }

    @Test
    public void find_SearchStringMatchesSingleFileInCurrentDir_ReturnsMatchedFile() throws Exception {
        searchWithGivenConfiguration();
    }

    @Test
    public void find_SearchStringIsAtBufferBoundary_ReturnsMatchedFile() throws IOException {
        Configuration.getConfiguration().setReadBufferSize(32);
        searchWithGivenConfiguration();
    }

    private void searchWithGivenConfiguration() throws IOException {
        String searchString = "Unique \"SearchString\" For Test Purpose";

        FileContentFinderImpl finder = new FileContentFinderImpl();
        List<Path> paths = finder.find(searchString);

        List<Path> expected = Arrays.asList(
                Paths.get("src", "test", "resources", "fileWithUniqueSearchString.txt").normalize().toAbsolutePath(),
                Paths.get("target", "test-classes", "fileWithUniqueSearchString.txt").normalize().toAbsolutePath(),
                Paths.get("target", "test-classes", "com", "github", "wonderbird", "RenameProject", "FileSystemAccess", "FileContentFinderImplTest.class").normalize().toAbsolutePath()
        );

        assertTrue("Find result does not contain all expected paths", paths.containsAll(expected));
        assertTrue("Find result contains other paths than expected", expected.containsAll(paths));
        assertEquals("Number of files found differs from expected number of files", paths.size(), expected.size());
    }
}