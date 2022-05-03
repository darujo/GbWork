package com.darujo.command;

public enum CommandType {
    AUTH,
    AUTH_OK,
    AUTH_NO_USER,
    AUTH_INCORRECT_PASSWORD,
    GET_USER_LIST,
    UPDATE_USERS_LIST,
    PUBLIC_MESSAGE,
    PRIVATE_MESSAGE,
    CLIENT_MESSAGE,
    ERROR_MESSAGE
}
