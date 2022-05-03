package com.darujo.network;

public enum NetError {

    SEND_MESSAGE("Не удалось отправить сообщение!"),
    SERVER_CONNECT("Не удалось установить соединение с сервером!");

    private static final String TITLE = "Сетевая ошибка";
    private static final String TYPE = "Ошибка передачи данных по сети";
    private final String message;

    NetError(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String getMessage(String befMessage) {
        return message + " " + befMessage;
    }

}
