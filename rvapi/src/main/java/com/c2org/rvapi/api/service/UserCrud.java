package com.c2org.rvapi.api.service;

import java.time.Instant;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import com.c2org.rvapi.api.models.UserInfo;
import com.c2org.rvapi.api.models.UserModel;
import com.c2org.rvapi.api.models.UserModelPW;
import com.c2org.rvapi.logic.PwHash;

@Service
public class UserCrud {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    // create new user
    public int create(UserModelPW user, boolean update) {
        // validate input
        if (user.getUsername() == null || user.getUsername().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        if (user.getFullname() == null || user.getFullname().isEmpty()) {
            throw new IllegalArgumentException("Full name cannot be null or empty");
        }
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }

        // handle create & update
        String query = "INSERT INTO usermap "
                + "(id, username, fullname, email, hashed_password, acc_active, acc_created, acc_updated) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?) ";

        if (update) {
            query += "ON CONFLICT (id) DO UPDATE SET "
                    + "username = EXCLUDED.username, "
                    + "fullname = EXCLUDED.fullname, "
                    + "email = EXCLUDED.email, "
                    + "hashed_password = EXCLUDED.hashed_password, "
                    + "acc_active = EXCLUDED.acc_active, "
                    + "acc_created = usermap.acc_created, "
                    + "acc_updated = EXCLUDED.acc_updated";
        }

        String newId = update ? user.getId() : UUID.randomUUID().toString();
        String currTime = Instant.now().toString();

        try {
            return jdbcTemplate.update(query,
                    newId,
                    user.getUsername(),
                    user.getFullname(),
                    user.getEmail(),
                    PwHash.hashPassword(user.getPassword()),
                    true,
                    currTime,
                    currTime);
        } catch (DuplicateKeyException ex) {
            throw new RuntimeException("Error", ex);
        }
    }

    // return info for single user
    public UserInfo readSingle(String uid) {
        String query = "SELECT * FROM usermap WHERE id = ?";

        try {
            return jdbcTemplate.queryForObject(query, (rs, rowNum) -> {
                UserInfo user = new UserInfo();
                user.setId(rs.getString("id"));
                user.setUsername(rs.getString("username"));
                user.setFullname(rs.getString("fullname"));
                user.setEmail(rs.getString("email"));
                user.setAccActive(rs.getBoolean("acc_active"));
                user.setAccCreated(rs.getString("acc_created"));
                user.setAccUpdated(rs.getString("acc_updated"));
                return user;
            }, uid);
        } catch (DataAccessException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    // toggle account active/inactive
    public int toggleAcc(String uid, boolean active) {
        String query = "UPDATE usermap SET acc_active = ? WHERE id = ?";

        try {
            return jdbcTemplate.update(query,
                    active ? true : false,
                    uid);
        } catch (DuplicateKeyException ex) {
            throw new RuntimeException("Error", ex);
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

    // query by username
    public UserModel queryByUsername(String username) {
        String query = "SELECT * FROM usermap WHERE username = ?";
        try {
            return jdbcTemplate.queryForObject(
                    query,
                    new BeanPropertyRowMapper<>(UserModel.class),
                    username);
        } catch (DataAccessException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    // create/update token
    public int createToken(UserModelPW user, boolean update,String input_token) {
        if (user.getUsername() == null || user.getUsername().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }

        String query = "INSERT INTO usermap "
                + "(username, auth_token) "
                + "VALUES (?, ?) ";

        if (update) {
            query += "ON CONFLICT (username) DO UPDATE SET "
                    + "auth_token = EXCLUDED.auth_token";
        }

        try {
            return jdbcTemplate.update(query,
                    user.getUsername(),
                    input_token);
        } catch (DuplicateKeyException ex) {
            throw new RuntimeException("Username already exists", ex);
        }
    }
}
