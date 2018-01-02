package com.github.wonderbird.RenameProject.Models;

public class RenameFromToPair {
    private String from;

    private String to;

    RenameFromToPair(String from, String to) {
        this.from = from;
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }
}
