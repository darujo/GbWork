package com.darujo.networkstorageserver.fileworker;

import com.darujo.command.Command;
import com.darujo.command.CommandType;
import com.darujo.command.commanddata.FileNameCommandData;
import com.darujo.command.commanddata.MessageCommandData;
import com.darujo.command.commanddata.SendFileCommandData;
import com.darujo.command.object.FileInfo;
import com.darujo.command.object.PathFile;
import com.darujo.networkstorageserver.auchcenter.AuthCenter;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class FileWork {
    private static final String WORK_DIR_SERVER = "NetworkStorageServer" + File.separator + "user-dir" + File.separator + "user_";

    public static Command saveFile(int userId, SendFileCommandData fileCommand) {
        Path dirName;
        try {
            dirName = getFileName(userId, fileCommand.getPathFile());
        } catch (SQLException e) {
            return Command.getMessageCommand(CommandType.ERROR_MESSAGE, "Guid не действителен");
        }
        try {
            Files.createDirectories(dirName);
            if (fileCommand.getFile() != null) {
                dirName = dirName.resolve(fileCommand.getFile().getName());
                Files.createFile(dirName);
            }
        } catch (FileAlreadyExistsException ignored) {
            // do nothing
        } catch (IOException e) {
            return Command.getMessageCommand(CommandType.ERROR_MESSAGE, "Ошибка создания директории на сервере" + dirName);
        }
        if (fileCommand.getFile() == null) {
            return Command.getMessageCommand(CommandType.INFO_MESSAGE, "Создана директория " + dirName);
        }

        try {
            Files.write(dirName, fileCommand.getData());
        } catch (IOException e) {
            return Command.getMessageCommand(CommandType.ERROR_MESSAGE, "Ошибка записив в файл " + dirName.getFileName());
        }
        return Command.getMessageCommand(CommandType.INFO_MESSAGE, "Файл загружен на сервер " + dirName.getFileName());
    }

    private static Path getFileName(int userID, PathFile pathFile) throws SQLException {
        if (pathFile.getGuid() == null) {
            return Path.of(WORK_DIR_SERVER + userID ).resolve(pathFile.getPath());
        } else {
            return Path.of(AuthCenter.getInstance().getShareFilePath(userID, pathFile.getGuid()), pathFile.getPath());
        }
    }

    public static Command getDirList(int userID, PathFile pathDir) {
        Set<FileInfo> files = new HashSet<>();
        Path dirName;
        try {
            dirName = getFileName(userID, pathDir);
        } catch (SQLException e) {
            return Command.getMessageCommand(CommandType.ERROR_MESSAGE, "Guid не действителен");
        }
        if (pathDir.getGuid()== null && pathDir.getPath().equals("") ) {
            AuthCenter.getInstance().getShareLinkDir(userID).forEach((guid, fileName) -> files.add(new FileInfo(fileName, false, guid)));
        }
        File[] arrFile = dirName.toFile().listFiles();
        if (arrFile == null) {
            return Command.getMessageCommand(CommandType.INFO_MESSAGE, "Нет директории пользователя");
        }
        for (File file : arrFile) {
            files.add(new FileInfo(file.getName(), file.isFile(), AuthCenter.getInstance().getShareGuid(null, file.getPath())));
        }

        if (files.size() == 0) {
            return Command.getMessageCommand(CommandType.INFO_MESSAGE, "Директория пуста");
        }
        return Command.getDirListCommand(pathDir, files);
    }

    public static Command getDelFile(int userID, SendFileCommandData fileCommand) {
        Path root = Path.of(WORK_DIR_SERVER + userID);
        Path file ;
        try {
            file = getFileName(userID, fileCommand.getPathFile());
        } catch (SQLException e) {
            return Command.getMessageCommand(CommandType.ERROR_MESSAGE, "Guid не действителен");
        }
        try {
            Files.createDirectories(root);
            Files.delete(file);
        } catch (FileAlreadyExistsException ignored) {
            // do nothing
        } catch (IOException e) {
            return Command.getMessageCommand(CommandType.ERROR_MESSAGE, "Ошибка создания директории на сервере");
        }
        return Command.getMessageCommand(CommandType.INFO_MESSAGE, "Файл удален с сервер " + file);
    }

    public static Command sendFile(int userId, FileNameCommandData data) {
        Path file ;
        try {
            file = getFileName(userId,data.getFilePath());
        } catch (SQLException e) {
            return Command.getErrorMessageCommand("Не удалось найти файл или guid не действителен");
        }
        try {
            return Command.getSendFileCommand(data.getFilePath(), file.toFile());
        } catch (IOException e) {
            return Command.getErrorMessageCommand("Не удалось найти файл");
        }
    }

    public static Command getNewGuid(int userId, FileNameCommandData data) {
        Path file;
        try {
            file = getFileName(userId,data.getFilePath());
        } catch (SQLException e) {
            return Command.getErrorMessageCommand("Просрочен Guid");
        }
        String guid ;
        try {
            guid = AuthCenter.getInstance().updateShareGuid(userId, file.toString());
        } catch (SQLException e) {
            return Command.getErrorMessageCommand("Что-то пошло не так не удалось обновить GUID ");

        }
        return Command.getFileGuidCommand(data.getFilePath(), guid);
    }

    public static Command addGuidForUser(int userid, MessageCommandData data) {
        try {
            AuthCenter.getInstance().updateShareLink(userid, data.getText());
        } catch (SQLException e) {
            return Command.getErrorMessageCommand("Что-то пошло не так не удалось привязан GUID " + data.getText());
        }
        return Command.getMessageCommand(CommandType.INFO_MESSAGE, "Привязан GUID " + data.getText());

    }
}
