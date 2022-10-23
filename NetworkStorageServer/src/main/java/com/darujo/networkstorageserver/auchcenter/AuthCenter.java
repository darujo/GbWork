package com.darujo.networkstorageserver.auchcenter;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AuthCenter {
    private final int AVAIL_TOKEN_MIN = 10;
    private final String URL = "jdbc:sqlite:NetworkStorageServer\\db\\storage.db";
    private final Map<Connection, Boolean> connectDBs = new HashMap<>();
    private static AuthCenter Instance;

    public enum AuthMessage {
        AUTH_OK("OK"),
        INVALID_PASSWORD("Некорректный пароль"),
        INVALID_LOGIN("Некорректный логин"),
        LOGIN_IS_BUSY("Уже есть пользователь с таким логином"),
        USER_NAME_IS_BUSY("Уже есть пользователь с таким именем"),
        USER_DATA_CHANGE_OK("Данные изменены"),
        AUTH_SQL_ERROR("Ошибка обраотк запроса"),
        USER_DATA_NOT_CHANGE("Все данные совпадают");


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

    private Connection getConnection() {
        for (Map.Entry<Connection, Boolean> entry : connectDBs.entrySet()) {
            if (!entry.getValue()) {
                entry.setValue(true);
                return entry.getKey();
            }
        }
        try {
            Connection connection = DriverManager.getConnection(URL);
            connectDBs.put(connection, true);
            return connection;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    private void disableConnect(Connection connection) {
        connectDBs.put(connection, false);
    }

    public void addUser(String userName, String login, String password) {
        Connection connection = getConnection();

        try (PreparedStatement prepInsert = connection.prepareStatement("INSERT INTO usersStorage (name, login, password) VALUES (?,?,?)")) {
            prepInsert.setString(1, userName);
            prepInsert.setString(2, login);
            prepInsert.setString(3, password);
            prepInsert.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            disableConnect(connection);
        }
    }

    public AuthMessage availableUser(String login, String password) {
        User user = getUser(login);
        if (user == null) {
            return AuthMessage.INVALID_LOGIN;
        } else {
            if (password.equals(user.getPassword())){
                setToken(user.getId());
                return AuthMessage.AUTH_OK;
            }
            return  AuthMessage.INVALID_PASSWORD;
        }
    }

    public AuthMessage registrationUser(String login, String password, String userName) {
        User user = getUser(login);
        if (user == null) {
            AuthMessage userNameIsBusy = checkUserName(userName, null);
            if (userNameIsBusy != null) return userNameIsBusy;
            addUser(userName, login, password);
            setToken(login);
            return AuthMessage.AUTH_OK;
        } else {
            return AuthMessage.LOGIN_IS_BUSY;
        }
    }

    private AuthMessage checkUserName(String userName, Integer userId) {
        Connection connection = getConnection();
        try (PreparedStatement prepInsert = connection.prepareStatement(userId == null ?
                "SELECT login FROM usersStorage WHERE  name = ? " :
                "SELECT login FROM usersStorage WHERE  name = ? and id != ? "
        )) {
            prepInsert.setString(1, userName);
            if (userId != null) {
                prepInsert.setInt(2, userId);
            }
            ResultSet resultSet = prepInsert.executeQuery();
            if (resultSet.next()) {
                return AuthMessage.USER_NAME_IS_BUSY;
            }
        } catch (SQLException e) {
            return AuthMessage.AUTH_SQL_ERROR;
        } finally {
            disableConnect(connection);
        }
        return null;
    }

    public User getUser(String login) {
        Connection connection = getConnection();
        User user = null;

        try (PreparedStatement prepInsert = connection.prepareStatement("SELECT id,name, password, token FROM usersStorage WHERE  login = ? ")) {
            prepInsert.setString(1, login);
            ResultSet resultSet = prepInsert.executeQuery();
            while (resultSet.next()) {
                user = new User(resultSet.getInt("id"),
                                resultSet.getString("name"),
                                login,
                                resultSet.getString("password"),
                                resultSet.getString("token"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            disableConnect(connection);
        }
        return user;
    }

    public User getUser(int userId) {
        Connection connection = getConnection();
        User user = null;

        try (PreparedStatement prepInsert = connection.prepareStatement("SELECT login, name, password, token FROM usersStorage WHERE  id = ? ")) {
            prepInsert.setInt(1, userId);
            ResultSet resultSet = prepInsert.executeQuery();
            while (resultSet.next()) {
                user = new User(userId,
                                resultSet.getString("name"),
                                resultSet.getString("login"),
                                resultSet.getString("password"),
                                resultSet.getString("token"));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            disableConnect(connection);
        }
        return user;
    }


    public static AuthCenter getInstance() {
        if (Instance == null) {
            Instance = new AuthCenter();
        }
        return Instance;
    }

    public static void close() {
        if (Instance == null) {
            return;
        }
        try {
            for (Map.Entry<Connection, Boolean> entry : Instance.connectDBs.entrySet()) {

                entry.getKey().close();

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public AuthMessage changeUserData(int id, String loginNew, String passwordOld, String passwordNew, String userNameNew) {
        User user = getUser(id);
        if (user == null) {
            return AuthMessage.AUTH_SQL_ERROR;
        } else if (passwordNew != null) {
            if (!user.getPassword().equals(passwordOld)) {
                return AuthMessage.INVALID_PASSWORD;
            }
        }
        if (userNameNew != null && !user.getUserName().equals(userNameNew)) {
            AuthMessage authMessage = checkUserName(userNameNew, id);
            if (authMessage != null) return authMessage;
        } else
            userNameNew = null;
        if (loginNew != null && !user.getLogin().equals(loginNew)) {
            User userLogin = getUser(loginNew);
            if (userLogin != null) return AuthMessage.LOGIN_IS_BUSY;
        } else
            loginNew = null;
        if (user.getPassword().equals(passwordNew)) {
            passwordNew = null;
        }


        return setUserData(id, loginNew, userNameNew, passwordNew);
    }

    private AuthMessage setUserData(int id, String loginNew, String userNameNew, String passwordNew) {
        StringBuilder command;
        boolean first = true;
        command = new StringBuilder("UPDATE usersStorage SET ");
        if (userNameNew != null) {
            command.append("name = ? ");
            first = false;
        }
        if (loginNew != null) {
            command.append(first ? "" : ", ").append("login = ? ");
            first = false;
        }
        if (passwordNew != null) {
            command.append(first ? "" : ", ").append("password = ? ");
            first = false;
        }
        if (first) {
            return AuthMessage.USER_DATA_NOT_CHANGE;
        } else {
            command.append("where usersStorage.id = ? ");
            Connection connection = getConnection();

            try (PreparedStatement prepInsert = connection.prepareStatement(command.toString())) {
                int count = 1;
                if (userNameNew != null) {
                    prepInsert.setString(count++, userNameNew);
                }
                if (loginNew != null) {
                    prepInsert.setString(count++, loginNew);
                }
                if (passwordNew != null) {
                    prepInsert.setString(count++, passwordNew);
                }
                prepInsert.setInt(count, id);

                prepInsert.execute();
            } catch (SQLException e) {
                return AuthMessage.AUTH_SQL_ERROR;
            } finally {
                disableConnect(connection);
            }
            return AuthMessage.USER_DATA_CHANGE_OK;
        }
    }

    public void setToken(int userId) {
        Connection connection = getConnection();

        try {
            String uuid = getNewUUID(connection);
            try (PreparedStatement prepInsert = connection.prepareStatement("UPDATE usersStorage SET token = ?, tokenTime = ? where usersStorage.id = ? ")) {

                prepInsert.setString(1, uuid);
                prepInsert.setString(2, LocalDateTime.now().toString());
                prepInsert.setInt(3, userId);
                prepInsert.execute();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            disableConnect(connection);
        }
    }
    public void setToken(String login) {
        Connection connection = getConnection();

        try {
            String uuid = getNewUUID(connection);
            try (PreparedStatement prepInsert = connection.prepareStatement("UPDATE usersStorage SET token = ?, tokenTime = ? where usersStorage.login = ? ")) {

                prepInsert.setString(1, uuid);
                prepInsert.setString(2, LocalDateTime.now().toString());
                prepInsert.setString(3, login);
                prepInsert.execute();

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            disableConnect(connection);
        }

    }

    private String getNewUUID(Connection connection) throws SQLException {
        String uuid = null;
        boolean guidAvail = true;
        while (guidAvail) {
            uuid = UUID.randomUUID().toString();
            try (PreparedStatement prepInsert = connection.prepareStatement("SELECT token  FROM usersStorage WHERE  token = ? ")) {
                prepInsert.setString(1, uuid);
                ResultSet resultSet = prepInsert.executeQuery();
                guidAvail = resultSet.next();

            }
        }
        return uuid;
    }

    public void setTokenTime(String token) {
        Connection connection = getConnection();

        try (PreparedStatement prepInsert = connection.prepareStatement("UPDATE usersStorage SET tokenTime = ? where token = ? ")) {

            prepInsert.setString(1, LocalDateTime.now().toString());
            prepInsert.setString(2, token);

            prepInsert.execute();

        } catch (SQLException e) {
            throw new RuntimeException(e);

        } finally {
            disableConnect(connection);
        }
    }

    public boolean checkToken(String token) {
        Connection connection = getConnection();
        try (PreparedStatement prepInsert = connection.prepareStatement("SELECT tokentime  FROM usersStorage WHERE  token = ? ")) {
            prepInsert.setString(1, token);
            ResultSet resultSet = prepInsert.executeQuery();
            LocalDateTime localDateTime = null;
            while (resultSet.next()) {
                localDateTime = LocalDateTime.parse(resultSet.getString("tokentime"));
            }
            if (localDateTime != null) {

                boolean tokenOK = LocalDateTime.now().isBefore(localDateTime.plusMinutes(AVAIL_TOKEN_MIN));
                if (tokenOK){
                    setTokenTime(token);
                }
                return tokenOK;
            } else {
                return false;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            disableConnect(connection);
        }

    }

}
