package com.sixheroes.onedayheroapplication.auth;


import com.sixheroes.onedayheroapplication.auth.infra.repository.BlacklistRepository;
import com.sixheroes.onedayheroapplication.global.jwt.JwtTokenManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;

@RequiredArgsConstructor
@Service
public class BlacklistService {

    private final BlacklistRepository blacklistRepository;
    private final JwtTokenManager jwtTokenManager;

    public Boolean check(
            String accessToken
    ) {
        return blacklistRepository.exist(accessToken);
    }

    public void addBlacklist(
            Long userId,
            String accessToken
    ) {
        var remainExpiryTimeMs = jwtTokenManager.calculateRemainExpiryTimeMs(accessToken);
        var duration = Duration.ofMillis(remainExpiryTimeMs);

        blacklistRepository.save(accessToken, String.valueOf(userId), duration);
    }
}
