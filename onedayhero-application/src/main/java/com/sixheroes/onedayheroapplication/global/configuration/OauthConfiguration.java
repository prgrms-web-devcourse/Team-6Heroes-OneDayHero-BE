package com.sixheroes.onedayheroapplication.global.configuration;

import com.sixheroes.onedayheroapplication.global.oauth.kakao.OauthKakaoProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@EnableConfigurationProperties(OauthKakaoProperties.class)
@Configuration
public class OauthConfiguration {
}
