package com.c2org.rvapi.logic;

import java.util.Date;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

public class JwtToken {
    private static final String SECRET = "your-secret-key";
    private static final String issuer = "your-issuer";

    // encode jwt
    public static String generateToken(String userId, String email) {
        Algorithm algorithm = Algorithm.HMAC256(SECRET);

        String token = JWT.create()
                .withSubject(userId)
                .withIssuer(issuer)
                .withClaim("email", email)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + 3600_000))
                .sign(algorithm);
        return token;
    }

    // decode jwt
    public static String matchUser(String token) {
        Algorithm algorithm = Algorithm.HMAC256(SECRET);

        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer(issuer)
                .build();

        DecodedJWT jwt = verifier.verify(token);
        return jwt.getSubject();
    }
}
