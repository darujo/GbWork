package com.darujo.comand.commanddata;

import java.io.Serializable;

public class MessageCommandData implements Serializable {
    public final String text;

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
