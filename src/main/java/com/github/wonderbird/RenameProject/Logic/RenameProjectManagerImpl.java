package com.github.wonderbird.RenameProject.Logic;

import com.github.wonderbird.RenameProject.FileSystemAccess.Implementation.FileContentFinderImpl;
import com.github.wonderbird.RenameProject.FileSystemAccess.Implementation.FileNamePatternFinderImpl;
import com.github.wonderbird.RenameProject.FileSystemAccess.Implementation.FileSystemMethodsImpl;
import com.github.wonderbird.RenameProject.FileSystemAccess.Interfaces.FilePathFinder;
import com.github.wonderbird.RenameProject.FileSystemAccess.Interfaces.FileSystemMethods;
import com.github.wonderbird.RenameProject.Models.Configuration;
import com.github.wonderbird.RenameProject.Models.RenameFromToPair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class RenameProjectManagerImpl implements RenameProjectManager {
    private Configuration config = Configuration.getConfiguration();
    private FilePathFinder fileContentFinder = new FileContentFinderImpl();
    private FilePathFinder fileNamePatternFinder = new FileNamePatternFinderImpl();
    private FileSystemMethods fileSystemMethods = new FileSystemMethodsImpl();
    private Logger logger = LoggerFactory.getLogger(RenameProjectManagerImpl.class);

    public void renameProject() throws IOException {
        for (RenameFromToPair fromToPair : config.getFromToPairs()) {
            logger.info("Renaming from '{}' to '{}' ...", fromToPair.getFrom(), fromToPair.getTo());

            renameFilesAndDirectories(fromToPair);

            replaceFileContents(fromToPair);
        }
    }

    private void renameFilesAndDirectories(RenameFromToPair aFromToPair) throws IOException {
        String from = aFromToPair.getFrom();
        String filePattern = "*" + from + "*";
        List<Path> affectedPaths = fileNamePatternFinder.find(config.getStartDir(), filePattern);

        for (Path sourcePath : affectedPaths) {
            String sourcePathString = sourcePath.toString();
            String targetPathString = replaceLast(sourcePathString, aFromToPair);
            Path targetPath = Paths.get(targetPathString);

            logger.info("{} -> {}", sourcePath.toString(), targetPath.toString());

            fileSystemMethods.move(sourcePath, targetPath, REPLACE_EXISTING);
        }
    }

    private String replaceLast(String aSourcePathString, RenameFromToPair aFromToPair) {
        String targetPathString = aSourcePathString;

        String from = aFromToPair.getFrom();
        int startOfReplacement = aSourcePathString.lastIndexOf(from);
        if (startOfReplacement >= 0) {
            targetPathString = aSourcePathString.substring(0, startOfReplacement);
            targetPathString += aFromToPair.getTo();
            targetPathString += aSourcePathString.substring(startOfReplacement + from.length());
        }

        return targetPathString;
    }

    private void replaceFileContents(RenameFromToPair aFromToPair) throws IOException {
        List<Path> affectedPaths = fileContentFinder.find(config.getStartDir(), aFromToPair.getFrom());

        for (Path path : affectedPaths) {
            logger.info("Replace contents: {}", path.toString());

            fileSystemMethods.replaceInFile(path, aFromToPair.getFrom(), aFromToPair.getTo());
        }
    }

    void setFileNamePatternFinder(final FilePathFinder aFinder) {
        fileNamePatternFinder = aFinder;
    }

    void setFileContentFinder(FilePathFinder aFinder) {
        fileContentFinder = aFinder;
    }

    void setFileSystemMethods(FileSystemMethods aFileSystemMethods) {
        fileSystemMethods = aFileSystemMethods;
    }

    void setLogger(Logger aLogger) {
        logger = aLogger;
    }
}
