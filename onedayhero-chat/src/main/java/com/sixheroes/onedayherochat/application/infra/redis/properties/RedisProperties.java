package com.sixheroes.onedayherochat.application.infra.redis.properties;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties(prefix = "spring.data.redis")
public class RedisProperties {

    private final Integer port;

    /**
     * SpringBoot 3.0 이후로 생성자가 하나라면 @ConstructorBinding 제거 가능
     */
    public RedisProperties(
            Integer port
    ) {
        this.port = port;
    }
}
