package com.sixheroes.onedayheroapplication.auth.infra;


import com.sixheroes.onedayheroapplication.auth.infra.repository.RefreshTokenRepository;
import com.sixheroes.onedayheroapplication.global.jwt.JwtProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;


@RequiredArgsConstructor
@Service
public class AuthService {

    private final JwtProperties jwtProperties;
    private final RefreshTokenRepository refreshTokenRepository;

    public String generateRefreshToken(
            Long userId
    ) {
        var key = RefreshTokenGenerator.generate();
        var value = RefreshTokenSerializer.serialize(RefreshToken.issue(userId));
        var duration = Duration.ofMillis(jwtProperties.getRefreshTokenExpiryTimeMs());

        refreshTokenRepository.save(key, value, duration);

        return key;
    }

    public RefreshToken findRefreshToken(
            String key
    ) {
        return RefreshTokenSerializer.deserialize(refreshTokenRepository.find(key));
    }
}
