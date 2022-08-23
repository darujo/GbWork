package com.darujo.command.commands;

import com.darujo.command.object.UserPublic;

import java.io.Serializable;

public class PrivateMessageCommand implements Serializable {
    private final UserPublic receiver;
    private final String message;

    public PrivateMessageCommand(UserPublic receiver, String message) {
        this.receiver = receiver;
        this.message = message;
    }

    public UserPublic getReceiver() {
        return receiver;
    }

    public String getMessage() {
        return message;
    }
}
