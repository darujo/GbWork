package com.darujo.serverchat.server;

public class ParamConnectClient {
    private String userName;
    private Boolean authOk = false;

    public void setAuthOk(Boolean authOk) {
        this.authOk = authOk;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public Boolean getAuthOk() {
        return authOk;
    }
}
