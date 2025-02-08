package com.certTrack.ProgressTrackingService.Security;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

@Component
public class TokenGenerator {
    @Autowired
    private JWTProperties jwtProperties;

    public String generateServiceToken(int userId) {
        return JWT.create()
                .withSubject(userId+"")
                .withExpiresAt(new Date(System.currentTimeMillis() + 3600 * 1000)) // 1 година
				.withClaim("e", "dima6836753@gmail.com")
				.withClaim("a", List.of("ROLE_SERVICE"))
                .sign(Algorithm.HMAC256(jwtProperties.getSecretKey()));
    }
}

