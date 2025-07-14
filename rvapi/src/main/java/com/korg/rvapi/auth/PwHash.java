package com.korg.rvapi.auth;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PwHash {
    public static String createHashedPassword(String inputPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(inputPassword);
    }

    public static boolean compareToHashedPassword(String inputPassword, String storedHash) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.matches(inputPassword, storedHash);
    }
}
