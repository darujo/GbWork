package com.darujo.serverchat.server;

import com.darujo.command.Command;
import com.darujo.command.CommandType;
import com.darujo.command.commands.AuthCommandData;
import com.darujo.command.commands.PrivateMessageCommand;
import com.darujo.command.commands.PublicMessageCommand;
import com.darujo.command.commands.RegistrationUser;
import com.darujo.event.EventType;
import com.darujo.network.ClientHandler;
import com.darujo.network.Network;
import com.darujo.serverchat.server.auth.AuthCenter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServerChat {
    Map<ClientHandler, ParamConnectClient> connectClients = new HashMap<>();

    public ServerChat() {
        Network network = Network.getNetwork();
        network.readerMessages.add((clientHandler, command) -> {
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
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        network.addReaderEvent(event -> {
            try {
                if (event.getEventType() == EventType.ADD_CLIENT_HANDLER) {

                    addClient((ClientHandler) event.getData());

                } else if (event.getEventType() == EventType.REMOVE_CLIENT_HANDLER) {
                    removeClient((ClientHandler) event.getData());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        network.createSocketServer();
    }

    private void registrationAndSendAnswer(ClientHandler clientHandler, RegistrationUser data) throws IOException {
        AuthCenter authCenter = AuthCenter.getInstance();
        AuthCenter.AuthMessage authMessage = authCenter.registrationUser(data.getLogin(), data.getPassword(), data.getUserName());
        String userName = authCenter.getUserName(data.getLogin());
        authSendAnswer(clientHandler, userName, authMessage);
    }

    private void authAndSendAnswer(ClientHandler clientHandler, AuthCommandData data) throws IOException {
        AuthCenter authCenter = AuthCenter.getInstance();
        AuthCenter.AuthMessage authMessage = authCenter.availableUser(data.getLogin(), data.getPassword());
        String userName = authCenter.getUserName(data.getLogin());
        authSendAnswer(clientHandler, userName, authMessage);
    }

    private void authSendAnswer(ClientHandler clientHandler, String userName, AuthCenter.AuthMessage authMessage) throws IOException {
        if (authMessage == AuthCenter.AuthMessage.AUTH_OK) {
            if (isUserWork(userName)) {
                clientHandler.sendCommand(
                        Command.getErrorMessageCommand("Пользователь уже работает на другом устройстве"));
            } else {
                clientHandler.sendCommand(Command.getAuthOkCommand(userName));
                Network.getNetwork().addClientHandler(clientHandler);
                authClient(clientHandler, userName);
            }
        } else if (authMessage == AuthCenter.AuthMessage.INVALID_LOGIN) {
            Command command = Command.getAuthNoUserCommand(authMessage.getMessage());
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

    private synchronized void addClient(ClientHandler clientHandler) throws IOException {
        connectClients.put(clientHandler, new ParamConnectClient());
        notifyUserListUpdatedAllUser();
    }

    private synchronized void authClient(ClientHandler clientHandler, String userName) throws IOException {

        ParamConnectClient connectClient = connectClients.get(clientHandler);
        if (connectClient == null) {
            connectClient = new ParamConnectClient();
            connectClients.put(clientHandler, connectClient);
        }
        connectClient.setAuthOk(true);
        connectClient.setUserName(userName);
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

    private boolean isUserWork(String senderName) {
        for (Map.Entry<ClientHandler, ParamConnectClient> connectClient : connectClients.entrySet()) {
            String userName = connectClient.getValue().getUserName();
            if (userName != null && userName.equals(senderName)) {
                return true;
            }
        }
        return false;
    }

    private void sendPublicMessage(ClientHandler clientHandler, PublicMessageCommand messageCommand) throws IOException {
        String user = connectClients.get(clientHandler).getUserName();
        sendCommandAllAuthUser(Command.getClientMessageCommand(user, messageCommand.getMessage(), false), clientHandler);
    }

    private void sendPrivateMessage(ClientHandler clientHandler, PrivateMessageCommand messageCommand) throws IOException {
        String user = connectClients.get(clientHandler).getUserName();
        for (Map.Entry<ClientHandler, ParamConnectClient> connectClient : connectClients.entrySet()) {
            if (connectClient.getValue().getUserName().equals(messageCommand.getReceiver())) {
                connectClient.getKey().sendCommand(Command.getClientMessageCommand(user, messageCommand.getMessage(), true));
            }
        }
    }

    private List<String> notifyUserListUpdated() {
        List<String> users = new ArrayList<>();
        for (Map.Entry<ClientHandler, ParamConnectClient> connectClient : connectClients.entrySet()) {
            users.add(connectClient.getValue().getUserName());
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

