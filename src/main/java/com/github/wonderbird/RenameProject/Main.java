package com.github.wonderbird.RenameProject;

import com.github.wonderbird.RenameProject.Logic.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class Main {

    private static ArgumentParser argumentParser = new ArgumentParserImpl();
    private static RenameProjectManager renameProjectManager = new RenameProjectManagerImpl();
    private static Logger logger = LoggerFactory.getLogger(Main.class);
    private static UiManager uiManager = new UiManagerImpl();

    public static void main(String[] args) {
        try {
            argumentParser.parse(args);

            renameProjectManager.renameProject();
        } catch (WrongUsageException aException) {
            System.out.println(aException.getLocalizedMessage());

            uiManager.runUi(args);
        } catch (IOException aException) {
            logger.error(aException.toString());
        }
    }

    static void setArgumentParser(final ArgumentParser aArgumentParser) {
        argumentParser = aArgumentParser;
    }

    static void setRenameProjectManager(RenameProjectManager aRenameProjectManager) {
        renameProjectManager = aRenameProjectManager;
    }

    static void setUiManager(UiManager aUiManager) {
        uiManager = aUiManager;
    }
}
