package com.sixheroes.onedayherocommon.jwt;

import com.sixheroes.onedayherocommon.error.ErrorCode;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JwtTokenUtilsTest {

    private final static String INVALID_FORMAT_ACCESS_TOKEN = "esJhbGciOiJIUzI1NiJ9.eyJpZCI6MSwicm9sZSI6Ik1FTUJFUiIsImlhdCI6MTY5ODkxMTkyNCwiZXwIjoxNjk4OTEyNTI0fQ.k-sTH0U_HM3lv7augTF2Gx0497DKzDZiyRMzv_QZObQ";
    private final static String TEST_SECRET_KEY = "EENY5W0eegTf1naQB2eDeaaaRS2b8xa5c4qLdS0hmVjtbvo8tOyhPMcAmtPuQ";
    private final static Long TEST_EXPIRY_TIME_MS = 600000L;
    private final static Long TEST_SHORT_EXPIRY_TIME_MS = 1L;

    @DisplayName("생성된 액세스토큰 PAYLOAD 에 유저 아이디, 유저 권한이 존재한다.")
    @Test
    void validateTokenHasValidPayLoad() {
        // given
        var userId = 1L;
        var userRole = "MEMBER";
        var accessToken = JwtTokenUtils.generateAccessToken(
                userId,
                userRole,
                TEST_SECRET_KEY,
                TEST_EXPIRY_TIME_MS
        );

        // when
        var userIdFromAccessToken = JwtTokenUtils.getId(
                accessToken,
                TEST_SECRET_KEY
        );
        var userRoleFromAccessToken = JwtTokenUtils.getRole(
                accessToken,
                TEST_SECRET_KEY
        );

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            assertThat(userId).isEqualTo(userIdFromAccessToken);
            assertThat(userRole).isEqualTo(userRoleFromAccessToken);
        });
    }

    @DisplayName("잘못된 형식의 액세스토큰을 검증할 수 있다.")
    @Test
    void validateMalformedToken() {
        // when & then
        assertThatThrownBy(() ->
                JwtTokenUtils.getRole(
                        INVALID_FORMAT_ACCESS_TOKEN,
                        TEST_SECRET_KEY
                )
        )
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(ErrorCode.T_001.name());
    }

    @DisplayName("만료된 액세스토큰을 검증할 수 있다.")
    @Test
    void validateExpiredToken() {
        // given
        var userId = 1L;
        var userRole = "MEMBER";
        var expiredAccessToken = JwtTokenUtils.generateAccessToken(
                userId,
                userRole,
                TEST_SECRET_KEY,
                TEST_SHORT_EXPIRY_TIME_MS
        );

        // when & then
        assertThatThrownBy(() ->
                JwtTokenUtils.getId(
                        expiredAccessToken,
                        TEST_SECRET_KEY
                )
        )
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(ErrorCode.T_001.name());
    }
}
