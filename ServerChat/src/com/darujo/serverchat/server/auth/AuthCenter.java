package com.darujo.serverchat.server.auth;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class AuthCenter {
    private final String URL = "jdbc:sqlite:ServerChat\\db\\chat.db";
    //private final Map<String, User> users = new HashMap<>();
    private final Map<Connection,Boolean> connectDBs = new HashMap<>();
    private static AuthCenter Instance;

    public enum AuthMessage {
        AUTH_OK("OK"),
        INVALID_PASSWORD("Некорректный пароль"),
        INVALID_LOGIN("Некорректный логин "),
        LOGIN_IS_BUSY("Уже есть пользователь с таким логином"),
        USER_NAME_IS_BUSY("Уже есть пользователь с таким именем");

        private final String message;

        AuthMessage(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }

    private AuthCenter() {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    private Connection getConnection(){
        for (Map.Entry<Connection,Boolean> entry : connectDBs.entrySet()) {
            if (!entry.getValue()) {
                entry.setValue(true);
                return entry.getKey();
            }
        }
        try {
            Connection connection = DriverManager.getConnection(URL);
            connectDBs.put( connection,true);
            return  connection;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    private void disableConnect(Connection connection){
        connectDBs.put (connection,false);
    }
    public void addUser(String userName, String login, String password) {
        Connection connection = getConnection();

        try (PreparedStatement prepInsert = connection.prepareStatement("INSERT INTO usersChat (name, login, password) VALUES (?,?,?)")){
            prepInsert.setString(1,userName);
            prepInsert.setString(2,login);
            prepInsert.setString(3,password);
            prepInsert.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        finally {
           disableConnect(connection);
        }
    }

    public AuthMessage availableUser(String login, String password) {
        User user = getUser(login);
        if (user == null) {
            return AuthMessage.INVALID_LOGIN;
        } else {
            return password.equals(user.getPassword()) ? AuthMessage.AUTH_OK : AuthMessage.INVALID_PASSWORD;
        }
    }

    public AuthMessage registrationUser(String login, String password, String userName) {
        User user = getUser(login);
        if (user == null) {
            Connection connection = getConnection();
            try (PreparedStatement prepInsert = connection.prepareStatement("SELECT login FROM usersChat WHERE  name = ? ")){
                prepInsert.setString(1,userName);
                ResultSet resultSet = prepInsert.executeQuery();
                while (resultSet.next()){
                    return AuthMessage.USER_NAME_IS_BUSY;
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            finally {
                disableConnect(connection);
            }
            addUser(userName, login, password);
            return AuthMessage.AUTH_OK;
        } else {
            return AuthMessage.LOGIN_IS_BUSY;
        }
    }

    public User getUser(String login) {
        Connection connection = getConnection();

        try (PreparedStatement prepInsert = connection.prepareStatement("SELECT name, password FROM usersChat WHERE  login = ? ")){
            prepInsert.setString(1,login);
            ResultSet resultSet = prepInsert.executeQuery();
            User user = null;
            while (resultSet.next()){
                user = new User(resultSet.getString("name"),login,resultSet.getString("password"));
            }
            return user;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        finally {
            disableConnect(connection);
        }
    }

    public String getUserName(String login) {
        User user = getUser(login);
        if (user == null) {
            return null;
        } else {
            return user.getUserName();
        }
    }

    public static AuthCenter getInstance() {
        if (Instance == null) {
            Instance = new AuthCenter();
        }
        return Instance;
    }

    public void close (){
        try {
            for (Map.Entry<Connection,Boolean> entry : connectDBs.entrySet()) {

                    entry.getKey().close();

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
