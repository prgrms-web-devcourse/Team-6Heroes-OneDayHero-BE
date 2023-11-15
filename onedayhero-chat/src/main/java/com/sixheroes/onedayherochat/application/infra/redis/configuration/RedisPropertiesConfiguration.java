package com.sixheroes.onedayherochat.application.infra.redis.configuration;

import com.sixheroes.onedayherochat.application.infra.redis.properties.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(RedisProperties.class)
public class RedisPropertiesConfiguration {
}
