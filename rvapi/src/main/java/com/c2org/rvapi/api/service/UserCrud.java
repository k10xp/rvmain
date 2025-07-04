package com.c2org.rvapi.api.service;

import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import com.c2org.rvapi.api.models.UserModel;
import com.c2org.rvapi.api.models.UserModelPW;
import com.c2org.rvapi.logic.PwHash;

@Service
public class UserCrud {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int create(UserModelPW user, boolean update) {
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

        // handle create & update
        String query = "INSERT INTO usermap "
                + "(id, username, full_name, email, hashed_password, acc_disabled) "
                + "VALUES (?, ?, ?, ?, ?, ?) ";

        if (update == true) {
            query += "ON CONFLICT (id) DO UPDATE SET "
                    + "username = EXCLUDED.username, "
                    + "full_name = EXCLUDED.full_name, "
                    + "email = EXCLUDED.email, "
                    + "hashed_password = EXCLUDED.hashed_password, "
                    + "acc_disabled = EXCLUDED.acc_disabled";
        }

        String newId = update ? user.getId() : UUID.randomUUID().toString();

        try {
            return jdbcTemplate.update(query,
                    newId,
                    user.getUsername(),
                    user.getFullName(),
                    user.getEmail(),
                    PwHash.hashPassword(user.getPassword()),
                    false);
        } catch (DuplicateKeyException ex) {
            throw new RuntimeException("Username already exists", ex);
        }
    }

    public List<UserModel> readAll() {
        String query = "SELECT * FROM usermap";

        try {
            return jdbcTemplate.query(
                    query,
                    new BeanPropertyRowMapper<>(UserModel.class));
        } catch (DataAccessException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public UserModel readSingle(String uid) {
        String query = "SELECT * FROM usermap WHERE id = ?";

        try {
            return jdbcTemplate.queryForObject(
                    query,
                    new BeanPropertyRowMapper<>(UserModel.class),
                    uid);
        } catch (DataAccessException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public int delete(String uid) {
        String query = "DELETE FROM usermap WHERE id = ?";

        try {
            return jdbcTemplate.update(query, uid);
        } catch (DataAccessException ex) {
            ex.printStackTrace();
            return -1;
        }
    }

    // confirm user exists
    public boolean confirmExist(String uid) {
        String query = "SELECT * FROM usermap WHERE id = ?";

        try {
            UserModel userModel = jdbcTemplate.queryForObject(
                    query,
                    new BeanPropertyRowMapper<>(UserModel.class),
                    uid);
            return userModel != null;
        } catch (DataAccessException ex) {
            ex.printStackTrace();
            return false;
        }
    }
}
