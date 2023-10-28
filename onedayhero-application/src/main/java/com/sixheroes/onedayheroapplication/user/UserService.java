package com.sixheroes.onedayheroapplication.user;

import com.sixheroes.onedayheroapplication.user.request.UserServiceUpdateRequest;
import com.sixheroes.onedayheroapplication.user.response.UserUpdateResponse;
import com.sixheroes.onedayherocommon.error.ErrorCode;
import com.sixheroes.onedayherodomain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public UserUpdateResponse updateUser(UserServiceUpdateRequest userServiceUpdateRequest) {
        var userId = userServiceUpdateRequest.userId();
        var user = userRepository.findById(userId)
                            .orElseThrow(() -> {
                                log.debug("존재하지 않는 유저 아이디입니다. userId : {}", userId);
                                return new NoSuchElementException(ErrorCode.EUC_001.name());
                            });

        var userBasicInfo = userServiceUpdateRequest.toUserBasicInfo();
        var userFavoriteWorkingDay = userServiceUpdateRequest.toUserFavoriteWorkingDay();

        user.updateUser(userBasicInfo, userFavoriteWorkingDay);

        return UserUpdateResponse.from(
            user.getId(),
            user.getUserBasicInfo(),
            user.getUserFavoriteWorkingDay()
        );
    }
}
