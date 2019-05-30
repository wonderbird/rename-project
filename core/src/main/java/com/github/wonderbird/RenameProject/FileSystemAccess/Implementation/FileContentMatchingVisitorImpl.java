package com.github.wonderbird.RenameProject.FileSystemAccess.Implementation;

import com.github.wonderbird.RenameProject.FileSystemAccess.Interfaces.FilePathVisitorWithResult;
import com.github.wonderbird.RenameProject.Models.Configuration;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileContentMatchingVisitorImpl extends SimpleFileVisitor<Path> implements FilePathVisitorWithResult {
    private static final int BUFFER_SIZE = Configuration.getConfiguration().getReadBufferSize();

    private final String searchString;
    private final PathExclusionPatterns exclusionPatterns;

    private List<Path> result = new ArrayList<>();

    FileContentMatchingVisitorImpl(String aSearchString, String[] aExclusions) {
        searchString = aSearchString;
        exclusionPatterns = new PathExclusionPatterns(aExclusions);
    }

    @Override
    public List<Path> getResult() {
        return result;
    }

    @Override
    public FileVisitResult visitFile(Path aFile, BasicFileAttributes aAttrs) throws IOException {
        Path normalizedPath = aFile.toAbsolutePath().normalize();

        if (!exclusionPatterns.isExcluded(normalizedPath)) {
            if (isSearchStringContainedInFile(normalizedPath)) {
                result.add(normalizedPath);
            }
        }

        return FileVisitResult.CONTINUE;
    }

    private boolean isSearchStringContainedInFile(Path aNormalizedPath) throws IOException {
        final String searchRegex = ".*" + searchString + ".*";
        Pattern pattern = Pattern.compile(searchRegex, Pattern.DOTALL);

        InputStream inputStream = Files.newInputStream(aNormalizedPath);
        try (InputStreamReader reader = new InputStreamReader(inputStream)) {
            char[] buffer = new char[BUFFER_SIZE];
            String endOfPreviousBuffer = "";

            while (reader.read(buffer, 0, BUFFER_SIZE) > 0) {
                String bufferAsText = endOfPreviousBuffer + new String(buffer);

                Matcher matcher = pattern.matcher(bufferAsText);
                if (matcher.matches()) {
                    return true;
                }

                int endOfBufferStartIndex = Math.max(0, bufferAsText.length() - searchString.length());
                endOfPreviousBuffer = bufferAsText.substring(endOfBufferStartIndex);
            }
        }

        return false;
    }
}
