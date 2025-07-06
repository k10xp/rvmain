package com.c2org.rvapi.api.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.c2org.rvapi.api.models.UserInfo;
import com.c2org.rvapi.api.models.UserModel;
import com.c2org.rvapi.api.models.UserModelPW;
import com.c2org.rvapi.api.service.UserCrud;
import com.c2org.rvapi.logic.JwtToken;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserCrud userCrud;

    // create user, need: username, email, fullname, password
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

    // get token, need: username, email, password
    @PostMapping("/token")
    public ResponseEntity<Map<String, Object>> getToken(@RequestBody UserModelPW user) {
        try {
            // get user id
            UserModel dbUser = userCrud.queryByUsername(user.getUsername());

            // generate token + write to db
            String token = JwtToken.generateToken(dbUser.getId(), user.getEmail());
            int rows = userCrud.createToken(user, true, token);

            if (rows > 0) {
                Map<String, Object> responseBody = new HashMap<>();

                responseBody.put("status", "success");
                responseBody.put("statusCode", HttpStatus.OK.value());
                responseBody.put("token", token);
                responseBody.put("username", user.getUsername());

                return new ResponseEntity<>(responseBody, HttpStatus.OK);
            } else {
                // no rows affected, treat as error
                Map<String, Object> errorResponse = new HashMap<>();

                errorResponse.put("status", "not_modified");
                errorResponse.put("statusCode", HttpStatus.NOT_MODIFIED.value());
                errorResponse.put("message", "No user was created or updated.");

                return new ResponseEntity<>(errorResponse, HttpStatus.NOT_MODIFIED);
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

    // current user info, need: token
    @GetMapping("/info")
    public ResponseEntity<?> readUser(@RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ", "");
        String userId = JwtToken.matchUser(token);

        try {
            UserInfo user = userCrud.readSingle(userId);
            if (user != null) {
                return new ResponseEntity<>(user, HttpStatus.OK);
            } else {
                Map<String, Object> responseBody = new HashMap<>();
                responseBody.put("status", "error");
                responseBody.put("statusCode", HttpStatus.NOT_FOUND.value());
                responseBody.put("message", "User not found with id: " + userId);

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

    // set user status helper function
    private ResponseEntity<Map<String, Object>> toggleUserStatus(String id, boolean active) {
        try {
            int rows = userCrud.toggleAcc(id, active);
            Map<String, Object> responseBody = new HashMap<>();
            if (rows > 0) {
                responseBody.put("status", "success");
                responseBody.put("statusCode", HttpStatus.OK.value());
                responseBody.put("message", "Account " + (active ? "active" : "inactive"));
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

    // set user active, need: token
    @PutMapping("/status/toggle-active")
    public ResponseEntity<Map<String, Object>> toggleActive(
            @RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ", "");
        String userId = JwtToken.matchUser(token);
        return toggleUserStatus(userId, true);
    }

    // set user inactive, need: token
    @PutMapping("/status/toggle-inactive")
    public ResponseEntity<Map<String, Object>> toggleInactive(
            @RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ", "");
        String userId = JwtToken.matchUser(token);
        return toggleUserStatus(userId, false);
    }

    // update single user, need: id, username, email, fullname, password
    @PutMapping("/update")
    public ResponseEntity<?> updateUser(@RequestBody UserModelPW user) {
        try {
            int rows = userCrud.create(user, true);
            Map<String, Object> responseBody = new HashMap<>();
            if (rows > 0) {
                responseBody.put("status", "success");
                responseBody.put("statusCode", HttpStatus.CREATED.value());
                responseBody.put("message", "User updated successfully");

                return new ResponseEntity<>(responseBody, HttpStatus.CREATED);
            } else {
                responseBody.put("status", "error");
                responseBody.put("statusCode", HttpStatus.INTERNAL_SERVER_ERROR.value());
                responseBody.put("message", "Failed to update user");

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

    // update single user, need: token + json to update (username, email, fullname,
    // password)
    @PutMapping("/update/v1")
    public ResponseEntity<?> updateUserV1(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody UserModelPW input) {
        String token = authorizationHeader.replace("Bearer ", "");

        UserModelPW user = new UserModelPW();
        user.setId(JwtToken.matchUser(token));
        user.setUsername(input.getUsername());
        user.setEmail(input.getEmail());
        user.setFullname(input.getFullname());
        user.setPassword(input.getPassword());

        try {
            int rows = userCrud.create(user, true);
            Map<String, Object> responseBody = new HashMap<>();
            if (rows > 0) {
                responseBody.put("status", "success");
                responseBody.put("statusCode", HttpStatus.CREATED.value());
                responseBody.put("message", "User updated successfully");

                return new ResponseEntity<>(responseBody, HttpStatus.CREATED);
            } else {
                responseBody.put("status", "error");
                responseBody.put("statusCode", HttpStatus.INTERNAL_SERVER_ERROR.value());
                responseBody.put("message", "Failed to update user");

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
