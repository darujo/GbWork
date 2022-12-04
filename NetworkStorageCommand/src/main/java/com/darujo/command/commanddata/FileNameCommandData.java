package com.darujo.command.commanddata;

import com.darujo.command.object.PathFile;

import java.io.Serializable;

public class FileNameCommandData implements Serializable {
    private final PathFile filePath;

    public FileNameCommandData(PathFile pathFile) {
        this.filePath = pathFile;
    }

    public PathFile getFilePath() {
        return filePath;
    }

    @Override
    public String toString() {
        return "FileNameCommandData{" +
                "filePath=" + filePath +
                '}' + ' ' + super.toString();
    }
}
