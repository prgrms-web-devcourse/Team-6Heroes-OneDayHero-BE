package com.sixheroes.onedayheroapplication.global.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;


@Slf4j
@RequiredArgsConstructor
public class JwtTokenManager {

    private final JwtProperties jwtProperties;

    public Long getId(
            String token
    ) throws JwtException{
        return extractClaims(token).get(
                jwtProperties.getClaimId(),
                Long.class
        );
    }

    public String getRole(
            String token
    ) throws JwtException {
        return extractClaims(token).get(
                jwtProperties.getClaimRole(),
                String.class
        );
    }

    private Claims extractClaims(
            String token
    ) throws JwtException {
        return Jwts.parserBuilder()
                    .setSigningKey(getKey(jwtProperties.getSecretKey()))
                    .build()
                    .parseClaimsJws(token).getBody();
    }

    public String generateAccessToken(
            Long id,
            String role
    ) {
        var currentTimeMillis = System.currentTimeMillis();

        Claims claims = Jwts.claims();
        claims.put(jwtProperties.getClaimId(), id);
        claims.put(jwtProperties.getClaimRole(), role);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(currentTimeMillis))
                .setExpiration(new Date(currentTimeMillis + jwtProperties.getAccessTokenExpiryTimeMs()))
                .signWith(getKey(jwtProperties.getSecretKey()), SignatureAlgorithm.HS256)
                .compact();
    }

    private static Key getKey(String key) {
        return Keys.hmacShaKeyFor(
                key.getBytes(StandardCharsets.UTF_8)
        );
    }
}
