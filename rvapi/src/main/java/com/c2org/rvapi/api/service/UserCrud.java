package com.c2org.rvapi.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.jdbc.core.JdbcTemplate;

import com.c2org.rvapi.api.models.UserModel;
import com.c2org.rvapi.api.models.UserModelDB;
import com.c2org.rvapi.logic.PwHash;

@Service
public class UserCrud {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int create(UserModel user) {
        // validate input
        if (user.getUsername() == null || user.getUsername().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        if (user.getFullName() == null || user.getFullName().isEmpty()) {
            throw new IllegalArgumentException("Full name cannot be null or empty");
        }
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }

        // create new user object
        UserModelDB newUser = new UserModelDB();
        newUser.setUsername(user.getUsername());
        newUser.setFullName(user.getFullName());
        newUser.setEmail(user.getEmail());
        newUser.setHashedPassword(PwHash.hashPassword(user.getPassword()));

        // will have DuplicateKeyException on confict
        String sql = "INSERT INTO usermap (id, username, full_name, email, hashed_password, acc_disabled) " +
                "VALUES (?, ?, ?, ?, ?, ?) ";

        try {
            return jdbcTemplate.update(sql,
                    newUser.getId(),
                    newUser.getUsername(),
                    newUser.getFullName(),
                    newUser.getEmail(),
                    newUser.getHashedPassword(),
                    newUser.getAccDisabled());
        } catch (DuplicateKeyException ex) {
            throw new RuntimeException("Username already exists", ex);
        }
    }
}
