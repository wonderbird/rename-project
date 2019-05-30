package com.github.wonderbird.RenameProject;

import com.github.wonderbird.RenameProject.Logic.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;

public class Main {

    private static ArgumentParser argumentParser = new ArgumentParserImpl();
    private static RenameProjectManager renameProjectManager = new RenameProjectManagerImpl();
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(final String[] args) {
        try {
            argumentParser.parse(args);

            renameProjectManager.renameProject();
        } catch (final WrongUsageException aException) {
            System.out.println(aException.getLocalizedMessage());
        } catch (final IOException aException) {
            logger.error("Error in I/O operation:", aException);
        }
    }

    static void setArgumentParser(final ArgumentParser aArgumentParser) {
        argumentParser = aArgumentParser;
    }

    static void setRenameProjectManager(final RenameProjectManager aRenameProjectManager) {
        renameProjectManager = aRenameProjectManager;
    }
}
