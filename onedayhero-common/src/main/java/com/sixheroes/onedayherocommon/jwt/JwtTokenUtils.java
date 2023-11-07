package com.sixheroes.onedayherocommon.jwt;

import com.sixheroes.onedayherocommon.error.ErrorCode;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;


@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class JwtTokenUtils {

    private static final String USER_ID = "id";
    private static final String USER_ROLE = "role";

    public static Long getId(
            String token,
            String key) {
        return extractClaims(
                token,
                key
        ).get(
                USER_ID,
                Long.class
        );
    }

    public static String getRole(
            String token,
            String key
    ) {
        return extractClaims(
                token,
                key
        ).get(
                USER_ROLE,
                String.class
        );
    }

    private static Claims extractClaims(
            String token,
            String key
    ) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getKey(key))
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

    public static String generateAccessToken(
            Long id,
            String role,
            String key,
            long expiredTimeMs
    ) {
        var currentTimeMillis = System.currentTimeMillis();

        Claims claims = Jwts.claims();
        claims.put(USER_ID, id);
        claims.put(USER_ROLE, role);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(currentTimeMillis))
                .setExpiration(new Date(currentTimeMillis + expiredTimeMs))
                .signWith(getKey(key), SignatureAlgorithm.HS256)
                .compact();
    }

    private static Key getKey(String key) {
        return Keys.hmacShaKeyFor(
                key.getBytes(StandardCharsets.UTF_8)
        );
    }
}
