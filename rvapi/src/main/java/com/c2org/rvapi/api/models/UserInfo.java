package com.c2org.rvapi.api.models;

//only used for /users/info
public class UserInfo extends UserModel {
    private boolean accActive;
    private String accCreated;
    private String accUpdated;

    public boolean getAccActive() {
        return this.accActive;
    }

    public void setAccActive(boolean input) {
        this.accActive = input;
    }

    public String getAccCreated() {
        return this.accCreated;
    }

    public void setAccCreated(String input) {
        this.accCreated = input;
    }

    public String getAccUpdated() {
        return this.accUpdated;
    }

    public void setAccUpdated(String input) {
        this.accUpdated = input;
    }
}
