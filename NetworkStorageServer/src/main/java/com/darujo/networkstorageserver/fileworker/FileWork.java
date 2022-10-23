package com.darujo.networkstorageserver.fileworker;

import com.darujo.command.Command;
import com.darujo.command.CommandType;
import com.darujo.command.commanddata.SendFileCommandData;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileWork {
    private static final String WORK_DIR_SERVER = "NetworkStorageServer/user-dir";
    public static Command saveFile (SendFileCommandData fileCommand) {
        Path root = Path.of(WORK_DIR_SERVER);
        Path file = null;
        try {
            Files.createDirectories(root);
            file = root.resolve(fileCommand.getFile().getPath());
            Files.createDirectories(file.getParent());
            Files.createFile(file);
        } catch (FileAlreadyExistsException ignored) {
            // do nothing
        } catch (IOException e) {
            return Command.getCommandMessage(CommandType.ERROR_MESSAGE,"Ошибка создания директории на сервере");
        }
        if (file == null){
            return Command.getCommandMessage(CommandType.ERROR_MESSAGE,"Ошибка создания файла на сервере");
        }

        try {
            Files.write(file, fileCommand.getData());
        } catch (IOException e) {
            return Command.getCommandMessage(CommandType.ERROR_MESSAGE,"Ошибка записив в файл " + file.getFileName());
        }
        return Command.getCommandMessage(CommandType.INFO_MESSAGE,"Файл загружен на сервер " + file.getFileName());
    }

    public static Command getDirList (String dirName){
        File [] files = new File(WORK_DIR_SERVER + "/" + dirName).listFiles();
        if (files == null){
            return Command.getCommandErrorMessage("Директория пуста");
        }
        return Command.getCommandDirList(dirName, files);
    }
}
