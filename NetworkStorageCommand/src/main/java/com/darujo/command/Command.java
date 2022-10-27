package com.darujo.command;

import com.darujo.command.commanddata.DirListCommandData;
import com.darujo.command.commanddata.FileNameCommandData;
import com.darujo.command.commanddata.MessageCommandData;
import com.darujo.command.commanddata.SendFileCommandData;
import com.darujo.command.commanddata.AuthCommandData;
import com.darujo.command.commanddata.AuthOkCommandData;
import com.darujo.command.commanddata.ErrorCommandData;
import com.darujo.command.commanddata.RegistrationUser;
import com.darujo.command.object.UserPublic;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

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

    public static Command getCommandSendFile(String fileName) throws IOException {
        return new Command(CommandType.SEND_FILE, new SendFileCommandData(fileName));
    }
    public static Command getCommandGetSendFile(String fileName)  {
        return new Command(CommandType.GET_SEND_FILE, new FileNameCommandData(fileName));
    }
    public static Command getCommandErrorMessage(String text) {
        return new Command(CommandType.ERROR_MESSAGE, new MessageCommandData(text));
    }

    public static Command getCommandMessage(CommandType type, String text) {
        return new Command(type, new MessageCommandData(text));
    }

    public static Command getCommandGetDirList(String dirName) {
        return new Command(CommandType.GET_DIR_LIST, new FileNameCommandData(dirName));
    }

    public static Command getCommandDirList(String dirName, File[] files) {
        return new Command(CommandType.DIR_LIST, new DirListCommandData(dirName, files));
    }

    public static Command getAuthCommand(String login, String password) {
        return new Command(CommandType.AUTH, new AuthCommandData(login, password));
    }

    public static Command getAuthOkCommand(UserPublic userPublic) {
        return new Command(CommandType.AUTH_OK, new AuthOkCommandData(userPublic));
    }

    public static Command getErrorMessageCommand(String text) {
        return new Command(CommandType.ERROR_MESSAGE, new ErrorCommandData(text));
    }

    public static Command getErrorMessageCommand(CommandType type, String text) {
        return new Command(type, new ErrorCommandData(text));
    }

    public static Command getAuthNoUserCommand(String text) {
        return getErrorMessageCommand(CommandType.AUTH_NO_USER, text);
    }

    public static Command getRegistrationUserCommand(String login, String password, String userName) {
        return new Command(CommandType.REGISTRATION_USER, new RegistrationUser(login, password, userName));
    }
//    public static Command getChangeUserDataOkCommand(UserPublic userPublic) {
//        return new Command(CommandType.USER_DATA_CHANGE_OK, new ChangeUserDataOkCommandData(userPublic));
//    }


}
