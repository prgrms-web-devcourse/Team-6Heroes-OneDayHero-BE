package com.sixheroes.onedayheroapplication.oauth;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "oauth")
public class OauthProperties {

    private final Kakao kakao;

    @Getter
    @RequiredArgsConstructor
    public static class Kakao {
        private final String authorizationServer;
        private final String clientId;
        private final String redirectUri;
        private final String loginPageRedirectUri;
    }
}
