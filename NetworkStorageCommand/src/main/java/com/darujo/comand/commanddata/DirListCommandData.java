package com.darujo.comand.commanddata;

import java.io.File;
import java.io.Serializable;
import java.util.Arrays;

public class DirListCommandData implements Serializable {
    private final String dirName;
    private final File [] files;

    public DirListCommandData(String dirName, File[] files) {
        this.dirName = dirName;
        this.files = files;
    }

    public String getDirName() {
        return dirName;
    }

    public File[] getFiles() {
        return files;
    }

    @Override
    public String toString() {
        return Arrays.toString(files);
    }
}
