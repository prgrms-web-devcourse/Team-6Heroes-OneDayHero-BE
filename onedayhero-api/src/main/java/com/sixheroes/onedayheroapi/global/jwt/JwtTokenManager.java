package com.sixheroes.onedayheroapi.global.jwt;

import com.sixheroes.onedayherocommon.error.ErrorCode;
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
    ) {
        return extractClaims(token).get(
                jwtProperties.getClaimId(),
                Long.class
        );
    }

    public String getRole(
            String token
    ) {
        return extractClaims(token).get(
                jwtProperties.getClaimRole(),
                String.class
        );
    }

    private Claims extractClaims(
            String token
    ) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getKey(jwtProperties.getSecretKey()))
                    .build()
                    .parseClaimsJws(token).getBody();
        } catch (MalformedJwtException exception) {
            log.warn("잘못된 형식의 JWT 토큰입니다.");

            throw new IllegalStateException(ErrorCode.T_001.name());
        } catch (ExpiredJwtException exception) {
            log.warn("만료된 JWT 토큰입니다.");

            throw new IllegalStateException(ErrorCode.T_001.name());
        } catch (JwtException exception) {
            log.warn("JWT 토큰 에러 발생");

            throw new IllegalStateException(ErrorCode.T_001.name());
        }
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
