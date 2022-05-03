package com.darujo.serverchat.server.auth;

import java.util.HashMap;
import java.util.Map;

public class AuthCenter {
    private final Map<String ,User> users = new HashMap<>();

    private static AuthCenter Instance;

    public enum AuthMessage {
        AUTH_OK("OK"),
        INVALID_PASSWORD("Некорректный пароль"),
        INVALID_LOGIN("Некорректный логин ");


        private String message;
        AuthMessage(String message) {
            this.message = message;
        }

        public String getMessage() {
           return message;
        }
    }

    private AuthCenter() {
        addUser("user1","login1","pass1");
        addUser("user2","login2","pass2");
        addUser("user3","login3","pass3");
    }

    public void addUser(String userName, String login,String password){
        users.put(login,new User(userName,login,password));
    }
    public User getUser(String login){
        return users.get(login);
    }
    public AuthMessage availableUser (String login, String password){
        User user = getUser(login);
        if (user == null){
            return AuthMessage.INVALID_LOGIN;
        }else{
            return password.equals(user.getPassword()) ? AuthMessage.AUTH_OK : AuthMessage.INVALID_PASSWORD;
        }
    }

    public String getUserName (String login){
        User user = getUser(login);
        if (user == null){
            return null;
        }else{
            return user.getUserName();
        }
    }

    public static AuthCenter getInstance() {
        if (Instance == null){
            Instance = new AuthCenter();
        }
        return Instance;
    }
}
