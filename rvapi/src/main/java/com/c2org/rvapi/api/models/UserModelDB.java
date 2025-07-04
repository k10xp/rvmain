package com.c2org.rvapi.api.models;

import java.util.UUID;

public class UserModelDB {
    private String id;
    private String username;
    private String fullName;
    private String email;
    private String hashedPassword;
    private Boolean accDisabled;

    public UserModelDB() {
        this.id = UUID.randomUUID().toString();
        this.accDisabled = false;
    }

    public String getId() {
        return this.id;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
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
