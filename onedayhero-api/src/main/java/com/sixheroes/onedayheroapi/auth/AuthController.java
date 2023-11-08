package com.sixheroes.onedayheroapi.auth;

import com.sixheroes.onedayheroapi.auth.response.LoginResponse;
import com.sixheroes.onedayheroapi.global.jwt.JwtTokenManager;
import com.sixheroes.onedayheroapi.global.response.ApiResponse;
import com.sixheroes.onedayheroapplication.auth.LoginService;
import com.sixheroes.onedayheroapplication.global.oauth.kakao.KakaoOauthClient;
import com.sixheroes.onedayheroapplication.global.oauth.kakao.OauthKakaoProperties;
import com.sixheroes.onedayheroapplication.global.oauth.kakao.response.KakaoAuthorizationCodeResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@RestController
public class AuthController {

    private final OauthKakaoProperties oauthKakaoProperties;
    private final KakaoOauthClient kakaoOauthClient;
    private final LoginService loginService;
    private final JwtTokenManager jwtTokenManager;

    @GetMapping("/kakao/login")
    public void loginKakao(
            HttpServletResponse httpServletResponse
    ) throws IOException {
        httpServletResponse.sendRedirect(oauthKakaoProperties.getLoginPageRedirectUri());
    }

    @GetMapping("/kakao/callback")
    public ResponseEntity<ApiResponse<LoginResponse>> callBackFromKakao(
            @ModelAttribute KakaoAuthorizationCodeResponse response
    ) {
        var accessToken = kakaoOauthClient.requestToken(response.code());
        var userEmail = kakaoOauthClient.requestResource(accessToken);
        var userLoginResponse = loginService.login(
                kakaoOauthClient.getSocialType(),
                userEmail
        );
        var jwtAccessToken = jwtTokenManager.generateAccessToken(
                userLoginResponse.userId(),
                userLoginResponse.userRole()
        );
        var loginResponse = LoginResponse.builder()
                .userId(userLoginResponse.userId())
                .accessToken(jwtAccessToken)
                .build();

        return ResponseEntity.ok(ApiResponse.ok(loginResponse));
    }
}
