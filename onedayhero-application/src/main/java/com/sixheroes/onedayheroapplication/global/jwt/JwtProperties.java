package com.sixheroes.onedayheroapplication.global.jwt;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

    private final String secretKey;
    private final Long accessTokenExpiryTimeMs;
    private final Long refreshTokenExpiryTimeMs;
    private final String claimId;
    private final String claimRole;
}
