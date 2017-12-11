package com.github.wonderbird.RenameProject.Models;

public class Configuration {
    private static Configuration instance;
    private String from;
    private String to;

    /**
     * Size of a block read at once from files (in bytes).
     */
    private int readBufferSize;
    private String startDir;

    protected Configuration() {
        reset();
    }

    public static Configuration getConfiguration() {
        if (instance == null) {
            instance = new Configuration();
        }
        return instance;
    }

    public String getFrom() {
        return from;
    }

    public int getReadBufferSize() {
        return readBufferSize;
    }

    public String getStartDir() {
        return startDir;
    }

    public String getTo() {
        return to;
    }

    public void reset() {
        readBufferSize = 1024 * 1024;
    }

    public void setFrom(final String aFromPattern) {
        from = aFromPattern;
    }

    public void setReadBufferSize(int readBufferSize) {
        this.readBufferSize = readBufferSize;
    }

    public void setStartDir(String aStartDir) {
        startDir = aStartDir;
    }

    public void setTo(String to) {
        this.to = to;
    }
}
