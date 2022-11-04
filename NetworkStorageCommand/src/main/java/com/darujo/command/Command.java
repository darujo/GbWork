package com.darujo.command;

import com.darujo.command.commanddata.*;
import com.darujo.command.object.FileInfo;
import com.darujo.command.object.PathFile;
import com.darujo.command.object.UserPublic;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Set;

public class Command implements Serializable {
    private final Serializable data;
    private final CommandType type;
    private final String token;
    private static String tokenGlobal =null;

    public static void setTokenGlobal(String tokenGlobal) {
        Command.tokenGlobal = tokenGlobal;
    }

    private Command(CommandType type, Serializable data) {
        this.data = data;
        this.type = type;
        this.token = tokenGlobal;
    }

    public Serializable getData() {
        return data;
    }

    public CommandType getType() {
        return type;
    }
    public String getToken(){
        return token;
    }

    @Override
    public String toString() {
        return this.type.toString() + " " + this.data.toString();
    }

    public static Command getSendFileCommand(PathFile path, String fileName) throws IOException {
        return new Command(CommandType.SEND_FILE, new SendFileCommandData(path,fileName));
    }
    public static Command getSendFileCommand(PathFile path, File file) throws IOException {
        return new Command(CommandType.SEND_FILE, new SendFileCommandData(path,file));
    }
    public static Command getDelFileCommand(PathFile pathFile) throws IOException {
        return new Command(CommandType.DEL_FILE, new SendFileCommandData(pathFile));
    }
    public static Command getGetSendFileCommand(PathFile fileName)  {
        return new Command(CommandType.GET_SEND_FILE, new FileNameCommandData(fileName));
    }
    public static Command getErrorMessageCommand(String text) {
        return new Command(CommandType.ERROR_MESSAGE, new MessageCommandData(text));
    }

    public static Command getMessageCommand(CommandType type, String text) {
        return new Command(type, new MessageCommandData(text));
    }

    public static Command getGetDirListCommand(PathFile dirName) {
        return new Command(CommandType.GET_DIR_LIST, new FileNameCommandData(dirName));
    }
    public static Command getGetNewGuidCommand(PathFile dirName) {
        return new Command(CommandType.GET_NEW_GUID, new FileNameCommandData(dirName));
    }
    public static Command getDirListCommand(PathFile dirName, Set<FileInfo> files) {
        return new Command(CommandType.DIR_LIST, new DirListCommandData(dirName, files));
    }

    public static Command getAuthCommand(String login, String password) {
        return new Command(CommandType.AUTH, new AuthCommandData(login, password));
    }

    public static Command getAuthOkCommand(UserPublic userPublic) {
        return new Command(CommandType.AUTH_OK, new AuthOkCommandData(userPublic));
    }

    public static Command getAuthNoUserCommand(String text) {
        return getMessageCommand(CommandType.AUTH_NO_USER, text);
    }

    public static Command getRegistrationUserCommand(String login, String password, String userName) {
        return new Command(CommandType.REGISTRATION_USER, new RegistrationUser(login, password, userName));
    }
    public static Command getFileGuidCommand(PathFile filePath, String guid) {
        return new Command(CommandType.FILE_GUID, new FileGuidCommandData(filePath, guid));
    }
    public static Command getAddGuidCommand( String guid) {
        return new Command(CommandType.ADD_GUID, new MessageCommandData(guid));
    }

//    public static Command getChangeUserDataOkCommand(UserPublic userPublic) {
//        return new Command(CommandType.USER_DATA_CHANGE_OK, new ChangeUserDataOkCommandData(userPublic));
//    }


}
