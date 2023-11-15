package com.sixheroes.onedayherochat.application.infra.redis.configuration;

import com.sixheroes.onedayherochat.application.infra.redis.properties.RedisProperties;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import redis.embedded.RedisServer;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Configuration
@Profile("dev")
public class EmbeddedRedisConfiguration {

    private final RedisProperties properties;
    private RedisServer redisServer;


    @PostConstruct
    public void startRedisServer() throws IOException {
        log.info("port : {} ", properties.getPort());
        redisServer = new RedisServer(properties.getPort());
        redisServer.start();
    }

    @PreDestroy
    public void stopRedisServer() {
        if (redisServer != null) {
            redisServer.stop();
        }
    }
}
