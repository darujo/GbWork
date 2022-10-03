package com.darujo.comand.commanddata;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;

public class SendFileCommandData implements Serializable {

    private final File file;
    private final byte[] data;

    public SendFileCommandData(String fileName ) throws IOException {
        this.file = new File(fileName);

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
