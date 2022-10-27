package com.darujo.command.commanddata;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;

public class SendFileCommandData implements Serializable {
    static final long MAX_FILE_SIZE = 100*1024*1024;
    private final File file;
    private final byte[] data;

    public SendFileCommandData(String fileName ) throws IOException {
        this.file = new File(fileName);
        if (this.file.length() > MAX_FILE_SIZE){
            throw new IOException("Максимальный размер файла не может превышать " + MAX_FILE_SIZE);
        }

        try {
            this.data = Files.readAllBytes(this.file.toPath());
        } catch (IOException e) {
            throw new IOException("Не удалось прочитать файл " + fileName);
        }
    }

    public File getFile() {
        return file;
    }

    public byte[] getData() {
        return data;
    }
}
