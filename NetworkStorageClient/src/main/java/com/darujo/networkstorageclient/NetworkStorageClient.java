package com.darujo.networkstorageclient;

import com.darujo.command.Command;
import com.darujo.command.object.UserPublic;
import com.darujo.networkstorageclient.controller.AuthController;
import com.darujo.networkstorageclient.controller.NetworkStorageClientController;
import com.darujo.networkstorageclient.network.NetworkClient;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class NetworkStorageClient extends Application {
    private Stage storageStage;
    private Stage authStage;
    private FXMLLoader storageWindow;
    private FXMLLoader authWindow;
    @Override
    public void start(Stage stage) throws IOException {
        this.storageStage =stage;
        initStorageDialog();
        initAuthDialog();
        authStage.show();

    }
    private void initStorageDialog() throws IOException {
        storageWindow = new FXMLLoader(NetworkStorageClient.class.getResource("networkstorageclient-view.fxml"));
        Scene scene = new Scene(storageWindow.load(), 1080, 720);
        storageStage.setTitle("Сетевое хранилище");
        storageStage.setScene(scene);
        NetworkStorageClientController controller = storageWindow.getController();
        controller.initializeControl();
//        storageStage.show();
//        chatStage.setOnCloseRequest(windowEvent ->   {
//            Network.getNetwork().close();
//            chatStage.close();
//        });
    }
    private void initAuthDialog() throws IOException {
        authWindow = new FXMLLoader();
        authWindow.setLocation(NetworkStorageClient.class.getResource("auth-view.fxml"));
        AnchorPane authDialogPanel = authWindow.load();

        authStage = new Stage();
        authStage.initOwner(storageStage);
        authStage.initModality(Modality.WINDOW_MODAL);
        authStage.setScene(new Scene(authDialogPanel));
        authStage.setTitle("Авторизация в хранилище DARu");
        authStage.setOnCloseRequest(windowEvent -> {
//            Network.getNetwork().close();
            storageStage.close();
        });
    }
    private static NetworkStorageClient INSTANCE;
    @Override
    public void init() {
        INSTANCE = this;
    }
    public static NetworkStorageClient getInstance() {
        return INSTANCE;
    }
    public void openStorageWindow(UserPublic user) {
        getAuthStage().close();
        getStorageController().getFolderList();
        storageStage.show();
    }
    public NetworkStorageClientController getStorageController() {
        return storageWindow.getController();
    }
    public Stage getAuthStage() {
        return authStage;
    }

    public Stage getStorageStage() {
        return storageStage;
    }
    public void authShow()  {
        storageStage.close();
        authStage.show();
        getAuthController().reShow();
    }
    public AuthController getAuthController() {
        return authWindow.getController();
    }
    public static void main(String[] args) {
        launch();
    }
}