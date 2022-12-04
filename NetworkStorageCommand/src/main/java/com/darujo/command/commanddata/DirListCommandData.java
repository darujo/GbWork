package com.darujo.command.commanddata;

import com.darujo.command.object.FileInfo;
import com.darujo.command.object.PathFile;

import java.util.Set;

public class DirListCommandData extends FileNameCommandData {
    private final Set<FileInfo > files;

    public DirListCommandData(PathFile fileName, Set<FileInfo> files) {
        super(fileName);
        this.files = files;
    }


    public Set<FileInfo > getFiles() {
        return files;
    }

}
