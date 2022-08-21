package com.darujo.serverchat.server;

import com.darujo.command.Command;
import com.darujo.command.CommandType;
import com.darujo.command.commands.*;
import com.darujo.command.object.UserPublic;
import com.darujo.event.EventType;
import com.darujo.network.ClientHandler;
import com.darujo.network.Network;
import com.darujo.serverchat.server.auth.AuthCenter;
import com.darujo.serverchat.server.auth.User;

import java.io.IOException;
import java.util.*;

public class ServerChat {
    Map<ClientHandler, ParamConnectClient> connectClients = new HashMap<>();

    private final long TIMER_OUT_AUTH = 120; // seconds
    private final long TIMER_EXECUTION_FREQUENCY = 5; // seconds
    private Timer timer;

    public ServerChat() {
        Network network = Network.getNetwork();
        network.addReaderMessage((clientHandler, command) -> {
            try {
                if (command.getType() == CommandType.AUTH) {
                    authAndSendAnswer(clientHandler, (AuthCommandData) command.getData());
                } else if (command.getType() == CommandType.GET_USER_LIST) {
                    notifyUserListUpdatedOneUser(clientHandler);
                } else if (command.getType() == CommandType.PRIVATE_MESSAGE) {
                    sendPrivateMessage(clientHandler, (PrivateMessageCommand) command.getData());
                } else if (command.getType() == CommandType.PUBLIC_MESSAGE) {
                    sendPublicMessage(clientHandler, (PublicMessageCommand) command.getData());
                } else if (command.getType() == CommandType.REGISTRATION_USER) {
                    registrationAndSendAnswer(clientHandler, (RegistrationUser) command.getData());
                }else if (command.getType() == CommandType.USER_CHANGE) {
                    setNotAuth(clientHandler);
                } else if (command.getType() == CommandType.USER_DATA_CHANGE) {
                    changeUserData(clientHandler,(ChangeUserData) command.getData());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        network.addReaderEvent(event -> {
            try {
                if (event.getEventType() == EventType.ADD_CLIENT_HANDLER) {
                    addClient((ClientHandler) event.getData());
                    createTimer();
                } else if (event.getEventType() == EventType.REMOVE_CLIENT_HANDLER) {
                    removeClient((ClientHandler) event.getData());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        try {

            network.createSocketServer();
        }
        finally {
            AuthCenter.getInstance().close();
        }

    }

    private void changeUserData(ClientHandler clientHandler, ChangeUserData data) throws IOException {
        ParamConnectClient paramConnectClient = connectClients.get(clientHandler);
        AuthCenter authCenter = AuthCenter.getInstance();
        AuthCenter.AuthMessage authMessage = authCenter.changeUserData(paramConnectClient.getUser().getId(),data.getLogin(),data.getPasswordOld(), data.getPassword(),data.getUserName());
        User user = authCenter.getUser(paramConnectClient.getUser().getId());
        authSendAnswer(clientHandler, user, authMessage,false);
    }

    private void createTimer() {
        if (timer == null) {
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {

                    disconnectNotAuthUserTimeOut();
                }

            };
            timer = new Timer(true);
            timer.scheduleAtFixedRate(timerTask, 0, TIMER_EXECUTION_FREQUENCY * 1000);
            System.out.println("Запущен процесс отключения по таймауту аунтификации.");
        }
    }

    private synchronized void disconnectNotAuthUserTimeOut() {
        boolean isAvailableNotAuthUser = true;
        for (Map.Entry<ClientHandler, ParamConnectClient> connectClient : connectClients.entrySet()) {
            ParamConnectClient paramConnectClient = connectClient.getValue();
            if (!paramConnectClient.getAuthOk()) {
                isAvailableNotAuthUser = false;
                if (paramConnectClient.getConnectTime() + TIMER_OUT_AUTH * 1000 < System.currentTimeMillis()) {
                    ClientHandler clientHandler = connectClient.getKey();
                    try {
                        clientHandler.sendCommand(Command.getErrorMessageCommand("Превышен таймаут " + TIMER_OUT_AUTH + " секунд на авторизацию."));
                        System.out.println("Превышен таймаут " + TIMER_OUT_AUTH + " секунд на авторизацию."
                                + " Время подключения " + new Date(paramConnectClient.getConnectTime())
                                + " текущее время " + new Date(System.currentTimeMillis()));
                        clientHandler.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        if (isAvailableNotAuthUser) {
            stopTimer();

        }
    }

    private void stopTimer() {
        timer.cancel();
        timer = null;
        System.out.println("Остановлен процесс отключения пользователей");

    }

    private void registrationAndSendAnswer(ClientHandler clientHandler, RegistrationUser data) throws IOException {
        AuthCenter authCenter = AuthCenter.getInstance();
        AuthCenter.AuthMessage authMessage = authCenter.registrationUser(data.getLogin(), data.getPassword(), data.getUserName());
        User user = authCenter.getUser(data.getLogin());
        authSendAnswer(clientHandler, user, authMessage);
    }

    private void authAndSendAnswer(ClientHandler clientHandler, AuthCommandData data) throws IOException {
        AuthCenter authCenter = AuthCenter.getInstance();
        AuthCenter.AuthMessage authMessage = authCenter.availableUser(data.getLogin(), data.getPassword());
        User user = authCenter.getUser(data.getLogin());
        authSendAnswer(clientHandler, user, authMessage);
    }

    private void authSendAnswer(ClientHandler clientHandler, User user, AuthCenter.AuthMessage authMessage) throws IOException {
        authSendAnswer( clientHandler,  user, authMessage,true);
    }
    private void authSendAnswer(ClientHandler clientHandler, User user, AuthCenter.AuthMessage authMessage,boolean authCommand) throws IOException {
        if (authMessage == AuthCenter.AuthMessage.AUTH_OK) {
            if (isUserWork(user, clientHandler)) {
                setNotAuth(clientHandler);
                clientHandler.sendCommand(
                        Command.getErrorMessageCommand("Пользователь уже работает на другом устройстве"));
            } else {
                clientHandler.sendCommand(Command.getAuthOkCommand(user.getUserPublic()));
                authClient(clientHandler, user);
            }
        } else if(authMessage == AuthCenter.AuthMessage.USER_DATA_CHANGE_OK) {
            clientHandler.sendCommand(Command.getChangeUserDataOkCommand(user.getUserPublic()));
            notifyUserListUpdatedAllUser();
        } else {
            if(authCommand) {
                setNotAuth(clientHandler);
            }
            if (authMessage == AuthCenter.AuthMessage.INVALID_LOGIN) {
                Command command = Command.getAuthNoUserCommand(authMessage.getMessage());
                connectClients.get(clientHandler).setUser(user);
                clientHandler.sendCommand(command);
            } else {
                CommandType commandType;
                if (authMessage == AuthCenter.AuthMessage.LOGIN_IS_BUSY) {
                    commandType = CommandType.LOGIN_IS_BUSY;
                } else if (authMessage == AuthCenter.AuthMessage.USER_NAME_IS_BUSY) {
                    commandType = CommandType.USER_NAME_IS_BUSY;
                } else {
                    commandType = CommandType.ERROR_MESSAGE;
                }
                clientHandler.sendCommand(Command.getErrorMessageCommand(commandType, authMessage.getMessage()));
            }
        }
    }

    private synchronized void setNotAuth(ClientHandler clientHandler)  {
        connectClients.get(clientHandler).setAuthBad();
    }

    private synchronized void addClient(ClientHandler clientHandler) throws IOException {
        connectClients.put(clientHandler, new ParamConnectClient());
        notifyUserListUpdatedAllUser();
    }

    private synchronized void authClient(ClientHandler clientHandler, User user) throws IOException {

        ParamConnectClient connectClient = connectClients.get(clientHandler);
        if (connectClient == null) {
            connectClient = new ParamConnectClient();
            connectClients.put(clientHandler, connectClient);
        }
        connectClient.setAuthOk(true);
        connectClient.setUser(user);
        notifyUserListUpdatedAllUser();
    }

    private synchronized void removeClient(ClientHandler clientHandler) throws IOException {
        connectClients.remove(clientHandler);
        notifyUserListUpdatedAllUser();
    }

    private void notifyUserListUpdatedOneUser(ClientHandler clientHandler) throws IOException {
        clientHandler.sendCommand(Command.getUpdateUserListCommand(notifyUserListUpdated()));
    }

    private void notifyUserListUpdatedAllUser() throws IOException {
        sendCommandAllAuthUser(Command.getUpdateUserListCommand(notifyUserListUpdated()), null);
    }

    private synchronized boolean isUserWork(User senderUser, ClientHandler clientHandler) {
        for (Map.Entry<ClientHandler, ParamConnectClient> connectClient : connectClients.entrySet()) {
            if (connectClient.getKey() != clientHandler) {
                User user = connectClient.getValue().getUser();
                if (user != null && user.equals(senderUser)) {
                    return true;
                }
            }
        }
        return false;
    }

    private synchronized void sendPublicMessage(ClientHandler clientHandler, PublicMessageCommand messageCommand) throws IOException {
        User user = connectClients.get(clientHandler).getUser();
        sendCommandAllAuthUser(Command.getClientMessageCommand(user.getUserPublic(), messageCommand.getMessage(), false), clientHandler);
    }

    private synchronized void sendPrivateMessage(ClientHandler clientHandler, PrivateMessageCommand messageCommand) throws IOException {
        User user = connectClients.get(clientHandler).getUser();
        for (Map.Entry<ClientHandler, ParamConnectClient> connectClient : connectClients.entrySet()) {
            if (connectClient.getValue().getUser().equals(messageCommand.getReceiver())) {
                connectClient.getKey().sendCommand(Command.getClientMessageCommand(user.getUserPublic(), messageCommand.getMessage(), true));
            }
        }
    }

    private synchronized List<UserPublic> notifyUserListUpdated() {
        List<UserPublic> users = new ArrayList<>();
        for (Map.Entry<ClientHandler, ParamConnectClient> connectClient : connectClients.entrySet()) {
            if(connectClient.getValue().getAuthOk()) {
                users.add(connectClient.getValue().getUser().getUserPublic());
            }
        }
        return users;
    }

    private synchronized void sendCommandAllAuthUser(Command command, ClientHandler clientHandler) throws IOException {
        for (Map.Entry<ClientHandler, ParamConnectClient> connectClient : connectClients.entrySet()) {
            ClientHandler receiver = connectClient.getKey();
            if (clientHandler != receiver && connectClient.getValue().getAuthOk()) {
                receiver.sendCommand(command);
            }
        }
    }

}

