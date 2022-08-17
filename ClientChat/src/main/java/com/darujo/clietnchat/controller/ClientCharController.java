package com.darujo.clietnchat.controller;

import com.darujo.clietnchat.ClientChat;
import com.darujo.command.Command;
import com.darujo.command.CommandType;
import com.darujo.command.commands.ClientMessageCommand;
import com.darujo.command.commands.UpdateUserListCommandData;
import com.darujo.network.ClientHandler;
import com.darujo.network.Network;
import com.darujo.network.ReaderMessage;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.awt.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;

public class ClientCharController {
    @FXML
    public TextArea textMessageAria;

    @FXML
    public TextField sendMessageText;

    @FXML
    public Button sendMessageButton;

    @FXML
    public ListView<String> userList;
    @FXML
    public Button SendAll;
    @FXML
    public MenuItem changeUserButton;
    @FXML
    public MenuItem closeMenuButton;

    private ClientHandler clientHandler;

    @FXML
    public void appendMessageToChar(ActionEvent actionEvent) {
        if (!sendMessageText.getText().isEmpty()) {
            if (!userList.getSelectionModel().isEmpty()) {
                List<String> selected = userList.getSelectionModel().getSelectedItems();
                for (String item : selected) {
                    try {
                        clientHandler.sendCommand(Command.getPrivateMessageCommand(item, sendMessageText.getText()));
                        printMessage("Я -> " + item, sendMessageText.getText());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                sendMessageText.setText("");
            } else {
                ClientChat.showMessage("Не выбран получатель");
            }
        }
        requestFocus();
    }

    @FXML
    public void SendMessageAll(ActionEvent actionEvent) {
        if (!sendMessageText.getText().isEmpty()) {
            try {
                clientHandler.sendCommand(Command.getPublicMessageCommand(sendMessageText.getText()));
                printMessage("Я", sendMessageText.getText());
                sendMessageText.setText("");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        requestFocus();
    }

    private void printMessage(String sender, String message) {
        textMessageAria.appendText(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).format(LocalDateTime.now()));
        textMessageAria.appendText(System.lineSeparator());
        textMessageAria.appendText(sender + ": ");
        textMessageAria.appendText(message);
        textMessageAria.appendText(System.lineSeparator());
        textMessageAria.appendText(System.lineSeparator());
        textMessageAria.setStyle("-fx-text-fill: yellow;");
        sendMessageText.setStyle("-fx-text-fill: yellow;");
        textMessageAria.appendText("sss");

    }

    private void requestFocus() {
        Platform.runLater(() -> sendMessageText.requestFocus());
    }

    public void initializeMessageReader() {
        ReaderMessage readMessage = (clientHandler, command) -> {
            if (command.getType() == CommandType.UPDATE_USERS_LIST) {
                UpdateUserListCommandData data = (UpdateUserListCommandData) command.getData();
                Platform.runLater(() ->
                        userList.setItems(FXCollections.observableArrayList(data.getUsers()))
                );
            } else if (command.getType() == CommandType.CLIENT_MESSAGE) {
                ClientMessageCommand clientMessageCommand = (ClientMessageCommand) command.getData();
                Platform.runLater(() -> {
                    String receiver = clientMessageCommand.isPrivateMessage() ? clientMessageCommand.getSender() + " -> мне" : clientMessageCommand.getSender();
                    printMessage(receiver, clientMessageCommand.getMessage());
                });

            }
        };
        Network network = Network.getNetwork();
        network.addReaderMessage(readMessage);
        clientHandler = network.getLastClientHandler();
        try {
            clientHandler.sendCommand(Command.getGetListCommand());
        } catch (IOException e) {
            e.printStackTrace();
        }
        userList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    public void changeUser(ActionEvent actionEvent) {
        Platform.runLater(() -> {
            ClientChat.getInstance().authShow();
            textMessageAria.clear();
        });
    }

    public void actionClose(ActionEvent actionEvent) {
        ClientChat.getInstance().getChatStage().close();
    }
}