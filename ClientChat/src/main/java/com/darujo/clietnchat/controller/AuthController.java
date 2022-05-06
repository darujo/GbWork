package com.darujo.clietnchat.controller;

import com.darujo.clietnchat.ClientChat;
import com.darujo.clietnchat.dialogs.Dialogs;
import com.darujo.command.Command;
import com.darujo.command.commands.AuthOkCommandData;
import com.darujo.command.commands.ErrorCommandData;
import com.darujo.event.EventType;
import com.darujo.network.ClientHandler;
import com.darujo.network.Network;
import com.darujo.network.ReaderMessage;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;

import java.io.IOException;

public class AuthController {

    private ClientHandler clientHandler;

    @FXML
    public Label userNameLabel;
    @FXML
    public Label badLoginLabel;
    @FXML
    public Label badPasswordLabel;
    @FXML
    public Label badUserNameLabel;
    @FXML
    public TextField userNameField;
    @FXML
    public TextField loginField;
    @FXML
    public PasswordField passwordField;
    @FXML
    public Button authButton;
    @FXML
    public Button registration;

    private final ReaderMessage readerMessage = (clientHandler, command) -> {
        switch (command.getType()) {
            case AUTH_OK: {

                String userName = ((AuthOkCommandData) command.getData()).userName;
                Platform.runLater(() ->
                        ClientChat.getInstance().openChatWindow(userName)
                );
                break;

            }
            case AUTH_NO_USER: {
                Platform.runLater(Dialogs.AuthError.INVALID_CREDENTIALS::show);
                break;
            }
            case AUTH_INCORRECT_PASSWORD: {
                Platform.runLater(() -> {
                    Dialogs.AuthError.INVALID_CREDENTIALS.show();
                    passwordField.requestFocus();
                });
                break;
            }
            case ERROR_MESSAGE: {
                String message = ((ErrorCommandData) command.getData()).getText();
                Platform.runLater(() -> Dialogs.showDialog(Alert.AlertType.ERROR, "Ошибка", "Ошибка", message));
                break;
            }
            case LOGIN_IS_BUSY: {
                String message = ((ErrorCommandData) command.getData()).getText();
                Platform.runLater(() -> {
                    badLoginLabel.setVisible(true);
                    loginField.requestFocus();
                    Dialogs.showDialog(Alert.AlertType.ERROR, "Ошибка", "Ошибка", message);

                });
                break;
            }
            case USER_NAME_IS_BUSY: {
                String message = ((ErrorCommandData) command.getData()).getText();
                Platform.runLater(() -> {
                    badUserNameLabel.setVisible(true);
                    userNameField.requestFocus();
                    Dialogs.showDialog(Alert.AlertType.ERROR, "Ошибка", "Ошибка", message);

                });
            }
        }
    };

    @FXML
    public void executeAuth(ActionEvent actionEvent) throws IOException {
        if (isBadLoginField() || isBadPasswordField()) {
            Dialogs.AuthError.EMPTY_CREDENTIALS.show();
            return;
        }
        if (clientHandler == null) {
            connectServer();
        }
        clientHandler.sendCommand(Command.getAuthCommand(loginField.getText(), passwordField.getText()));
    }

    public void reShow() {
        Network.getNetwork().addReaderMessage(readerMessage);
        passwordField.setText("");
    }

    private void connectServer() {
        Network network = Network.getNetwork(ClientChat::printNetError);

        network.addReaderEvent(event -> {
            if (event.getEventType() == EventType.ADD_CLIENT_HANDLER) {
                this.clientHandler = (ClientHandler) event.getData();

            } else if (event.getEventType() == EventType.REMOVE_CLIENT_HANDLER) {
                if (this.clientHandler.equals(event.getData())) {
                    this.clientHandler = null;
                    Platform.runLater(() -> {
                        ClientChat.getInstance().authShow();
                        ClientChat.showMessage("Потеряна связь с сервером");
                    });
                }
            }
        });
        network.addReaderMessage(readerMessage);
        clientHandler = network.createSocketClient();
    }

    public void close() {
        Network.getNetwork().removeReaderMessage(readerMessage);
    }

    @FXML
    public void executeRegistration(ActionEvent actionEvent) throws IOException {
        if (userNameField.isVisible()) {
            if (isBadLoginField() || isBadPasswordField() || isBadUserNameField()) {
                Dialogs.AuthError.EMPTY_CREDENTIALS.show();
                return;
            }
            if (clientHandler == null) {
                connectServer();
            }
            clientHandler.sendCommand(Command.getRegistrationUserCommand(loginField.getText(), passwordField.getText(), userNameField.getText()));

        } else {
            userNameLabel.setVisible(true);
            userNameField.setVisible(true);
            badUserNameLabel.setVisible(true);
        }
    }

    @FXML
    public void checkLogin(KeyEvent keyEvent) {
        badLoginLabel.setVisible(isBadLoginField());
    }

    public boolean isBadLoginField() {
        String login = loginField.getText();
        return login.isEmpty() || login.isBlank();
    }

    @FXML
    public void checkPassword(KeyEvent keyEvent) {
        badPasswordLabel.setVisible(isBadPasswordField());
    }

    public boolean isBadPasswordField() {
        String password = passwordField.getText();
        return password.isEmpty() || password.isBlank();
    }

    @FXML
    public void checkUserName(KeyEvent keyEvent) {
        badUserNameLabel.setVisible(isBadUserNameField());
    }

    private boolean isBadUserNameField() {
        if (userNameField.isVisible()) {
            String userName = userNameField.getText();
            return userName.isEmpty() || userName.isBlank();
        }
        return true;
    }
}