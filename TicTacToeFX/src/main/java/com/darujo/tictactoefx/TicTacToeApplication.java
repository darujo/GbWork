package com.darujo.tictactoefx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;


public class TicTacToeApplication extends Application {

    private static final String FRAME_TITLE = "Крестики-Нолики";

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(TicTacToeApplication.class.getResource("TicTacToe-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        TicTacToeController controller = fxmlLoader.getController();
        controller.createGamePanel();
        controller.startNewGame();
        stage.setTitle(FRAME_TITLE);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}