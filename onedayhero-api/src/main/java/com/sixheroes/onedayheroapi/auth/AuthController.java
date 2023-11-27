package com.sixheroes.onedayheroapi.auth;

import com.sixheroes.onedayheroapi.auth.response.oauth.LoginRequest;
import com.sixheroes.onedayheroapplication.auth.TokenService;
import com.sixheroes.onedayheroapplication.auth.reponse.ReissueTokenResponse;
import com.sixheroes.onedayheroapplication.oauth.OauthProperties;
import com.sixheroes.onedayheroapplication.oauth.response.LoginResponse;
import com.sixheroes.onedayheroapi.global.response.ApiResponse;
import com.sixheroes.onedayheroapplication.oauth.OauthLoginFacadeService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final TokenService tokenService;

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

    @GetMapping("/reissue-token")
    public ResponseEntity<ApiResponse<ReissueTokenResponse>> reissueToken(
            @CookieValue(value = REFRESH_TOKEN) String refreshToken,
            HttpServletResponse response
    ) {
        var refreshTokenValue = tokenService.findRefreshTokenValue(refreshToken);

        if (refreshTokenValue.isEmpty()) {
            removeCookie(response);

            return ResponseEntity.ok(ApiResponse.ok(ReissueTokenResponse.fail()));
        }

        var reissueTokenResponse = tokenService.rotation(
                refreshToken,
                refreshTokenValue.get()
        );
        setCookie(response, reissueTokenResponse.refreshToken());

        return ResponseEntity.ok(ApiResponse.ok(reissueTokenResponse));
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
        Cookie cookie = new Cookie(REFRESH_TOKEN, refreshToken);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
    }

    private void removeCookie(
            HttpServletResponse response
    ) {
        Cookie cookie = new Cookie(REFRESH_TOKEN, null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}
