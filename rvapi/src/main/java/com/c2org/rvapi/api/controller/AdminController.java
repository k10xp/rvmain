package com.c2org.rvapi.api.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.c2org.rvapi.api.models.DBEntry;
import com.c2org.rvapi.api.models.LogEntry;
import com.c2org.rvapi.api.models.TagInfo;
import com.c2org.rvapi.api.service.AdminCrud;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private AdminCrud adminCrud;

    private Map<String, Object> exceptionWrapper(Exception e) {
        Map<String, Object> errorResponse = new HashMap<>();

        errorResponse.put("status", "internal_server_error");
        errorResponse.put("statusCode", HttpStatus.INTERNAL_SERVER_ERROR.value());
        errorResponse.put("message", "Error: " + e.getMessage());

        return errorResponse;
    }

    // read all users
    @GetMapping("/users/read")
    public ResponseEntity<List<DBEntry>> readUsers() {
        try {
            List<DBEntry> users = adminCrud.readAll_users();
            if (users != null) {
                return new ResponseEntity<>(users, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            exceptionWrapper(e);
        }
        return null;
    }

    // read all tags
    @GetMapping("/tags/read")
    public ResponseEntity<List<TagInfo>> readTags() {
        try {
            List<TagInfo> users = adminCrud.readAll_tags();
            if (users != null) {
                return new ResponseEntity<>(users, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            exceptionWrapper(e);
        }
        return null;
    }

    // read all logs
    @GetMapping("/logs/read")
    public ResponseEntity<List<LogEntry>> readLogs() {
        try {
            List<LogEntry> users = adminCrud.readAll_logs();
            if (users != null) {
                return new ResponseEntity<>(users, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            exceptionWrapper(e);
        }
        return null;
    }
}
