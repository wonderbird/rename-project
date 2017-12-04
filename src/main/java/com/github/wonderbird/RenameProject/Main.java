package com.github.wonderbird.RenameProject;

import com.github.wonderbird.RenameProject.FileSystemAccess.Implementation.FileContentFinderImpl;
import com.github.wonderbird.RenameProject.FileSystemAccess.Implementation.FileNamePatternFinderImpl;
import com.github.wonderbird.RenameProject.FileSystemAccess.Implementation.FileSystemMethodsImpl;
import com.github.wonderbird.RenameProject.FileSystemAccess.Interfaces.FilePathFinder;
import com.github.wonderbird.RenameProject.FileSystemAccess.Interfaces.FileSystemMethods;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class Main {
    private static Configuration config;

    private static ArgumentParser argumentParser = new ArgumentParserImpl();

    private static FilePathFinder fileContentFinder = new FileContentFinderImpl();

    private static FilePathFinder fileNamePatternFinder = new FileNamePatternFinderImpl();

    private static FileSystemMethods fileSystemMethods = new FileSystemMethodsImpl();

    private static Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        try {
            try {
                config = argumentParser.parse(args);

                renameFilesAndDirectories();

                replaceFileContents();
            } catch (WrongUsageException aException) {
                System.out.println(aException.getLocalizedMessage());

                java.awt.EventQueue.invokeLater(() -> new RenameProjectGUI().setVisible(true));

                logger.info("GUI window has been closed");
            }
        } catch (Exception aE) {
            aE.printStackTrace();
        }
    }

    private static void renameFilesAndDirectories() throws IOException {
        String filePattern = "*" + config.getFrom() + "*";
        List<Path> affectedPaths = fileNamePatternFinder.find(filePattern);

        for (Path sourcePath : affectedPaths) {
            Path targetPath = Paths.get(sourcePath.toString().replace(config.getFrom(), config.getTo()));

            logger.info("{} -> {}", sourcePath.toString(), targetPath.toString());

            fileSystemMethods.move(sourcePath, targetPath, REPLACE_EXISTING);
        }
    }

    private static void replaceFileContents() throws IOException {
        List<Path> affectedPaths = fileContentFinder.find(config.getFrom());

        for (Path path : affectedPaths) {
            logger.info("Replace contents: {}", path.toString());

            fileSystemMethods.replaceInFile(path, config.getFrom(), config.getTo());
        }
    }

    static void setArgumentParser(final ArgumentParser aArgumentParser) {
        argumentParser = aArgumentParser;
    }

    static void setFileNamePatternFinder(final FilePathFinder aFinder) {
        fileNamePatternFinder = aFinder;
    }

    static void setFileContentFinder(FilePathFinder aFinder) {
        fileContentFinder = aFinder;
    }

    static void setFileSystemMethods(FileSystemMethods aFileSystemMethods) {
        fileSystemMethods = aFileSystemMethods;
    }

    static void setLogger(Logger aLogger) {
        logger = aLogger;
    }
}
