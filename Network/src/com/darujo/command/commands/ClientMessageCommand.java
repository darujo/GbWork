package com.darujo.command.commands;

import com.darujo.command.object.UserPublic;

import java.io.Serializable;

public class ClientMessageCommand implements Serializable {
    private final UserPublic sender;
    private final String message;
    private final boolean privateMessage;

    public ClientMessageCommand(UserPublic sender, String message, boolean privateMessage) {
        this.sender = sender;
        this.message = message;
        this.privateMessage = privateMessage;
    }

    public UserPublic getSender() {
        return sender;
    }

    public String getMessage() {
        return message;
    }

    public boolean isPrivateMessage() {
        return privateMessage;
    }
}
