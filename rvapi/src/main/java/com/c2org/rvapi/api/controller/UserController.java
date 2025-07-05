package com.c2org.rvapi.api.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.c2org.rvapi.api.models.UserModel;
import com.c2org.rvapi.api.models.UserModelPW;
import com.c2org.rvapi.api.service.UserCrud;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserCrud userCrud;

    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createUser(@RequestBody UserModelPW user) {
        try {
            int rows = userCrud.create(user, false);
            Map<String, Object> responseBody = new HashMap<>();
            if (rows > 0) {
                responseBody.put("status", "success");
                responseBody.put("statusCode", HttpStatus.CREATED.value());
                responseBody.put("message", "User created successfully");

                return new ResponseEntity<>(responseBody, HttpStatus.CREATED);
            } else {
                responseBody.put("status", "error");
                responseBody.put("statusCode", HttpStatus.INTERNAL_SERVER_ERROR.value());
                responseBody.put("message", "Failed to create user");

                return new ResponseEntity<>(responseBody, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (IllegalArgumentException e) {
            Map<String, Object> errorResponse = new HashMap<>();

            errorResponse.put("status", "bad_request");
            errorResponse.put("statusCode", HttpStatus.BAD_REQUEST.value());
            errorResponse.put("message", e.getMessage());

            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();

            errorResponse.put("status", "internal_server_error");
            errorResponse.put("statusCode", HttpStatus.INTERNAL_SERVER_ERROR.value());
            errorResponse.put("message", "Error: " + e.getMessage());

            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/read")
    public ResponseEntity<List<UserModel>> readUsers() {
        try {
            List<UserModel> users = userCrud.readAll();
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

    @GetMapping("/read/{id}")
    public ResponseEntity<?> readUser(@PathVariable String id) {
        try {
            UserModel user = userCrud.readSingle(id);
            if (user != null) {
                return new ResponseEntity<>(user, HttpStatus.OK);
            } else {
                Map<String, Object> responseBody = new HashMap<>();
                responseBody.put("status", "error");
                responseBody.put("statusCode", HttpStatus.NOT_FOUND.value());
                responseBody.put("message", "User not found with id: " + id);

                return new ResponseEntity<>(responseBody, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();

            errorResponse.put("status", "internal_server_error");
            errorResponse.put("statusCode", HttpStatus.INTERNAL_SERVER_ERROR.value());
            errorResponse.put("message", "Error: " + e.getMessage());

            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Map<String, Object>> deleteUser(@PathVariable String id) {
        try {
            int rows = userCrud.delete(id);
            Map<String, Object> responseBody = new HashMap<>();
            if (rows > 0) {
                responseBody.put("status", "success");
                responseBody.put("statusCode", HttpStatus.OK.value());
                responseBody.put("message", "User deleted successfully");

                return new ResponseEntity<>(responseBody, HttpStatus.OK);
            } else {
                responseBody.put("status", "error");
                responseBody.put("statusCode", HttpStatus.NOT_FOUND.value());
                responseBody.put("message", "User not found with id: " + id);

                return new ResponseEntity<>(responseBody, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();

            errorResponse.put("status", "internal_server_error");
            errorResponse.put("statusCode", HttpStatus.INTERNAL_SERVER_ERROR.value());
            errorResponse.put("message", "Error: " + e.getMessage());

            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateUser(@RequestBody UserModelPW user) {
        try {
            int rows = userCrud.create(user, true);
            Map<String, Object> responseBody = new HashMap<>();
            if (rows > 0) {
                responseBody.put("status", "success");
                responseBody.put("statusCode", HttpStatus.CREATED.value());
                responseBody.put("message", "User created successfully");

                return new ResponseEntity<>(responseBody, HttpStatus.CREATED);
            } else {
                responseBody.put("status", "error");
                responseBody.put("statusCode", HttpStatus.INTERNAL_SERVER_ERROR.value());
                responseBody.put("message", "Failed to create user");

                return new ResponseEntity<>(responseBody, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (IllegalArgumentException e) {
            Map<String, Object> errorResponse = new HashMap<>();

            errorResponse.put("status", "bad_request");
            errorResponse.put("statusCode", HttpStatus.BAD_REQUEST.value());
            errorResponse.put("message", e.getMessage());

            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();

            errorResponse.put("status", "internal_server_error");
            errorResponse.put("statusCode", HttpStatus.INTERNAL_SERVER_ERROR.value());
            errorResponse.put("message", "Error: " + e.getMessage());

            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
