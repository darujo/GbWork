package com.darujo.command.commanddata;

import com.darujo.command.object.PathFile;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;

public class SendFileCommandData implements Serializable {
    static final long MAX_FILE_SIZE = 100*1024*1024;
    private final File file;
    private final PathFile path;
    private final byte[] data;

    public SendFileCommandData(PathFile pathFile ) {
        this.path = pathFile;
        this.file = null;
        this.data = null;
    }
    public SendFileCommandData(PathFile path, String fileName ) throws IOException {
        this(path, new File(fileName));

    }
    public SendFileCommandData(PathFile path, File file ) throws IOException {
        this.path =path;
        this.file = file;
        if (file == null){
            this.data = null;
            return;
        }


        if (this.file.length() > MAX_FILE_SIZE){
            throw new IOException("Максимальный размер файла не может превышать " + MAX_FILE_SIZE);
        }

        try {
            this.data = Files.readAllBytes(this.file.toPath());
        } catch (IOException e) {
            throw new IOException("Не удалось прочитать файл " + file.getName());
        }
    }

    public File getFile() {
        return file;
    }
    public PathFile getPathFile() {
        return path;
    }

    public byte[] getData() {
        return data;
    }
}
