package com.c2org.rvapi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.c2org.rvapi.logic.JwtToken;

public class JwtTest {
    @Test
    void testJwtToken() {
        String userId = "user123";
        String email = "user@example.com";

        // generate token
        String token = JwtToken.generateToken(userId, email);
        assertNotNull(token, "Token should not be null");

        // get user id
        String extractedUserId = JwtToken.matchUser(token);
        assertEquals(userId, extractedUserId, "User ID should match");

        // get email
        DecodedJWT decodedJWT = JWT.decode(token);
        String extractedEmail = decodedJWT.getClaim("email").asString();
        assertEquals(email, extractedEmail, "Email should match");
    }
}