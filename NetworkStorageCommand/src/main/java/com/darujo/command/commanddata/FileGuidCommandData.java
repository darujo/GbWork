package com.darujo.command.commanddata;


import com.darujo.command.object.PathFile;

public class FileGuidCommandData extends FileNameCommandData {
    private final String guid;

    public String getGuid() {
        return guid;
    }

    public FileGuidCommandData(PathFile filePath, String guid) {
        super(filePath);
        this.guid = guid;
    }

    @Override
    public String toString() {
        return "FileGuidCommandData{" +
                "guid='" + guid + '\'' +
                '}';
    }
}
