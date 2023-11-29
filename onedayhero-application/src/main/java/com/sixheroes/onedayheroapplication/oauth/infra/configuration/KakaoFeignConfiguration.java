package com.sixheroes.onedayheroapplication.oauth.infra.configuration;

import com.sixheroes.onedayheroapplication.oauth.infra.feign.decoder.LoginErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KakaoFeignConfiguration {

    @Bean
    public LoginErrorDecoder loginErrorDecoder() {
        return new LoginErrorDecoder();
    }
}
