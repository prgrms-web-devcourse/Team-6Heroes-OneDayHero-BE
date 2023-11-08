package com.sixheroes.onedayheroapplication.global.oauth.kakao;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "oauth.kakao")
public class OauthKakaoProperties {

    private final String clientId;
    private final String redirectUri;
    private final String loginPageRedirectUri;
}
