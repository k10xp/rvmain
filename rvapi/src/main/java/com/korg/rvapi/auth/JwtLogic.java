package com.korg.rvapi.auth;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

@Component
public class JwtLogic {
    // import from env file
    private final Environment env;

    @Autowired
    public JwtLogic(Environment env) {
        this.env = env;
    }

    private String getSecret() {
        return env.getProperty("JWT_SECRET");
    }

    private String getIssuer() {
        return env.getProperty("JWT_ISSUER");
    }

    public String encodeJWT(String userId, String email) {
        String secret = getSecret();
        String issuer = getIssuer();

        Algorithm algorithm = Algorithm.HMAC256(secret);

        return JWT.create()
                .withSubject(userId)
                .withIssuer(issuer)
                .withClaim("email", email)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000)) // 1 day
                .sign(algorithm);
    }

    public String decodeJWT(String token) {
        String secret = getSecret();
        String issuer = getIssuer();

        Algorithm algorithm = Algorithm.HMAC256(secret);

        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer(issuer)
                .build();

        DecodedJWT jwt = verifier.verify(token);
        return jwt.getSubject();
    }
}
