package com.c2org.rvapi.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.c2org.rvapi.api.models.UserModel;
import com.c2org.rvapi.api.service.UserCrud;

@RestController
public class ApiController {
    @GetMapping
    public String baseRoute() {
        return "Welcome to rvapi";
    }

    @Autowired
    private UserCrud userCrud;

    @PostMapping("/users/create")
    public ResponseEntity<String> createUser(@RequestBody UserModel user) {
        try {
            int rows = userCrud.create(user);
            if (rows > 0) {
                return ResponseEntity.ok("User created successfully");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create user");
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }
}
