package com.github.wonderbird.RenameProject.FileSystemAccess;

import com.github.wonderbird.RenameProject.FileSystemAccess.Implementation.FileNamePatternFinderImpl;
import com.github.wonderbird.RenameProject.FileSystemAccess.Interfaces.FilePathFinder;
import com.github.wonderbird.RenameProject.Models.Configuration;
import com.github.wonderbird.RenameProject.FileSystemAccess.Implementation.FileContentFinderImpl;
import org.junit.After;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class FileContentFinderImplTest {
    private final String uniqueSearchString = "Unique \"SearchString\" For Test Purpose";

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
        FileContentFinderImpl finder = new FileContentFinderImpl();
        String startDirectory = Paths.get("src", "test", "resources").toString();
        List<Path> paths = finder.find(startDirectory, uniqueSearchString);

        List<Path> expected = Collections.singletonList(
                Paths.get("src", "test", "resources", "fileWithUniqueSearchString.txt").normalize().toAbsolutePath()
        );

        assertTrue("Find result does not contain all expected paths", paths.containsAll(expected));
        assertTrue("Find result contains other paths than expected", expected.containsAll(paths));
        assertEquals("Number of files found differs from expected number of files", paths.size(), expected.size());
    }

    /**
     * Bug-fix: If find is called twice, the pattern of the first call is also used in the second call.
     *
     * @throws IOException is thrown by renameProject but not expected in this test.
     */
    @Test
    public void find_CalledTwiceWithDifferentPatterns_AppliesTheSecondPatternForTheSecondCall() throws IOException {
        final String secondSearchString = uniqueSearchString + "_WillNotBeFound";

        FilePathFinder finder = new FileContentFinderImpl();

        String startDirectory = Paths.get("src", "test", "resources").toString();
        finder.find(startDirectory, uniqueSearchString);
        List<Path> paths = finder.find(startDirectory, secondSearchString);

        Path unwantedPath = Paths.get(startDirectory, "fileWithUniqueSearchString.txt").toAbsolutePath();
        assertFalse("File containing the first search string should not be returned", paths.contains(unwantedPath));
        assertEquals("Applying the second search string should return 0 paths", 0, paths.size());
    }
}