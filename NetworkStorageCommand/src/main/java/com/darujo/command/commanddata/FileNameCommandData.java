package com.darujo.command.commanddata;

import java.io.Serializable;

public class FileNameCommandData implements Serializable {
    private final String dirName;

    public FileNameCommandData(String text) {
        this.dirName = text;
    }

    public String getDirName() {
        return dirName;
    }

    @Override
    public String toString() {
        return dirName;
    }
}
