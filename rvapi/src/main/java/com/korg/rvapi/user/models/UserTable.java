package com.korg.rvapi.user.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import com.fasterxml.jackson.annotation.JsonIgnore;

//sql crud
@Entity
@Table(name = "users")
public class UserTable {
    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Column(name = "name")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "email")
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(name = "username")
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Column(name = "hashed_pw")
    @JsonIgnore // prevent exposing in api responses
    private String hashpw;

    public String getHashpw() {
        return hashpw;
    }

    public void setHashpw(String pw) {
        this.hashpw = pw;
    }

    @Column(name = "jwt_token")
    @JsonIgnore
    private String savedjwt;

    public String getJwt() {
        return savedjwt;
    }

    public void setJwt(String jwt) {
        this.savedjwt = jwt;
    }

    @Column(name = "time_created")
    private String created;

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    @Column(name = "time_updated")
    private String updated;

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }
}
