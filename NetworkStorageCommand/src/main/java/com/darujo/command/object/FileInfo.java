package com.darujo.command.object;

import java.io.Serializable;
import java.util.Objects;

public class FileInfo implements Serializable {
    private final String fileName;
    private final boolean isFile;
    private final String guid;

    public FileInfo(String file, boolean isFile, String guid) {
        this.fileName = file;
        this.isFile = isFile;
        this.guid = guid;
    }

    public String getFileName() {
        return fileName;
    }

    public boolean isFile() {
        return isFile;
    }

    public String getGuid() {
        return guid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FileInfo fileInfo = (FileInfo) o;
        return Objects.equals(fileName, fileInfo.fileName) && Objects.equals(guid, fileInfo.guid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fileName, guid);
    }
}
