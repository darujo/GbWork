package com.darujo.networkstorageclient.controller;

import com.darujo.command.Command;
import com.darujo.command.commanddata.AuthOkCommandData;
import com.darujo.command.commanddata.ErrorCommandData;
import com.darujo.command.object.UserPublic;
import com.darujo.networkstorageclient.NetworkStorageClient;
import com.darujo.networkstorageclient.dialogs.Dialogs;
import com.darujo.networkstorageclient.network.NetworkClient;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;

public class AuthController {


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

    private void parseCommand (Command command)  {
        switch (command.getType()) {
            case AUTH_OK: {

                UserPublic user = ((AuthOkCommandData) command.getData()).getUserPublic();
                Command.setTokenGlobal(user.getToken());
                Platform.runLater(() ->
                        NetworkStorageClient.getInstance().openStorageWindow(user)
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
    }

    @FXML
    public void executeAuth(ActionEvent actionEvent)  {
        if (isBadLoginField() || isBadPasswordField()) {
            Dialogs.AuthError.EMPTY_CREDENTIALS.show();
            return;
        }
        try {
            NetworkClient.getInstance().send(Command.getAuthCommand(loginField.getText(), passwordField.getText()),this::parseCommand);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void reShow() {
        passwordField.setText("");
    }

    @FXML
    public void executeRegistration(ActionEvent actionEvent){
        if (userNameField.isVisible()) {
            if (isBadLoginField() || isBadPasswordField() || isBadUserNameField()) {
                Dialogs.AuthError.EMPTY_CREDENTIALS.show();
                return;
            }
            try {
                NetworkClient.getInstance().send(Command.getRegistrationUserCommand(loginField.getText(), passwordField.getText(), userNameField.getText()),this::parseCommand);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

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

    @FXML
    public void nextField(ActionEvent actionEvent) {
        final Object obj = actionEvent.getSource();
        Platform.runLater(() -> {
            if (obj == loginField && !isBadLoginField()) {
                passwordField.requestFocus();
            } else if (obj == passwordField && !isBadPasswordField()) {
                if (userNameField.isVisible()) {
                    userNameField.requestFocus();
                } else {
                    authButton.requestFocus();
                }
            } else if (obj == userNameField) {
                registration.requestFocus();
            }

        });
    }
}