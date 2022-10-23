package com.darujo.command.commanddata;

import com.darujo.command.object.UserPublic;

import java.io.Serializable;

public class AuthOkCommandData implements Serializable {
    private final UserPublic userPublic;

    public AuthOkCommandData(UserPublic userPublic) {
        this.userPublic = userPublic;
    }

    public UserPublic getUserPublic() {
        return userPublic;
    }
}
