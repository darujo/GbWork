package com.darujo.serverchat.server;

import com.darujo.serverchat.server.auth.User;

public class ParamConnectClient {
    private User user;
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

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public Boolean getAuthOk() {
        return authOk;
    }

    public long getConnectTime() {
        return connectTime;
    }
}
