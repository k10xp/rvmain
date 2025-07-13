package com.korg.rvapi.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import com.fasterxml.jackson.annotation.JsonIgnore;

import com.korg.rvapi.auth.PwHash;

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

    @Column(name = "hashed_pw")
    @JsonIgnore // prevent exposing in api responses
    private String hashpw;

    public String getHashpw() {
        return hashpw;
    }

    public void setHashpw(String pw) {
        this.hashpw = PwHash.hashPassword(pw);
    }
}
