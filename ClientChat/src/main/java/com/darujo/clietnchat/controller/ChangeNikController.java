package com.darujo.clietnchat.controller;

import com.darujo.clietnchat.ClientChat;
import com.darujo.clietnchat.dialogs.Dialogs;
import com.darujo.command.Command;
import com.darujo.command.commands.AuthOkCommandData;
import com.darujo.command.commands.ChangeUserDataOkCommandData;
import com.darujo.command.commands.ErrorCommandData;
import com.darujo.network.ClientHandler;
import com.darujo.network.Network;
import com.darujo.network.ReaderMessage;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;

import java.io.IOException;

public class ChangeNikController {

    @FXML
    public CheckBox changeNike;
    private ClientHandler clientHandler;

    @FXML
    public TextField loginField;
    @FXML
    public Label userNameLabel;
    @FXML
    public Button changeButton;
    @FXML
    public Button cancelButton;
    @FXML
    public TextField userNameField;
    @FXML
    public Label badLoginLabel;

    @FXML
    public Label badUserNameLabel;
    @FXML
    public CheckBox changeLogin;

    @FXML
    public void checkLogin(KeyEvent keyEvent) {
        badLoginLabel.setVisible(isBadLoginField());
    }

    @FXML
    public void checkUserName(KeyEvent keyEvent) {
        badUserNameLabel.setVisible(isBadUserNameField());
    }


    @FXML
    public void nextField(ActionEvent actionEvent) {
        final Object obj = actionEvent.getSource();
        Platform.runLater(() -> {
            if (obj == loginField && !isBadLoginField()) {

                if (userNameField.isVisible()) {
                    userNameField.requestFocus();
                } else {
                    cancelButton.requestFocus();
                }
            } else if (obj == userNameField) {
                cancelButton.requestFocus();
            }

        });
    }

    private boolean isBadUserNameField() {
        if (!userNameField.isDisable()) {
            String userName = userNameField.getText();
            return userName.isEmpty() || userName.isBlank();
        }
        return true;
    }

    public boolean isBadLoginField() {
        if (!loginField.isDisable()) {
            String login = loginField.getText();
            return login.isEmpty() || login.isBlank();
        }
        return true;
    }
    private final ReaderMessage readerMessage = (clientHandler, command) -> {
        switch (command.getType()) {
            case USER_DATA_CHANGE_OK: {
                String userName = ((ChangeUserDataOkCommandData) command.getData()).getUserPublic().getUserName();
                Platform.runLater(() ->
                        ClientChat.getInstance().closeWindowsChangeNik(userName)
                );
                break;

            }
            case AUTH_NO_USER: {
                Platform.runLater(Dialogs.AuthError.INVALID_CREDENTIALS::show);
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

    public void reShow() {
        clientHandler = Network.getNetwork().getLastClientHandler();
        clientHandler.addReaderMessage(readerMessage);
    }
    public void close() {
        if (clientHandler!= null) {
            clientHandler.removeReaderMessage(readerMessage);
        }
    }
    private void connectServer() {
        Network network = Network.getNetwork(ClientChat::printNetError);

        clientHandler = network.getLastClientHandler();
        clientHandler.addReaderMessage(readerMessage);

    }

    @FXML
    public void executeCancel(ActionEvent actionEvent) {
        if(clientHandler!= null) {
            clientHandler.removeReaderMessage(readerMessage);
        }
        ClientChat.getInstance().getChangeNikStage().close();
    }

    @FXML
    public void executeChange(ActionEvent actionEvent) throws IOException {
        if (!loginField.isDisable() || !userNameField.isDisable()) {
            if (isBadLoginField() && isBadUserNameField()) {
                Dialogs.AuthError.EMPTY_CREDENTIALS.show();
                return;
            }
            if (clientHandler == null) {
                connectServer();
            }
            clientHandler.sendCommand(Command.getChangeUserNikAndLoginCommand((loginField.isDisable() ? null : loginField.getText()),
                    (userNameField.isDisable() ? null : userNameField.getText())));
//        clientHandler.sendCommand(Command.getRegistrationUserCommand(loginField.getText(), passwordField.getText(), userNameField.getText()));
        }
    }

    @FXML
    public void changeLoginActive(ActionEvent actionEvent) {
        loginField.setDisable(!changeLogin.isSelected());
        badLoginLabel.setVisible(changeLogin.isSelected());
        if (changeLogin.isSelected()){
            loginField.setText("");
        }
    }

    @FXML
    public void changeNikActive(ActionEvent actionEvent) {
        userNameField.setDisable(!changeNike.isSelected());
        badUserNameLabel.setVisible(changeNike.isSelected());
        if (!changeNike.isSelected()){
            userNameField.setText("");
        }
    }
}
