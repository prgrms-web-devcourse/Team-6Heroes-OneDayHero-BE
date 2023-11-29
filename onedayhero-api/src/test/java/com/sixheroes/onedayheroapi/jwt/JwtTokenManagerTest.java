package com.sixheroes.onedayheroapi.jwt;

import com.sixheroes.onedayheroapplication.global.jwt.JwtProperties;
import com.sixheroes.onedayheroapplication.global.jwt.JwtTokenManager;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JwtTokenManagerTest {

    private final static String INVALID_FORMAT_ACCESS_TOKEN = "esJhbGciOiJIUzI1NiJ9.eyJpZCI6MSwicm9sZSI6Ik1FTUJFUiIsImlhdCI6MTY5ODkxMTkyNCwiZXwIjoxNjk4OTEyNTI0fQ.k-sTH0U_HM3lv7augTF2Gx0497DKzDZiyRMzv_QZObQ";
    private final static String TEST_SECRET_KEY = "testtesttesttesttesttesttesttesttesttesttesttesttesttest";
    private final static Long TEST_EXPIRY_TIME_MS = 60000000000L;
    private final static Long TEST_EXPIRY_TIME_MS2 = 60000L;
    private final static Long TEST_SHORT_EXPIRY_TIME_MS = 1L;

    @DisplayName("생성된 액세스토큰 PAYLOAD 에 유저 아이디, 유저 권한이 존재한다.")
    @Test
    void validateTokenHasValidPayLoad() {
        // given
        var userId = 1L;
        var userRole = "MEMBER";

        var jwtTokenManager = new JwtTokenManager(createProperties(TEST_EXPIRY_TIME_MS));
        var accessToken = jwtTokenManager.generateAccessToken(
                userId,
                userRole
        );

        // when
        var userIdFromAccessToken = jwtTokenManager.getId(accessToken);
        var userRoleFromAccessToken = jwtTokenManager.getRole(accessToken);

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            assertThat(userId).isEqualTo(userIdFromAccessToken);
            assertThat(userRole).isEqualTo(userRoleFromAccessToken);
        });
    }

    @DisplayName("잘못된 형식의 액세스토큰을 검증할 수 있다.")
    @Test
    void validateMalformedToken() {
        var jwtTokenManager = new JwtTokenManager(createProperties(TEST_EXPIRY_TIME_MS));

        // when & then
        assertThatThrownBy(() ->
                jwtTokenManager.getRole(INVALID_FORMAT_ACCESS_TOKEN)
        ).isInstanceOf(MalformedJwtException.class);
    }

    @DisplayName("만료된 액세스토큰을 검증할 수 있다.")
    @Test
    void validateExpiredToken() {
        // given
        var userId = 1L;
        var userRole = "MEMBER";
        var jwtTokenManager = new JwtTokenManager(createProperties(TEST_SHORT_EXPIRY_TIME_MS));
        var expiredAccessToken = jwtTokenManager.generateAccessToken(
                userId,
                userRole
        );

        // when & then
        assertThatThrownBy(() ->
                jwtTokenManager.getId(expiredAccessToken)
        ).isInstanceOf(ExpiredJwtException.class);
    }

    @DisplayName("액세스 토큰이 만료까지 남은 시간을 계산할 수 있다.")
    @Test
    void calculateTokenExpiredTimeMs() {
        // given
        var userId = 1L;
        var userRole = "MEMBER";

        var jwtTokenManager = new JwtTokenManager(createProperties(TEST_EXPIRY_TIME_MS2));
        var accessToken = jwtTokenManager.generateAccessToken(
                userId,
                userRole
        );

        // when
        var remainExpiryTimeMs = jwtTokenManager.calculateRemainExpiryTimeMs(accessToken);

        // then
        assertThat(remainExpiryTimeMs).isGreaterThan(0L);
    }

    private JwtProperties createProperties(Long accessTokenExpiryTime) {
        return new JwtProperties(
                TEST_SECRET_KEY,
                accessTokenExpiryTime,
                1L,
                "id",
                "role"
                );
    }
}
