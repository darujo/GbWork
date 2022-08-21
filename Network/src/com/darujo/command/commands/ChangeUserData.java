package com.darujo.command.commands;

import java.io.Serializable;

public class ChangeUserData implements Serializable {
    private final String login;
    private final String password;
    private final String userName;

    private final String passwordOld;


    public ChangeUserData(String login, String passwordOld, String password, String userName) {
        this.login = login;
        this.password = password;
        this.userName = userName;
        this.passwordOld =passwordOld;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getPasswordOld() {
        return passwordOld;
    }

    public String getUserName() {
        return userName;
    }
}
