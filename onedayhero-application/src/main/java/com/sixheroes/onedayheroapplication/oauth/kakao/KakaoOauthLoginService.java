package com.sixheroes.onedayheroapplication.oauth.kakao;


import com.sixheroes.onedayheroapplication.oauth.OauthLoginService;
import com.sixheroes.onedayheroapplication.oauth.OauthProperties;
import com.sixheroes.onedayheroapplication.user.UserLoginService;
import com.sixheroes.onedayheroapplication.user.response.UserAuthResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@RequiredArgsConstructor
@Service
public class KakaoOauthLoginService implements OauthLoginService {

    private final static String AUTHORIZATION_SERVER = "KAKAO";
    private final KakaoOauthClientService kakaoOauthClient;
    private final UserLoginService loginService;

    @Override
    public Boolean supports(String authorizationServer) {
        return AUTHORIZATION_SERVER.equals(authorizationServer);
    }

    @Override
    public UserAuthResponse login(String code) {
        System.out.println("로그인3");
        var accessToken = kakaoOauthClient.requestToken(code);
        System.out.println("로그인4");
        var userEmail = kakaoOauthClient.requestResource(accessToken);

        System.out.println("로그인5");

        return loginService.login(
                kakaoOauthClient.getSocialType(),
                userEmail
        );
    }
}
