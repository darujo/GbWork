package com.darujo.comand.commanddata;

import java.io.Serializable;

public class GetDirListCommandData implements Serializable {
    private final String dirName;

    public GetDirListCommandData(String text) {
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
