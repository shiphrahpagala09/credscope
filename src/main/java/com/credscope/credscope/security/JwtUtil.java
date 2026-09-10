package com.credscope.credscope.security;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {
	 @Value("${jwt.secret}")
	    private String secret;

	    private final long expirationMs = 86400000; // 24 hours

	    private SecretKey getKey() {
	        return Keys.hmacShaKeyFor(secret.getBytes());
	    }

	    public String generateToken(String email, String role) {
	        return Jwts.builder()
	                .subject(email)
	                .claim("role", role)
	                .issuedAt(new Date())
	                .expiration(new Date(System.currentTimeMillis() + expirationMs))
	                .signWith(getKey())
	                .compact();
	    }
	    public boolean validateToken(String token) {
	        try {
	            Jwts.parser().verifyWith(getKey()).build().parseSignedClaims(token);
	            return true;
	        } catch (Exception e) {
	            return false;
	        }
	    }

	    public String extractEmail(String token) {
	        return Jwts.parser().verifyWith(getKey()).build()
	                .parseSignedClaims(token).getPayload().getSubject();
	    }

	    public String extractRole(String token) {
	        return Jwts.parser().verifyWith(getKey()).build()
	                .parseSignedClaims(token).getPayload().get("role", String.class);
	    }

}
