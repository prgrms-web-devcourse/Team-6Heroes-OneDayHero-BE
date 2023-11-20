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
        System.out.println("로그인1");
        var loginResponse = oauthLoginFacadeService.login(
                oauthProperties.getKakao().getAuthorizationServer(),
                loginRequest.code()
        );
        return ResponseEntity.ok(ApiResponse.ok(loginResponse));
    }


    @GetMapping("/kakao/callback")
    public ResponseEntity<ApiResponse<LoginResponse>> callback(
            @RequestParam String code
    ) {
        return ResponseEntity.ok(ApiResponse.ok(new LoginResponse(1L, code)));
    }
}
