package com.korg.rvapi.auth;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PwHash {
    // create hashed pw
    public static String hashPassword(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }

    // compare to hash pw
    public static boolean verifyPassword(String inputPassword, String storedHash) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.matches(inputPassword, storedHash);
    }
}
