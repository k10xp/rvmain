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
import com.c2org.rvapi.api.service.AdminCrud;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private AdminCrud adminCrud;

    // read all users
    @GetMapping("/read")
    public ResponseEntity<List<DBEntry>> readUsers() {
        try {
            List<DBEntry> users = adminCrud.readAll();
            if (users != null) {
                return new ResponseEntity<>(users, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();

            errorResponse.put("status", "internal_server_error");
            errorResponse.put("statusCode", HttpStatus.INTERNAL_SERVER_ERROR.value());
            errorResponse.put("message", "Error: " + e.getMessage());
        }
        return null;
    }
}
