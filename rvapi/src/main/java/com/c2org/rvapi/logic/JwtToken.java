package com.c2org.rvapi.logic;

import java.util.Date;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

public class JwtToken {
    private static final String SECRET = "your-secret-key";

    public static String generateToken(String userId, String email) {
        Algorithm algorithm = Algorithm.HMAC256(SECRET);

        String token = JWT.create()
                .withIssuer("your-issuer")
                .withSubject(userId)
                .withClaim("email", email)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + 3600_000))
                .sign(algorithm);

        return token;
    }
}
