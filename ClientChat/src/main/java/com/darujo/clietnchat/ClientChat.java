package com.darujo.clietnchat;

import com.darujo.clietnchat.controller.AuthController;
import com.darujo.clietnchat.controller.ClientCharController;
import com.darujo.clietnchat.dialogs.Dialogs;
import com.darujo.network.NetError;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;

public class ClientChat extends Application {

    private Stage chatStage;
    private Stage authStage;

    private FXMLLoader chatWindow;
    private FXMLLoader authWindow;

    private static ClientChat INSTANCE;

    @Override
    public void start(Stage stagePrimary) throws IOException {
        chatStage = stagePrimary;
        initChatDialog();
        initAuthDialog();
        chatStage.show();
        authStage.show();
    }

    private void initChatDialog() throws IOException {
        chatWindow = new FXMLLoader();
        chatWindow.setLocation(ClientChat.class.getResource("clientchat-view.fxml"));
        Scene scene = new Scene(chatWindow.load());
        chatStage.setTitle("Chat");
        chatStage.setScene(scene);
    }

    private void initAuthDialog() throws IOException {
        authWindow = new FXMLLoader();
        authWindow.setLocation(ClientChat.class.getResource("auth-view.fxml"));
        AnchorPane authDialogPanel = authWindow.load();

        authStage = new Stage();
        authStage.initOwner(chatStage);
        authStage.initModality(Modality.WINDOW_MODAL);
        authStage.setScene(new Scene(authDialogPanel));
        authStage.setOnCloseRequest(windowEvent -> chatStage.close());
    }

    public void authShow(){
        authStage.show();
        getAuthController().reShow();
    }

    public static void main(String[] args) {
        launch();
    }

    public void openChatWindow(String userName) {
        getChatController().initializeMessageReader();
        getChatStage().setTitle(userName);
        getAuthController().close();
        getAuthStage().close();
    }

    @Override
    public void init() {
        INSTANCE = this;
    }

    public Stage getChatStage() {
        return chatStage;
    }

    public AuthController getAuthController() {
        return authWindow.getController();
    }

    public ClientCharController getChatController() {
        return chatWindow.getController();
    }

    public Stage getAuthStage() {
        return authStage;
    }

    public static ClientChat getInstance() {
        return INSTANCE;
    }

    public static void showMessage(String message) {
        Platform.runLater(() -> Dialogs.showDialog(Alert.AlertType.ERROR, "Ошибка", "Ошибка", message));
    }
    public static void printNetError(NetError netError,String text){
        if (netError != NetError.DISCONNECT){
            showMessage(text);
        }
    }
}