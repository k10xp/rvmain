package com.c2org.rvapi.api.models;

import java.util.UUID;

public class UserModel {
    private String id;
    private String username;
    private String fullName;
    private String email;
    private String password;

    public UserModel() {
        this.id = UUID.randomUUID().toString();
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
        return this.fullName;
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

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String pw) {
        this.password = pw;
    }
}
