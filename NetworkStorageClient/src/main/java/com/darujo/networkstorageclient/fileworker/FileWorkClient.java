package com.darujo.networkstorageclient.fileworker;

import com.darujo.command.commanddata.SendFileCommandData;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Consumer;

public class FileWorkClient {

    public static void saveFile(Path saveDir, SendFileCommandData fileCommand, Consumer<String> callBack) {
        Path file = null;
        try {
            Files.createDirectories(saveDir);
            file = saveDir.resolve(fileCommand.getFile().getPath());
            Files.createDirectories(file.getParent());
            Files.createFile(file);
        } catch (FileAlreadyExistsException ignored) {
            // do nothing
        } catch (IOException e) {
            callBack.accept("Ошибка создания директории" + saveDir);
        }
        if (file == null) {
            callBack.accept("Ошибка создания файла");
        } else {
            try {
                Files.write(file, fileCommand.getData());
            } catch (IOException e) {
                callBack.accept("Ошибка записив в файл " + file.getFileName());
            }
        }

    }

}
