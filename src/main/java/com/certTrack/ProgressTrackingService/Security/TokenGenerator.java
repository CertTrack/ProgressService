package com.certTrack.ProgressTrackingService.Security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

@Component
public class TokenGenerator {
    @Autowired
    private JWTProperties jwtProperties;

    public String generateServiceToken(int userId) {
        Algorithm algorithm = Algorithm.HMAC256(jwtProperties.getSecretKey());
        return JWT.create()
                .withSubject(userId+"")
                .withClaim("roles", "ROLE_SERVICE")
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + 3600 * 1000)) // 1 година
                .sign(algorithm);
    }
}

