package com.sixheroes.onedayheroapi.auth;

import com.sixheroes.onedayheroapi.auth.response.oauth.LoginRequest;
import com.sixheroes.onedayheroapplication.auth.infra.AuthService;
import com.sixheroes.onedayheroapplication.oauth.OauthProperties;
import com.sixheroes.onedayheroapplication.oauth.response.LoginResponse;
import com.sixheroes.onedayheroapi.global.response.ApiResponse;
import com.sixheroes.onedayheroapplication.oauth.OauthLoginFacadeService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@RestController
public class AuthController {

    public static final String REFRESH_TOKEN = "refreshToken";

    private final OauthProperties oauthProperties;
    private final OauthLoginFacadeService oauthLoginFacadeService;

    @PostMapping("/kakao/login")
    public ResponseEntity<ApiResponse<LoginResponse>> loginKakao(
            @Valid @RequestBody LoginRequest loginRequest,
            HttpServletResponse response
    ) {
        var loginResponse = oauthLoginFacadeService.login(
                oauthProperties.getKakao().getAuthorizationServer(),
                loginRequest.code()
        );
        setCookie(response, loginResponse.refreshToken());

        return loginOrSignUpResponse(loginResponse);
    }

    @PostMapping("/reissue-token")
    public void reIssueToken(
            @CookieValue(value = REFRESH_TOKEN) String refreshToken,
            HttpServletResponse response
    ) {
        //TODO: refreshToken 존재 및 중복 사용 검증
        //TODO: refreshToken 통해 새로운 refreshToken' 생성
        //TODO: refreshToken 토큰과 refreshToken' 에 대해 체인 설정
    }

    private ResponseEntity<ApiResponse<LoginResponse>> loginOrSignUpResponse(LoginResponse loginResponse) {
        if (loginResponse.signUp()) {
            return ResponseEntity.created(URI.create("")).body(ApiResponse.created(loginResponse));
        }

        return ResponseEntity.ok(ApiResponse.ok(loginResponse));
    }

    private void setCookie(
            HttpServletResponse response,
            String refreshToken
    ) {
        Cookie cookie = new Cookie(REFRESH_TOKEN,  refreshToken);
        cookie.setHttpOnly(true);

        response.addCookie(cookie);
    }
}
