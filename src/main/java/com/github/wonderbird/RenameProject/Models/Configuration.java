package com.github.wonderbird.RenameProject.Models;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Configuration {
    private static Configuration instance;

    private Set<RenameFromToPair> fromToPairs = new HashSet<>();

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

    public void addFromToPair(String aFrom, String aTo) {
        RenameFromToPair pair = new RenameFromToPair(aFrom, aTo);
        fromToPairs.add(pair);
    }

    public List<RenameFromToPair> getFromToPairs() {
        return new ArrayList<>(fromToPairs);
    }

    public int getReadBufferSize() {
        return readBufferSize;
    }

    public String getStartDir() {
        return startDir;
    }

    public void reset() {
        readBufferSize = 1024 * 1024;
        fromToPairs = new HashSet<>();
    }

    public void setReadBufferSize(int readBufferSize) {
        this.readBufferSize = readBufferSize;
    }

    public void setStartDir(String aStartDir) {
        startDir = aStartDir;
    }

}
