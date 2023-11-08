package com.sixheroes.onedayheroapi.global.configuration;

import com.sixheroes.onedayheroapi.global.interceptor.JwtAuthInterceptor;
import com.sixheroes.onedayheroapi.global.jwt.JwtProperties;
import com.sixheroes.onedayheroapi.global.jwt.JwtTokenManager;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@EnableConfigurationProperties(JwtProperties.class)
@Configuration
public class JwtConfiguration {

    private final JwtProperties jwtProperties;

    @Bean
    public JwtAuthInterceptor jwtAuthInterceptor() {
        return new JwtAuthInterceptor(
                jwtProperties,
                jwtTokenManager()
        );
    }

    @Bean
    public JwtTokenManager jwtTokenManager() {
        return new JwtTokenManager(jwtProperties);
    }
}

