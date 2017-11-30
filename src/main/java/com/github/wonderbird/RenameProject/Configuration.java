package com.github.wonderbird.RenameProject;

public class Configuration {
    private String from;
    private String to;

    /**
     * Size of a block read at once from files (in bytes).
     */
    private int readBufferSize;

    private Configuration() {
        readBufferSize = 1024 * 1024;
    }

    public static Configuration getConfiguration() {
        return new Configuration();
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
