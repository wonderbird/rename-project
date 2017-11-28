package com.github.wonderbird.RenameProject;

import com.github.wonderbird.RenameProject.FileSystemAccess.Implementation.FileContentFinderImpl;
import com.github.wonderbird.RenameProject.FileSystemAccess.Implementation.FileNamePatternFinderImpl;
import com.github.wonderbird.RenameProject.FileSystemAccess.Implementation.FileSystemMethodsImpl;
import com.github.wonderbird.RenameProject.FileSystemAccess.Interfaces.FilePathFinder;
import com.github.wonderbird.RenameProject.FileSystemAccess.Interfaces.FileSystemMethods;

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

    public static void main(String[] args) {
        try {
            config = argumentParser.parse(args);

            renameFilesAndDirectories();

            replaceFileContents();
        } catch (WrongUsageException aException) {
            System.out.println(aException.getLocalizedMessage());
        } catch (IOException aE) {
            aE.printStackTrace();
        }
    }

    private static void renameFilesAndDirectories() throws IOException {
        String filePattern = "*" + config.getFrom() + "*";
        List<Path> affectedPaths = fileNamePatternFinder.find(filePattern);

        for (Path sourcePath : affectedPaths) {
            Path targetPath = Paths.get(sourcePath.toString().replace(config.getFrom(), config.getTo()));
            fileSystemMethods.move(sourcePath, targetPath, REPLACE_EXISTING);
        }
    }

    private static void replaceFileContents() throws IOException {
        List<Path> affectedPaths = fileContentFinder.find(config.getFrom());

        for (Path path : affectedPaths) {
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
}