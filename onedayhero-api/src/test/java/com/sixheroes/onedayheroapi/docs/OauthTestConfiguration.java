package com.sixheroes.onedayheroapi.docs;

import com.sixheroes.onedayheroapplication.oauth.OauthProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class OauthTestConfiguration {

    @Bean
    public OauthProperties oauthProperties() {
        var kakao = new OauthProperties.Kakao("KAKAO", "clientId", "redirectUri", "redirectURi");

        return new OauthProperties(kakao);
    }
}
