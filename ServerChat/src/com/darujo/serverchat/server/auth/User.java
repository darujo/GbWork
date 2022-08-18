package com.darujo.serverchat.server.auth;

public class User {
    private final String userName;
    private final String login;
    private final String password;

    public User(String userName, String login, String password) {
        this.userName = userName;
        this.login = login;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }
}
