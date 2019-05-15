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

            List<Path> affectedPaths = findPathsToRename(fromToPair);

            renameFilesAndDirectories(affectedPaths, fromToPair);

            replaceFileContents(fromToPair);
        }
    }

    private List<Path> findPathsToRename(RenameFromToPair aFromToPair) throws IOException {
        String from = aFromToPair.getFrom();
        String filePattern = "*" + from + "*";
        return fileNamePatternFinder.find(config.getStartDir(), filePattern);
    }

    private void renameFilesAndDirectories(List<Path> aAffectedPaths, RenameFromToPair aFromToPair) throws IOException {

        for (Path sourcePath : aAffectedPaths) {
            String targetPathString = replaceLastPathSibling(sourcePath, aFromToPair);
            Path targetPath = Paths.get(targetPathString);

            logger.info("{} -> {}", sourcePath.toString(), targetPath.toString());

            fileSystemMethods.move(sourcePath, targetPath, REPLACE_EXISTING);
        }
    }

    private String replaceLastPathSibling(Path aSourcePath, RenameFromToPair aFromToPair) {
        String lastSibling = aSourcePath.getFileName().toString();
        String lastSiblingWithReplacement = lastSibling.replaceAll(aFromToPair.getFrom(), aFromToPair.getTo());
        Path result = aSourcePath.getParent().resolve(lastSiblingWithReplacement);

        return result.toString();
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
