package com.sixheroes.onedayheroapplication.user;

import com.sixheroes.onedayherocommon.error.ErrorCode;
import com.sixheroes.onedayherodomain.user.User;
import com.sixheroes.onedayherodomain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Component
public class UserReader {

    private final UserRepository userRepository;

    public User findOne(
            Long userId
    ) {
        return userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.debug("존재하지 않는 유저 아이디입니다. id : {}", userId);
                    return new NoSuchElementException(ErrorCode.EUC_000.name());
                });
    }

    public User findOneWithUserImage(
        Long userId
    ) {
        return userRepository.findByIdJoinUserImage(userId)
            .orElseThrow(() -> {
                log.debug("존재하지 않는 유저 아이디입니다. id : {}", userId);
                return new NoSuchElementException(ErrorCode.EUC_000.name());
            });
    }
}