package com.darujo.command.commanddata;

import java.io.Serializable;

public class ErrorCommandData implements Serializable {
    public final String text;

    public ErrorCommandData(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
