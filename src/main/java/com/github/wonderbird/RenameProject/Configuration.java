package com.github.wonderbird.RenameProject;

public class Configuration {
    private static Configuration instance;
    private String from;
    private String to;

    /**
     * Size of a block read at once from files (in bytes).
     */
    private int readBufferSize;

    protected Configuration() {
        reset();
    }

    public static Configuration getConfiguration() {
        if (instance == null) {
            instance = new Configuration();
        }
        return instance;
    }

    String getFrom() {
        return from;
    }

    public int getReadBufferSize() {
        return readBufferSize;
    }

    String getTo() {
        return to;
    }

    public void reset() {
        readBufferSize = 1024 * 1024;
    }

    void setFrom(final String aFromPattern) {
        from = aFromPattern;
    }

    public void setReadBufferSize(int readBufferSize) {
        this.readBufferSize = readBufferSize;
    }

    void setTo(String to) {
        this.to = to;
    }
}
