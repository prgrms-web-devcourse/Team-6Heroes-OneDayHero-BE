package com.sixheroes.onedayheroapi.auth;

import com.sixheroes.onedayheroapi.global.argumentsresolver.authuser.AuthUser;
import com.sixheroes.onedayheroapplication.oauth.OauthProperties;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RequiredArgsConstructor
@RequestMapping("/api/v1/login")
@RestController
public class LoginPageRedirectController {

    private final OauthProperties oauthKakaoProperties;

    //TODO: LoginPageRedirectController 프론트 연동 테스트 성공 시 삭제 예정
    @GetMapping("/kakao")
    public void loginKakao(
            HttpServletResponse httpServletResponse
    ) throws IOException {
        httpServletResponse.sendRedirect(oauthKakaoProperties.getKakao().getLoginPageRedirectUri());
    }

    @GetMapping("/test")
    public String test(@AuthUser Long userId) {
        return "[TEST] userId: " + userId;
    }
}
