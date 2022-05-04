package com.darujo.command;

import com.darujo.command.commands.*;

import java.io.Serializable;
import java.util.List;

public class Command implements Serializable {
    private final Serializable data;
    private final CommandType type;

    private Command(CommandType type, Serializable data) {
        this.data = data;
        this.type = type;
    }

    public Serializable getData() {
        return data;
    }

    public CommandType getType() {
        return type;
    }

    public static Command getAuthCommand(String login, String password) {
        return new Command(CommandType.AUTH, new AuthCommandData(login, password));
    }

    public static Command getAuthOkCommand(String userName) {
        return new Command(CommandType.AUTH_OK, new AuthOkCommandData(userName));
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

    public static Command getUpdateUserListCommand(List<String> users) {
        return new Command(CommandType.UPDATE_USERS_LIST, new UpdateUserListCommandData(users));
    }

    public static Command getGetListCommand() {
        return new Command(CommandType.GET_USER_LIST, null);
    }

    public static Command getClientMessageCommand(String sender, String message, boolean privateMessage) {
        return new Command(CommandType.CLIENT_MESSAGE, new ClientMessageCommand(sender, message, privateMessage));
    }

    public static Command getPrivateMessageCommand(String receiver, String message) {
        return new Command(CommandType.PRIVATE_MESSAGE, new PrivateMessageCommand(receiver, message));
    }

    public static Command getPublicMessageCommand(String message) {
        return new Command(CommandType.PUBLIC_MESSAGE, new PublicMessageCommand(message));
    }

    public static Command getRegistrationUserCommand(String login, String password, String userName) {
        return new Command(CommandType.REGISTRATION_USER, new RegistrationUser(login, password, userName));
    }
}
