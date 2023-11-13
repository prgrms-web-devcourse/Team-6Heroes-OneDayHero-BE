package com.sixheroes.onedayheroapplication.oauth;


import com.sixheroes.onedayheroapplication.global.jwt.JwtTokenManager;
import com.sixheroes.onedayheroapplication.oauth.response.LoginResponse;
import com.sixheroes.onedayheroapplication.user.response.UserAuthResponse;
import com.sixheroes.onedayherocommon.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class OauthLoginFacadeService {

    private final List<OauthLoginService> oauthLoginServices;
    private final JwtTokenManager jwtTokenManager;

    public LoginResponse login(
            String authorizationServer,
            String code
    ) {
        for (OauthLoginService oauthLoginService : oauthLoginServices) {
            if (oauthLoginService.supports(authorizationServer)) {
                var userAuthResponse = oauthLoginService.login(code);

                return new LoginResponse(
                        userAuthResponse.userId(),
                        getAccessToken(userAuthResponse)
                );
            }
        }

        throw new IllegalStateException(ErrorCode.S_001.name());
    }

    private String getAccessToken(UserAuthResponse userAuthResponse) {
        return jwtTokenManager.generateAccessToken(
                userAuthResponse.userId(),
                userAuthResponse.userRole()
        );
    }
}
