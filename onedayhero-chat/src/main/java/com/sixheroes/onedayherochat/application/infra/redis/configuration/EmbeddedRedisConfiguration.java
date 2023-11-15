package com.sixheroes.onedayherochat.application.infra.redis.configuration;

import com.sixheroes.onedayherochat.application.infra.redis.properties.RedisProperties;
import com.sixheroes.onedayherocommon.error.ErrorCode;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import redis.embedded.RedisServer;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Configuration
@Profile("dev")
public class EmbeddedRedisConfiguration {

    private final RedisProperties properties;
    private RedisServer redisServer;

    @PostConstruct
    public void startRedisServer() throws IOException {

        var redisPort = properties.getPort();

        if (isArmMac()) {
            redisServer = new RedisServer(Objects.requireNonNull(getRedisFileForArcMac()),
                    redisPort);
        }

        if (!isArmMac()) {
            redisServer = new RedisServer(redisPort);
        }
        
        redisServer.start();
    }

    @PreDestroy
    public void stopRedisServer() {
        if (redisServer != null) {
            redisServer.stop();
        }
    }

    private boolean isArmMac() {
        return Objects.equals(System.getProperty("os.arch"), "aarch64") &&
                Objects.equals(System.getProperty("os.name"), "Mac OS X");
    }

    private File getRedisFileForArcMac() {
        try {
            return new ClassPathResource("binary/redis/redis-server-7.2.3-mac-arm64").getFile();
        } catch (Exception e) {
            throw new IllegalStateException(ErrorCode.S_001.name(), e);
        }
    }
}
