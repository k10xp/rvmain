package com.c2org.rvapi.api.service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.c2org.rvapi.api.models.TagInfo;
import com.c2org.rvapi.api.models.TagModel;

@Service
public class TagCrud {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    // create
    public int create(TagModel tag, String userId, String tagId) {
        if (tag.getTagname() == null || tag.getTagname().isEmpty()) {
            throw new IllegalArgumentException("Tag name cannot be null or empty");
        }
        if (tag.getReponame() == null || tag.getReponame().isEmpty()) {
            throw new IllegalArgumentException("Repo cannot be null or empty");
        }

        String query = "INSERT INTO repotags "
                + "(tag_id, user_id, tagname, reponame, latest_tag, tag_created, tag_updated, is_show) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        String currTime = Instant.now().toString();

        try {
            return jdbcTemplate.update(query,
                    (tagId == null) ? UUID.randomUUID().toString() : tagId,
                    userId,
                    tag.getTagname(),
                    tag.getReponame(),
                    null,
                    currTime,
                    currTime,
                    false);
        } catch (DuplicateKeyException ex) {
            throw new RuntimeException(
                    "Duplicate tag: a tag with the same tag_id, user_id, and reponame already exists.", ex);
        }
    }

    // read all (user only)
    public List<TagInfo> readAll(String userId) {
        String query = "SELECT * FROM repotags WHERE user_id = ?";

        try {
            return jdbcTemplate.query(query, (rs, rowNum) -> {
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
            }, userId);
        } catch (DataAccessException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    // toggle hide/unhide
    public int tagStatus(String userId, String tagId, boolean hide) {
        String query = "UPDATE repotags SET is_show = ? WHERE user_id = ? AND tag_id = ?";

        try {
            return jdbcTemplate.update(query,
                    hide ? true : false,
                    userId, tagId);
        } catch (DuplicateKeyException ex) {
            throw new RuntimeException("Error", ex);
        }
    }

    // update
    public int update(TagModel tag, String userId, String tagId) {
        if (tag.getTagname() == null || tag.getTagname().isEmpty()) {
            throw new IllegalArgumentException("Tag name cannot be null or empty");
        }
        if (tag.getReponame() == null || tag.getReponame().isEmpty()) {
            throw new IllegalArgumentException("Repo cannot be null or empty");
        }

        String query = "UPDATE repotags SET tagname = ?, reponame = ?, tag_updated = ? WHERE user_id = ? AND tag_id = ?";

        try {
            return jdbcTemplate.update(query,
                    tag.getTagname(),
                    tag.getReponame(),
                    Instant.now().toString(),
                    userId,
                    tagId);
        } catch (DuplicateKeyException ex) {
            throw new RuntimeException(
                    "Duplicate tag: a tag with the same tag_id, user_id, and reponame already exists.", ex);
        }
    }
}
