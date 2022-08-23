package com.darujo.command.object;

import java.io.Serializable;

public class UserPublic implements Serializable {

    private final int id;
    private final String userName;

    public UserPublic(int id, String userName) {
        this.id = id;
        this.userName = userName;
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
