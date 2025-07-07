package com.c2org.rvapi.api.models;

public class DBEntry extends UserInfo {
    private String hashedPassword;
    private String authToken;

    public String getHashedPassword() {
        return this.hashedPassword;
    }

    public void setHashedPassword(String input) {
        this.hashedPassword = input;
    }

    public String getAuthToken() {
        return this.authToken;
    }

    public void setAuthToken(String input) {
        this.authToken = input;
    }
}
