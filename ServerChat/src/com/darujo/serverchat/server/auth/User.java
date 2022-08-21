package com.darujo.serverchat.server.auth;

import com.darujo.command.object.UserPublic;

import java.util.Objects;

public class User{

    private final UserPublic userPublic;
    private final String login;
    private final String password;

    public User(int id, String userName, String login, String password) {
        userPublic = new UserPublic(id,userName);
        this.login = login;
        this.password = password;
    }

    public String getUserName() {
        return userPublic.getUserName();
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }


    public int getId() {
        return userPublic.getId();
    }
    public UserPublic getUserPublic() {
        return  userPublic;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null ) return false;
        if((o instanceof UserPublic)) {
            UserPublic user = (UserPublic) o;
            return userPublic.getId() == user.getId();
        }
        else return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userPublic.getId());
    }


}
