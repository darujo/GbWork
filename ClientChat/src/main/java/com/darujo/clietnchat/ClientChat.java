package com.darujo.clietnchat;

import com.darujo.clietnchat.controller.AuthController;
import com.darujo.clietnchat.controller.ChangeNikController;
import com.darujo.clietnchat.controller.ClientCharController;
import com.darujo.clietnchat.dialogs.Dialogs;
import com.darujo.network.NetError;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class ClientChat extends Application {

    private Stage chatStage;
    private Stage authStage;

    private Stage changeNikStage;

    private FXMLLoader chatWindow;
    private FXMLLoader authWindow;
    private FXMLLoader changeNikWindow;

    private static ClientChat INSTANCE;

    @Override
    public void start(Stage stagePrimary) throws IOException {
        chatStage = stagePrimary;
        initChatDialog();
        initAuthDialog();
        authStage.show();
    }

    private void initChatDialog() throws IOException {
        chatWindow = new FXMLLoader();
        chatWindow.setLocation(ClientChat.class.getResource("clientchat-view.fxml"));
        Scene scene = new Scene(chatWindow.load());
        chatStage.setTitle("Чат DARu");
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
        authStage.setTitle("Авторизация в чат DARu");
        authStage.setOnCloseRequest(windowEvent -> chatStage.close());
    }

    private void initChangeNikDialog() throws IOException {
        changeNikWindow = new FXMLLoader();
        changeNikWindow.setLocation(ClientChat.class.getResource("change-nik-view.fxml"));
        AnchorPane changeNikDialogPanel = changeNikWindow.load();

        changeNikStage = new Stage();
        changeNikStage.initOwner(chatStage);
        changeNikStage.initModality(Modality.WINDOW_MODAL);
        changeNikStage.setScene(new Scene(changeNikDialogPanel));
        changeNikStage.setTitle("Смена ника или логина");
//        changeNikStage.setOnCloseRequest(windowEvent -> chatStage.close());
    }

    public void changeNikShow() throws IOException {
        if (changeNikStage == null){
            initChangeNikDialog();
        }
//        chatStage.close();
        changeNikStage.show();
        getChangeNikController().reShow();
    }

    public void authShow()  {
        chatStage.close();
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
        ChangeNikController changeNikController = getChangeNikController();
        if(changeNikController!= null){
            changeNikController.close();
        }
        getAuthStage().close();
        chatStage.show();
    }
    public void closeWindowsChangeNik(String userName) {
        getChatStage().setTitle(userName);
        getAuthController().close();
        getChangeNikController().close();
        getChangeNikStage().close();
        chatStage.show();
    }

    @Override
    public void init() {
        INSTANCE = this;
    }

    public Stage getChatStage() {
        return chatStage;
    }

    public Stage getChangeNikStage() {
        return changeNikStage;
    }

    public AuthController getAuthController() {
        return authWindow.getController();
    }

    public ChangeNikController getChangeNikController() {

        return changeNikWindow == null ? null : changeNikWindow.getController();
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

    public static void printNetError(NetError netError, String text) {
        if (netError != NetError.DISCONNECT) {
            showMessage(text);
        }
    }
}