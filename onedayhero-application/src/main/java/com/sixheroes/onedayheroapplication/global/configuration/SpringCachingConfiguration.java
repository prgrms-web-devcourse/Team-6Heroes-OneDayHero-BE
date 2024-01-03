package com.sixheroes.onedayheroapplication.global.configuration;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.List;
import java.util.concurrent.TimeUnit;

@EnableCaching
@EnableScheduling
@Configuration
public class SpringCachingConfiguration {

    private static final Long ONE_HOUR = 1L;
    private static final Long MAX_SIZE = 1000L;
    private static final String CACHE_NAME = "heroesRank";

    @Bean
    public CacheManager cacheManager() {
        SimpleCacheManager simpleCacheManager = new SimpleCacheManager();
        simpleCacheManager.setCaches(List.of(new ConcurrentMapCache("regions"), new ConcurrentMapCache("alarmTemplates")));
        simpleCacheManager.setCaches(List.of(new CaffeineCache(CACHE_NAME, Caffeine.newBuilder()
            .recordStats()
            .expireAfterWrite(ONE_HOUR, TimeUnit.HOURS)
            .maximumSize(MAX_SIZE)
            .build())));

        return simpleCacheManager;
    }
}
