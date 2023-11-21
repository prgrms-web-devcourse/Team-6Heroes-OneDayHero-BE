package com.sixheroes.onedayheroapi.auth;

import com.sixheroes.onedayheroapi.auth.response.oauth.LoginRequest;
import com.sixheroes.onedayheroapplication.oauth.OauthProperties;
import com.sixheroes.onedayheroapplication.oauth.response.LoginResponse;
import com.sixheroes.onedayheroapi.global.response.ApiResponse;
import com.sixheroes.onedayheroapplication.oauth.OauthLoginFacadeService;
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

    private final OauthProperties oauthProperties;
    private final OauthLoginFacadeService oauthLoginFacadeService;

    @PostMapping("/kakao/login")
    public ResponseEntity<ApiResponse<LoginResponse>> loginKakao(
            @Valid @RequestBody LoginRequest loginRequest
    ) {
        var loginResponse = oauthLoginFacadeService.login(
                oauthProperties.getKakao().getAuthorizationServer(),
                loginRequest.code()
        );

        if (loginResponse.signUp()) {
            //유저 정보 입력 페이지 생성 후 URI 설정
            return ResponseEntity.created(URI.create("")).body(ApiResponse.created(loginResponse));
        }

        return ResponseEntity.ok(ApiResponse.ok(loginResponse));
    }
}
