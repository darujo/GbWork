package com.darujo;

import java.io.Serializable;

public class MessageTest implements Serializable {

    private final String name;
    private final String message;

    public MessageTest(String name, String message) {
        this.name = name;
        this.message = message;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name + " " + message;
    }

    public String getMessage() {
        return message;
    }
}
