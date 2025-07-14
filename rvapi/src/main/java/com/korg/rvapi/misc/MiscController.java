package com.korg.rvapi.misc;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.korg.rvapi.auth.JwtLogic;
import com.korg.rvapi.auth.PwHash;

@RestController
@RequestMapping("/misc")
public class MiscController {
    @PostMapping("/pw/{pw}")
    public Map<String, Object> createHashPw(@PathVariable String pw) {
        Map<String, Object> response = new HashMap<>();

        response.put("Input password", pw);

        try {
            String hash = PwHash.createHashedPassword(pw);
            response.put("Hashed password", hash);
        } catch (Exception e) {
            e.printStackTrace();
            response.put("Hashed password", "Error: " + e.getMessage());
        }
        return response;
    }

    private final JwtLogic jwtToken;

    @Autowired
    public MiscController(JwtLogic jwtToken) {
        this.jwtToken = jwtToken;
    }

    @PostMapping("/jwt/{userId}/{email}")
    public Map<String, Object> createJwt(@PathVariable String userId, @PathVariable String email) {
        Map<String, Object> response = new HashMap<>();

        response.put("userId", userId);
        response.put("email", email);

        try {
            String token = jwtToken.encodeJWT(userId, email);
            response.put("Token", token);
        } catch (Exception e) {
            e.printStackTrace();
            response.put("Token", "Error: " + e.getMessage());
        }
        return response;
    }
}
