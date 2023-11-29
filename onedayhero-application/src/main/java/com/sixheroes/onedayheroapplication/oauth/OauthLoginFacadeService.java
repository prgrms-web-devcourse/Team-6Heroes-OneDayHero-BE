package com.sixheroes.onedayheroapplication.oauth;


import com.sixheroes.onedayheroapplication.auth.TokenService;
import com.sixheroes.onedayheroapplication.global.jwt.JwtTokenManager;
import com.sixheroes.onedayheroapplication.oauth.response.LoginResponse;
import com.sixheroes.onedayherocommon.error.ErrorCode;
import com.sixheroes.onedayherocommon.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class OauthLoginFacadeService {

    private final List<OauthLoginService> oauthLoginServices;
    private final JwtTokenManager jwtTokenManager;
    private final TokenService tokenService;

    public LoginResponse login(
            String authorizationServer,
            String code
    ) {
        for (OauthLoginService oauthLoginService : oauthLoginServices) {
            if (oauthLoginService.supports(authorizationServer)) {
                var userAuthResponse = oauthLoginService.login(code);

                var accessToken = jwtTokenManager.generateAccessToken(
                        userAuthResponse.userId(),
                        userAuthResponse.userRole()
                );

                var refreshToken = tokenService.generateRefreshToken(
                        userAuthResponse.userId()
                );

                return LoginResponse.of(
                        userAuthResponse.userId(),
                        userAuthResponse.signUp(),
                        accessToken,
                        refreshToken
                );
            }
        }

        throw new BusinessException(ErrorCode.LOGIN_FAIL);
    }
}
