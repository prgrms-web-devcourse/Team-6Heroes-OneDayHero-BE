package com.sixheroes.onedayheroapi.global.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    private static final String FRONT_LOCAL = "http://localhost:3000";
    private static final String FRONT_DEV = "https://team-6-heroes-one-day-hero-fe-git-develop-howon-shin.vercel.app";
    private static final String FRONT_PROD = "https://one-day-hero.vercel.app/";
    private static final Integer PRE_FLIGHT_CACHE_TIME = 120;

    @Override
    public void addCorsMappings(
            CorsRegistry registry
    ) {
        registry.addMapping("/**")
                .allowedOrigins(FRONT_LOCAL, FRONT_DEV, FRONT_PROD)
                .allowedMethods(
                        HttpMethod.GET.name(),
                        HttpMethod.POST.name(),
                        HttpMethod.PATCH.name(),
                        HttpMethod.DELETE.name(),
                        HttpMethod.PUT.name()
                )
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(PRE_FLIGHT_CACHE_TIME);
    }
}
