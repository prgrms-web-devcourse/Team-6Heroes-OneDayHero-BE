package com.sixheroes.onedayheroapi.auth;

import com.sixheroes.onedayheroapi.auth.response.oauth.KakaoAuthorizationCodeResponse;
import com.sixheroes.onedayheroapplication.oauth.OauthProperties;
import com.sixheroes.onedayheroapplication.oauth.response.LoginResponse;
import com.sixheroes.onedayheroapi.global.response.ApiResponse;
import com.sixheroes.onedayheroapplication.oauth.OauthLoginFacadeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@RestController
public class AuthController {

    private final OauthProperties oauthProperties;
    private final OauthLoginFacadeService oauthLoginFacadeService;

    @GetMapping("/kakao/callback")
    public ResponseEntity<ApiResponse<LoginResponse>> loginKakao(
            @ModelAttribute KakaoAuthorizationCodeResponse kakaoAuthorizationCodeResponse
    ) {
        var loginResponse = oauthLoginFacadeService.login(
                oauthProperties.getKakao().getAuthorizationServer(),
                kakaoAuthorizationCodeResponse.code()
        );

        return ResponseEntity.ok(ApiResponse.ok(loginResponse));
    }
}
