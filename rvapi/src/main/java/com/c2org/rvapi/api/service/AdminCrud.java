package com.c2org.rvapi.api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.c2org.rvapi.api.models.DBEntry;
import com.c2org.rvapi.api.models.LogEntry;
import com.c2org.rvapi.api.models.TagInfo;

@Service
public class AdminCrud extends UserCrud {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private String query = "SELECT * FROM ";

    // show all users
    public List<DBEntry> readAll_users() {
        try {
            return jdbcTemplate.query(query + "usermap", (rs, rowNum) -> {
                DBEntry user = new DBEntry();
                user.setId(rs.getString("id"));
                user.setUsername(rs.getString("username"));
                user.setFullname(rs.getString("fullname"));
                user.setEmail(rs.getString("email"));
                user.setHashedPassword(rs.getString("hashed_password"));
                user.setAuthToken(rs.getString("auth_token"));
                user.setAccActive(rs.getBoolean("acc_active"));
                user.setAccCreated(rs.getString("acc_created"));
                user.setAccUpdated(rs.getString("acc_updated"));
                return user;
            });
        } catch (DataAccessException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    // show all users
    public List<TagInfo> readAll_tags() {
        try {
            return jdbcTemplate.query(query + "repotags", (rs, rowNum) -> {
                TagInfo tag = new TagInfo();
                tag.setTagId(rs.getString("tag_id"));
                tag.setUserId(rs.getString("user_id"));
                tag.setTagname(rs.getString("tagname"));
                tag.setReponame(rs.getString("reponame"));
                tag.setLatestTag(rs.getString("latest_tag"));
                tag.setCreated(rs.getString("tag_created"));
                tag.setUpdated(rs.getString("tag_updated"));
                tag.setIsShow(rs.getBoolean("is_show"));
                return tag;
            });
        } catch (DataAccessException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    // show all logs
    public List<LogEntry> readAll_logs() {
        try {
            return jdbcTemplate.query(query + "errorlogs", (rs, rowNum) -> {
                LogEntry logs = new LogEntry();
                logs.setLogId(rs.getString("log_id"));
                logs.setUserId(rs.getString("user_id"));
                logs.setLogCreated(rs.getString("log_created"));
                logs.setLogContent(rs.getString("log_content"));
                return logs;
            });
        } catch (DataAccessException ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
