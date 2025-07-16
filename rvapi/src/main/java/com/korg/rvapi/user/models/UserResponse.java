package com.korg.rvapi.user.models;

public class UserResponse {
    private String name;
    private String email;
    private String username;

    public UserResponse() {
    }

    public UserResponse(UserTable user) {
        this.name = user.getName();
        this.email = user.getEmail();
        this.username = user.getUsername();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
