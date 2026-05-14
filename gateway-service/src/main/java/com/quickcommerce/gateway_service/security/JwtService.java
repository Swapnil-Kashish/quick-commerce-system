package com.quickcommerce.gateway_service.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    public boolean isTokenValid(
            String token
    ) {
        try {
            extractEmail(token);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public String extractEmail(
            String token
    ) {
        Key key = Keys.hmacShaKeyFor(
                secret.getBytes()
        );
        return Jwts.parser()
                .verifyWith((SecretKey) key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }
}