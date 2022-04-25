package com.darujo.clietnchat.clientchat;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.DataFormat;

import java.text.DateFormat;
import java.util.Date;
import java.util.zip.DataFormatException;

public class ClientCharController {
    @FXML
    public TextArea textMessageAria;

    @FXML
    public TextField sendMessageText;

    @FXML
    public Button sendMessageButton;

    @FXML
    public ListView userList;

    public void appendMessageToChar(ActionEvent actionEvent) {
        if (!sendMessageText.getText().isEmpty()){
            textMessageAria.appendText(DateFormat.getDateInstance().format(new Date()));
            textMessageAria.appendText(System.lineSeparator());
            if (!userList.getSelectionModel().isEmpty()) {
                textMessageAria.appendText(userList.getSelectionModel().getSelectedItem().toString() + " ");
            }
            textMessageAria.appendText(sendMessageText.getText());
            textMessageAria.appendText(System.lineSeparator());
            textMessageAria.appendText(System.lineSeparator());
            sendMessageText.setText("");
        }
//        sendMessageText.requestFocus();

        requestFocus();
    }
    private void requestFocus(){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                sendMessageText.requestFocus();
            }
        });
    }

}