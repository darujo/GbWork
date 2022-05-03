package com.darujo.command.commands;

import java.io.Serializable;

public class PublicMessageCommand implements Serializable {
    private final String message;

    public PublicMessageCommand(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
