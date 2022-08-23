package com.darujo.clietnchat.controller;

import com.darujo.clietnchat.ClientChat;
import com.darujo.clietnchat.dialogs.Dialogs;
import com.darujo.command.Command;
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

public class ChangePasswordController {

    @FXML
    public Label passwordNewLabel;
    @FXML
    public Label badPasswordOldLabel;
    @FXML
    public Label badPasswordNew2Label;
    @FXML
    public PasswordField passwordOldField;
    @FXML
    public PasswordField passwordNewField;
    @FXML
    public PasswordField passwordNew2Field;
    public Label badPasswordNew1Label;
    private ClientHandler clientHandler;

    @FXML
    public Button changeButton;
    @FXML
    public Button cancelButton;

    @FXML
    public void checkOldPassword(KeyEvent keyEvent) {
        badPasswordOldLabel.setVisible(isOldPasswordField());
    }
    @FXML
    public void checkNewPassword(KeyEvent keyEvent) {
        badPasswordNew1Label.setVisible(isNewPasswordField());
    }
    @FXML
    public void checkNewPassword2(KeyEvent keyEvent) {
        badPasswordNew2Label.setVisible(isNewPasswordField2());
    }

    @FXML
    public void nextField(ActionEvent actionEvent) {
        final Object obj = actionEvent.getSource();
        Platform.runLater(() -> {
            if (obj == passwordOldField && !isOldPasswordField()) {
                passwordNewField.requestFocus();
            } else if (obj == passwordNewField && !isNewPasswordField()) {
                passwordNew2Field.requestFocus();
            } else if (obj == passwordNew2Field && !isNewPasswordField2()) {
                cancelButton.requestFocus();
            }

        });
    }

    private boolean isOldPasswordField() {
        String password = passwordOldField.getText();
        return password.isEmpty() || password.isBlank();
    }
    private boolean isNewPasswordField() {
        String password = passwordNewField.getText();
        return password.isEmpty() || password.isBlank();
    }

    private boolean isNewPasswordField2() {
        String password = passwordNew2Field.getText();
        return password.isEmpty() || password.isBlank() || !password.equals(passwordNewField.getText());
    }

    private final ReaderMessage readerMessage = (clientHandler, command) -> {
        switch (command.getType()) {
            case USER_DATA_CHANGE_OK: {
                Platform.runLater(() ->
                        ClientChat.getInstance().closeWindowsChangePassword()
                );
                break;

            }
            case AUTH_INCORRECT_PASSWORD: {
                Platform.runLater(() -> {
                    Dialogs.AuthError.INVALID_CREDENTIALS.show();
                    passwordOldField.requestFocus();
                });
                break;
            }
            case ERROR_MESSAGE: {
                String message = ((ErrorCommandData) command.getData()).getText();
                Platform.runLater(() -> Dialogs.showDialog(Alert.AlertType.ERROR, "Ошибка", "Ошибка", message));
                break;
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
        ClientChat.getInstance().getChangePasswordStage().close();
    }

    @FXML
    public void executeChange(ActionEvent actionEvent) throws IOException {
        if (isOldPasswordField() || isNewPasswordField() ) {
            Dialogs.AuthError.EMPTY_PASSWORD.show();
            return;
        } else if(isNewPasswordField2()) {
            Dialogs.AuthError.EMPTY_PASSWORD_TWO.show();
            return;
        }
        if (clientHandler == null) {
            connectServer();
        }
        clientHandler.sendCommand(Command.getChangeUserPasswordCommand(passwordOldField.getText(),passwordNewField.getText()));
    }

}
