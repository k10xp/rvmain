package com.c2org.rvapi.api.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.c2org.rvapi.api.models.TagInfo;
import com.c2org.rvapi.api.models.TagModel;
import com.c2org.rvapi.api.service.TagCrud;
import com.c2org.rvapi.logic.JwtToken;

@RestController
@RequestMapping("/tags")
public class TagController {
    @Autowired
    private TagCrud tagCrud;

    // create tag
    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createTag(
            @RequestBody TagModel tag,
            @RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ", "");
        String userId = JwtToken.matchUser(token);

        try {
            int rows = tagCrud.create(tag, userId, null);
            Map<String, Object> responseBody = new HashMap<>();
            if (rows > 0) {
                responseBody.put("status", "success");
                responseBody.put("statusCode", HttpStatus.CREATED.value());
                responseBody.put("message", "Tag created successfully");

                return new ResponseEntity<>(responseBody, HttpStatus.CREATED);
            } else {
                responseBody.put("status", "error");
                responseBody.put("statusCode", HttpStatus.INTERNAL_SERVER_ERROR.value());
                responseBody.put("message", "Failed to create tag");

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

    // create batch
    @PostMapping("/create/batch")
    public ResponseEntity<Map<String, Object>> createTagBatch(
            @RequestBody List<TagModel> tags,
            @RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ", "");
        String userId = JwtToken.matchUser(token);

        int successCount = 0;
        int failCount = 0;

        try {
            for (TagModel tag : tags) {
                try {
                    int rows = tagCrud.create(tag, userId, null);
                    if (rows > 0) {
                        successCount++;
                    } else {
                        failCount++;
                    }
                } catch (IllegalArgumentException e) {
                    failCount++;
                }
            }

            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("created", successCount);
            responseBody.put("failed", failCount);

            if (successCount == tags.size()) {
                responseBody.put("status", "success");
                responseBody.put("statusCode", HttpStatus.CREATED.value());
                responseBody.put("message", "All tags created successfully");
                return new ResponseEntity<>(responseBody, HttpStatus.CREATED);
            } else if (successCount > 0) {
                responseBody.put("status", "partial_success");
                responseBody.put("statusCode", HttpStatus.MULTI_STATUS.value());
                responseBody.put("message", "Some tags created successfully");
                return new ResponseEntity<>(responseBody, HttpStatus.MULTI_STATUS);
            } else {
                responseBody.put("status", "error");
                responseBody.put("statusCode", HttpStatus.INTERNAL_SERVER_ERROR.value());
                responseBody.put("message", "Failed to create any tags");
                return new ResponseEntity<>(responseBody, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "internal_server_error");
            errorResponse.put("statusCode", HttpStatus.INTERNAL_SERVER_ERROR.value());
            errorResponse.put("message", "Error: " + e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // show all user tags
    @GetMapping("/all")
    public ResponseEntity<?> readTags(@RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ", "");
        String userId = JwtToken.matchUser(token);

        try {
            List<TagInfo> tags = tagCrud.readAll(userId);
            if (tags != null && !tags.isEmpty()) {
                return new ResponseEntity<>(tags, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();

            errorResponse.put("status", "internal_server_error");
            errorResponse.put("statusCode", HttpStatus.INTERNAL_SERVER_ERROR.value());
            errorResponse.put("message", "Error: " + e.getMessage());

            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // set tag status helper function
    private ResponseEntity<Map<String, Object>> toggleTagStatus(String userId, String tagId, boolean isShow) {
        try {
            int rows = tagCrud.tagStatus(userId, tagId, isShow);
            Map<String, Object> responseBody = new HashMap<>();
            if (rows > 0) {
                responseBody.put("status", "success");
                responseBody.put("statusCode", HttpStatus.OK.value());
                responseBody.put("message", "Tag " + (isShow ? "show" : "hidden"));
                return new ResponseEntity<>(responseBody, HttpStatus.OK);
            } else {
                responseBody.put("status", "error");
                responseBody.put("statusCode", HttpStatus.NOT_FOUND.value());
                responseBody.put("message", "User or tag not found with id: " + userId + " " + tagId);
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

    // show tag
    @PutMapping("/{tagId}/toggle-show")
    public ResponseEntity<Map<String, Object>> toggleActive(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable String tagId) {
        String token = authorizationHeader.replace("Bearer ", "");
        String userId = JwtToken.matchUser(token);
        return toggleTagStatus(userId, tagId, true);
    }

    // hide tag
    @PutMapping("/{tagId}/toggle-hide")
    public ResponseEntity<Map<String, Object>> toggleInactive(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable String tagId) {
        String token = authorizationHeader.replace("Bearer ", "");
        String userId = JwtToken.matchUser(token);
        return toggleTagStatus(userId, tagId, false);
    }

    // update
    @PutMapping("/update/{tagId}")
    public ResponseEntity<Map<String, Object>> updateTag(
            @RequestBody TagModel tag,
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable String tagId) {
        String token = authorizationHeader.replace("Bearer ", "");
        String userId = JwtToken.matchUser(token);

        try {
            int rows = tagCrud.update(tag, userId, tagId);
            Map<String, Object> responseBody = new HashMap<>();
            if (rows > 0) {
                responseBody.put("status", "success");
                responseBody.put("statusCode", HttpStatus.CREATED.value());
                responseBody.put("message", "Tag updated successfully");

                return new ResponseEntity<>(responseBody, HttpStatus.CREATED);
            } else {
                responseBody.put("status", "error");
                responseBody.put("statusCode", HttpStatus.INTERNAL_SERVER_ERROR.value());
                responseBody.put("message", "Failed to update tag");

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
