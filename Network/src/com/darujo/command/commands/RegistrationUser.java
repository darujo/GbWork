package com.darujo.command.commands;

import java.io.Serializable;

public class RegistrationUser implements Serializable {
    private final String login;
    private final String password;
    private final String userName;

    public RegistrationUser(String login, String password, String userName) {
        this.login = login;
        this.password = password;
        this.userName = userName;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getUserName() {
        return userName;
    }
}
