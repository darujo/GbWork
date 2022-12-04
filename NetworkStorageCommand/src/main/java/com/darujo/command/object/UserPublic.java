package com.darujo.command.object;

import java.io.Serializable;

public class UserPublic implements Serializable {

    private final int id;
    private final String userName;
    private final String token;
    public UserPublic(int id, String userName) {
        this.id = id;
        this.userName = userName;
        this.token = null;
    }

    public UserPublic(int id, String userName, String token) {
        this.id = id;
        this.userName = userName;
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public String getUserName() {
        return userName;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return  userName + " ( id =" + id +  ")";
    }
}
