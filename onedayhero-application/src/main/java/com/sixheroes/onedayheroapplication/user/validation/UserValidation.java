package com.sixheroes.onedayheroapplication.user.validation;

import com.sixheroes.onedayherocommon.error.ErrorCode;
import com.sixheroes.onedayherocommon.exception.BusinessException;
import com.sixheroes.onedayherodomain.user.User;
import com.sixheroes.onedayherodomain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Component
public class UserValidation {

    private final UserRepository userRepository;

    public void validUserNickname(
        Long userId,
        String nickname
    ) {
        var user = userRepository.findByUserBasicInfoNickname(nickname);
        if (user.isPresent() && !Objects.equals(user.map(User::getId).orElse(null), userId)) {
            throw new BusinessException(ErrorCode.DUPLICATED_NICKNAME);
        }
    }
}
