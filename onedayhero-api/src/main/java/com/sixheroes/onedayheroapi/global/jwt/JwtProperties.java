package com.sixheroes.onedayheroapi.global.jwt;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Profile;

@Profile("!test")
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
