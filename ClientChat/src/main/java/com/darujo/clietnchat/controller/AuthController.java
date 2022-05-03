package com.darujo.clietnchat.controller;

import com.darujo.clietnchat.ClientChat;
import com.darujo.clietnchat.dialogs.Dialogs;
import com.darujo.command.Command;
import com.darujo.command.commands.AuthOkCommandData;
import com.darujo.command.commands.ErrorCommandData;
import com.darujo.network.ClientHandler;
import com.darujo.network.Network;
import com.darujo.network.ReaderMessage;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;


public class AuthController {
    private ReaderMessage readMessage;
    private ClientHandler clientHandler;

    @FXML
    public TextField loginField;
    @FXML
    public PasswordField passwordField;
    @FXML
    public Button authButton;

    @FXML
    public void executeAuth(ActionEvent actionEvent) throws IOException {
        String password = passwordField.getText();
        String login = loginField.getText();
        if (login.isEmpty() || login.isBlank() || password.isEmpty() || password.isBlank()) {
            Dialogs.AuthError.EMPTY_CREDENTIALS.show();
            return;
        }
        if (clientHandler == null) {
            Network network = Network.getNetwork(ClientChat::showMessage);

            readMessage = (clientHandler, command) -> {
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
                        Platform.runLater(Dialogs.AuthError.INVALID_CREDENTIALS::show);
                        break;
                    }
                    case ERROR_MESSAGE: {
                        String message = ((ErrorCommandData) command.getData()).getText();
                        Platform.runLater(() -> Dialogs.showDialog(Alert.AlertType.ERROR, "Ошибка", "Ошибка", message));
                    }


                }

            };
            clientHandler = network.createSocketClient();
            network.addReaderMessage(readMessage);
        }
        Command command = Command.getAuthCommand(login, password);
        clientHandler.sendCommand(command);


    }

    public void close() {
        Network.getNetwork().removeReaderMessage(readMessage);
    }
}