package com.team2.online_examination.utils;

import com.team2.online_examination.dtos.JwtPayload;
import com.team2.online_examination.dtos.responses.JwtToken;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

@Component
public class JwtUtil {

    private SecretKey secretKey;

    @Value("${jwt.secret-access-token}")
    private String secret;

    @PostConstruct
    private void init() {
        if (secret.length() < 32) {
            throw new IllegalArgumentException("JWT secret must be at least 32 characters long");
        }
        secretKey = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public JwtToken generateToken(JwtPayload payload) {
        Map<String, Object> claims = payload.getClaims();
        String accessToken = Jwts.builder()
                .claims(claims)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000))
                .signWith(secretKey)
                .compact();

        String refreshToken = Jwts.builder()
                .claims(claims)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 30 * 24 * 60 * 60 * 1000))
                .signWith(secretKey)
                .compact();

        JwtToken jwtToken = new JwtToken();
        jwtToken.setAccess_token(accessToken);
        jwtToken.setRefresh_token(refreshToken);

        return jwtToken;
    }

    public JwtPayload extractPayload(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        JwtPayload payload = new JwtPayload();
        claims.forEach(payload::addClaim);

        return payload;
    }
}
