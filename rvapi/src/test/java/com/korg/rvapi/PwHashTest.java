package com.korg.rvapi;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.korg.rvapi.auth.PwHash;

public class PwHashTest {
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Test
    public void testHashPassword() {
        String pw = "testPassword123";
        String hashedpw = PwHash.createHashedPassword(pw);

        assertNotNull(hashedpw, "The hashed password should not be null");
        assertTrue(encoder.matches(pw, hashedpw),
                "The hashed password should match the original password");
    }

    @Test
    public void testVerifyPassword() {
        String pw = "testPassword123";
        String hashedpw = PwHash.createHashedPassword(pw);

        boolean isValid = PwHash.compareToHashedPassword("testPassword123", hashedpw);
        assertTrue(isValid, "The verification should return true for the correct password");

        boolean isInvalid = PwHash.compareToHashedPassword("wrongPassword", hashedpw);
        assertFalse(isInvalid, "The verification should return false for the incorrect password");
    }
}