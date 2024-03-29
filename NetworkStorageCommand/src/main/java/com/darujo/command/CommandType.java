package com.darujo.command;

public enum CommandType {
    AUTH,
    AUTH_OK,
    AUTH_NO_USER,
    AUTH_INCORRECT_PASSWORD,
    SEND_FILE,
    DEL_FILE,
    GET_SEND_FILE,
    ERROR_MESSAGE,
    INFO_MESSAGE,
    GET_DIR_LIST,
    DIR_LIST,
    USER_NAME_IS_BUSY,
    LOGIN_IS_BUSY,
    REGISTRATION_USER,
    GET_NEW_GUID, FILE_GUID, ADD_GUID, FAILED_TOKEN
//    LOGIN_IS_BUSY,
//    USER_NAME_IS_BUSY,
//    USER_CHANGE,
//    USER_DATA_CHANGE,
//    USER_DATA_CHANGE_OK,
//    USER_DATA_CHANGE_FAILED
}
