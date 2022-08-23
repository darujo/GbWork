package com.darujo.command.commands;

import com.darujo.command.object.UserPublic;

import java.io.Serializable;
import java.util.List;

public class UpdateUserListCommandData implements Serializable {
    private final List<UserPublic> users;

    public UpdateUserListCommandData(List<UserPublic> users) {
        this.users = users;
    }

    public List<UserPublic> getUsers() {
        return users;
    }
}
