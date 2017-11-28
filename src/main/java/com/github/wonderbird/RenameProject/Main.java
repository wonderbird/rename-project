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
    private static FilePathFinder fileContentFinder = new FileContentFinderImpl();

    private static FilePathFinder fileNamePatternFinder = new FileNamePatternFinderImpl();

    private static ArgumentParser argumentParser = new ArgumentParserImpl();

    private static FileSystemMethods fileSystemMethods = new FileSystemMethodsImpl();

    private static Configuration config;

    public static void main(String[] args) {
        try {
            config = argumentParser.parse(args);

            renameFilesAndDirectories();

            fileContentFinder.find(config.getFrom());
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
        Main.fileSystemMethods = aFileSystemMethods;
    }
}
