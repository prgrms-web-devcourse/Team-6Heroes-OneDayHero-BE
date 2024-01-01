package com.sixheroes.onedayheroapplication.user.reader;

import com.sixheroes.onedayheroapplication.user.repository.UserQueryRepository;
import com.sixheroes.onedayheroapplication.user.repository.dto.HeroRankQueryResponse;
import com.sixheroes.onedayherocommon.error.ErrorCode;
import com.sixheroes.onedayherocommon.exception.EntityNotFoundException;
import com.sixheroes.onedayherodomain.user.User;
import com.sixheroes.onedayherodomain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Component
public class UserReader {

    private static final String SEARCH = "%%%s%%";

    private final UserRepository userRepository;
    private final UserQueryRepository userQueryRepository;

    public User findOne(
            Long userId
    ) {
        return userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.debug("존재하지 않는 유저 아이디입니다. id : {}", userId);
                    return new EntityNotFoundException(ErrorCode.INVALID_REQUEST_VALUE);
                });
    }

    public Slice<User> findHeroes(
            String nickname,
            Pageable pageable
    ) {
        return userRepository.findByNicknameAndIsHeroMode(SEARCH.formatted(nickname), pageable);
    }

    public List<HeroRankQueryResponse> findHeroesRank(
        Long regionId,
        Long missionCategoryId,
        Pageable pageable
    ) {
        return userQueryRepository.findHeroesRank(regionId, missionCategoryId, pageable);
    }
}