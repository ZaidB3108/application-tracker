package com.basri.applicationtracker.auth;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class JwtService {
    private final SecretKey key;
    private final long expirationHours;
    public JwtService(@Value("${app.jwt.secret}") String secret, @Value("${app.jwt.expiration-hours}") long expirationHours) {
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret)); this.expirationHours = expirationHours;
    }
    public String createToken(Long userId, String email) {
        Instant now = Instant.now();
        return Jwts.builder().subject(userId.toString()).claim("email", email).issuedAt(java.util.Date.from(now))
            .expiration(java.util.Date.from(now.plus(expirationHours, ChronoUnit.HOURS))).signWith(key).compact();
    }
    public Long userId(String token) { return Long.valueOf(Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload().getSubject()); }
}
