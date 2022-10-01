package com.darujo.networkstorageclient;

import com.darujo.networkstorageclient.controller.NetworkStorageClientController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class NetworkStorageClient extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(NetworkStorageClient.class.getResource("networkstorageclient-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1080, 720);
        stage.setTitle("Сетевое хранилище");
        stage.setScene(scene);
        NetworkStorageClientController controller = fxmlLoader.getController();
        controller.initializeControl();
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}