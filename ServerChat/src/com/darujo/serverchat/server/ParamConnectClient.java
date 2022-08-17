package com.darujo.serverchat.server;

public class ParamConnectClient {
    private String userName;
    private Boolean authOk = false;
    private long connectTime = System.currentTimeMillis();

    public void setAuthOk(Boolean authOk) {
        this.authOk = authOk;
    }

    public void setAuthBad() {
        if (authOk){
            connectTime = System.currentTimeMillis();
        }
        authOk = false;
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

    public long getConnectTime() {
        return connectTime;
    }
}
