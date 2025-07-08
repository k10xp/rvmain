package com.c2org.rvapi.api.service;

import java.time.Instant;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class CreateLog {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    //create new log (everything should be unique)
    public int create(String userId, String logContent) {
        String query = "INSERT INTO errorlogs "
                + "(log_id, user_id, log_created, log_content) "
                + "VALUES (?, ?, ?, ?)";

        try {
            return jdbcTemplate.update(query,
                    UUID.randomUUID().toString(),
                    userId,
                    Instant.now().toString(),
                    logContent);
        } catch (DuplicateKeyException ex) {
            throw new RuntimeException("Duplicate log_id");
        }
    }
}
