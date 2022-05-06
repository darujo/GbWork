package com.darujo.command.commands;

import java.io.Serializable;

public class ClientMessageCommand implements Serializable {
    private final String sender;
    private final String message;
    private final boolean privateMessage;

    public ClientMessageCommand(String sender, String message, boolean privateMessage) {
        this.sender = sender;
        this.message = message;
        this.privateMessage = privateMessage;
    }

    public String getSender() {
        return sender;
    }

    public String getMessage() {
        return message;
    }

    public boolean isPrivateMessage() {
        return privateMessage;
    }
}
