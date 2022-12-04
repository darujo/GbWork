package com.darujo.command.commanddata;

import java.io.Serializable;

public class MessageCommandData implements Serializable {
    private final String text;

    public MessageCommandData(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return text;
    }
}
