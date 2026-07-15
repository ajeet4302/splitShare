package com.splitwise.security;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    private SecretKey getSignKey() {

        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

    }

    // This will Generate JWT Token
    public String generateToken(String email) {

        Date now = new Date();

        Date expiryDate = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .subject(email)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(getSignKey())
                .compact();

    }

    // Extract Email
    public String extractEmail(String token) {

        return extractAllClaims(token).getSubject();

    }

    // Extract Expiry Date
    public Date extractExpiration(String token) {

        return extractAllClaims(token).getExpiration();

    }

    // Extract Claims
    private Claims extractAllClaims(String token) {

        return Jwts.parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

    }

    // Check Expired
    public boolean isTokenExpired(String token) {

        return extractExpiration(token).before(new Date());

    }

    // Validate Token
    public boolean validateToken(String token, String email) {

        return extractEmail(token).equals(email) && !isTokenExpired(token);

    }

}