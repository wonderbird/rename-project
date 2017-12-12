package com.github.wonderbird.RenameProject.Models;

public enum Notification {
    QUIT("quit"),
    RENAME("rename"),
    BROWSESTARTDIR("browseStartDir");

    private final String message;

    Notification(String aMessageString) {
        message = aMessageString;
    }

    public String toString() {
        return message;
    }
}
