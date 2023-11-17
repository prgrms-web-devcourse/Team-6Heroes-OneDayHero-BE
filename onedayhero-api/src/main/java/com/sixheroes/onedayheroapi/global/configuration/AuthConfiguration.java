package com.sixheroes.onedayheroapi.global.configuration;

import com.sixheroes.onedayheroapi.global.argumentsresolver.authuser.AuthUserArgumentResolver;
import com.sixheroes.onedayheroapi.global.interceptor.JwtAuthInterceptor;
import com.sixheroes.onedayheroapplication.global.jwt.JwtProperties;
import com.sixheroes.onedayheroapplication.global.jwt.JwtTokenManager;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Profile("!test")
@RequiredArgsConstructor
@Configuration
public class AuthConfiguration implements WebMvcConfigurer {

    private final JwtProperties jwtProperties;
    private final JwtTokenManager jwtTokenManager;

    @Bean
    public JwtAuthInterceptor jwtAuthInterceptor() {
        return new JwtAuthInterceptor(
                jwtProperties,
                jwtTokenManager
        );
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new AuthUserArgumentResolver(jwtProperties));
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new JwtAuthInterceptor(jwtProperties, jwtTokenManager))
                .order(1)
                .addPathPatterns("/api/v1/login/test")
                .excludePathPatterns("/api/v1/auth/**");
    }
}
