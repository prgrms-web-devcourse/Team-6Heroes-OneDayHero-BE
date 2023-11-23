package com.sixheroes.onedayherochat.application.infra.redis.properties;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Slf4j
@Getter
@ConfigurationProperties(prefix = "spring.data.redis")
public class RedisProperties {

    private final String host;
    private final Integer port;
    
    public RedisProperties(
            String host,
            Integer port
    ) {
        this.host = host;
        this.port = port;
    }
}
