package com.darujo.networkstorageclient.dialogs;

import com.darujo.networkstorageclient.NetworkStorageClient;
import javafx.scene.control.Alert;

public class Dialogs {

    public enum AuthError {
        EMPTY_CREDENTIALS("Логин и пароль должны быть указаны"),
        EMPTY_PASSWORD("Пароли должны быть указаны"),
        EMPTY_PASSWORD_TWO("Повторый пароЫль должен совпадать с новым"),

        INVALID_CREDENTIALS("Логин и пароль заданы некорректно");

        private static final String TITLE = "Ошибка аутентификации";
        private static final String TYPE = TITLE;

        private final String message;

        AuthError(String message) {
            this.message = message;
        }

        public void show() {
            showDialog(Alert.AlertType.ERROR, TITLE, TYPE, message);
        }
    }

    public static void showDialog(Alert.AlertType dialogType, String title, String type, String message) {
        Alert alert = new Alert(dialogType);
        alert.initOwner(NetworkStorageClient.getInstance().getStorageStage());
        alert.setTitle(title);
        alert.setHeaderText(type);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
