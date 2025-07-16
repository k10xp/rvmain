package com.korg.rvapi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;

import com.korg.rvapi.auth.JwtLogic;

//test as standalone function
public class JwtTest {
    private JwtLogic jwtLogicTest;

    @BeforeEach
    void setUpTestEnvVars() {
        String testSecret = "test-secret";
        String testIssuer = "test-issuer";
        jwtLogicTest = new JwtLogic(testSecret, testIssuer);
    }

    @Test
    void testJwtToken() {
        String userId = "user123";
        String email = "user@example.com";

        // generate token
        String token = jwtLogicTest.encodeJWT(userId, email);
        assertNotNull(token, "Token should not be null");

        // get user id
        String extractedUserId = jwtLogicTest.decodeJWT(token);
        assertEquals(userId, extractedUserId, "User ID should match");

        // get email
        DecodedJWT decodedJWT = JWT.decode(token);
        String extractedEmail = decodedJWT.getClaim("email").asString();
        assertEquals(email, extractedEmail, "Email should match");
    }
}
