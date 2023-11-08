package com.sixheroes.onedayheroapi.global.configuration;

import com.sixheroes.onedayheroapi.global.auth.AuthUserArgumentResolver;
import com.sixheroes.onedayheroapi.global.interceptor.JwtAuthInterceptor;
import com.sixheroes.onedayheroapi.global.jwt.JwtProperties;
import com.sixheroes.onedayheroapi.global.jwt.JwtTokenManager;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Profile("!test")
@RequiredArgsConstructor
@Configuration
public class JwtWebConfiguration implements WebMvcConfigurer {

    private final JwtProperties jwtProperties;
    private final JwtTokenManager jwtTokenManager;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new AuthUserArgumentResolver(jwtProperties));
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new JwtAuthInterceptor(jwtProperties, jwtTokenManager))
                .order(1)
                .addPathPatterns("/test")
                .excludePathPatterns("/api/v1/auth/*/login");
    }
}
