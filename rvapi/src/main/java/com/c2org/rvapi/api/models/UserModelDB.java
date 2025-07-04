package com.c2org.rvapi.api.models;

public class UserModelDB extends UserModel {
    private String hashedPassword;
    private Boolean accDisabled;

    public UserModelDB() {
        this.accDisabled = false;
    }

    public String getHashedPassword() {
        return this.hashedPassword;
    }

    public void setHashedPassword(String input) {
        this.hashedPassword = input;
    }

    public Boolean getAccDisabled() {
        return this.accDisabled;
    }

    public void setAccDisabled(Boolean disabled) {
        this.accDisabled = disabled;
    }
}
