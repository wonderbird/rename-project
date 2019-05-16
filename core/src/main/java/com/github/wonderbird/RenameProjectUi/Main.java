package com.github.wonderbird.RenameProjectUi;

public class Main {
    private static UiManager uiManager = new UiManagerImpl();

    public static void main(String[] args) {
        uiManager.runUi(args);
    }

    static void setUiManager(final UiManager aUiManager) {
        uiManager = aUiManager;
    }
}
