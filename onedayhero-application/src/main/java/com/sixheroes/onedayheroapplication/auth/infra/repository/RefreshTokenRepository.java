package com.sixheroes.onedayheroapplication.auth.infra.repository;


import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.TimeUnit;


@Repository
public class RefreshTokenRepository {

    private final RedisTemplate<String, String> redisTemplate;

    public RefreshTokenRepository(
            @Qualifier("refreshTokenRedisTemplate") RedisTemplate<String, String> redisTemplate
    ) {
        this.redisTemplate = redisTemplate;
    }

    public void save(
            String key,
            String value,
            Duration duration
    ) {
        redisTemplate.opsForValue().set(key, value, duration);
    }

    public Optional<String> find(
            String key
    ) {
        return Optional.ofNullable(redisTemplate.opsForValue().get(key));
    }

    public void delete(
            String key
    ) {
        redisTemplate.opsForValue().getAndDelete(key);
    }

    public void update(
            String key,
            String newValue,
            Duration duration
    ) {
        redisTemplate.opsForValue().set(key, newValue, duration);
    }

    public Long getExpireMsTime(
            String key
    ) {
        return redisTemplate.getExpire(
                key,
                TimeUnit.MILLISECONDS
        );
    }
}
