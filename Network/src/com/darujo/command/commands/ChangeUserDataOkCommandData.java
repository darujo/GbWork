package com.darujo.command.commands;

import com.darujo.command.object.UserPublic;

import java.io.Serializable;

public class ChangeUserDataOkCommandData implements Serializable {
    private final UserPublic userPublic;

    public ChangeUserDataOkCommandData(UserPublic userPublic) {
        this.userPublic = userPublic;
    }

    public UserPublic getUserPublic() {
        return userPublic;
    }
}
