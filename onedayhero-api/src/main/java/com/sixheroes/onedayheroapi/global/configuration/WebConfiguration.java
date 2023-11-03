package com.sixheroes.onedayheroapi.global.configuration;

import com.sixheroes.onedayheroapi.global.jwt.AuthArgumentResolver;
import com.sixheroes.onedayheroapi.global.interceptor.JwtAuthInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new AuthArgumentResolver());
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new JwtAuthInterceptor())
                .order(1)
                .addPathPatterns("/test")
                .excludePathPatterns("/login_test");
    }
}
