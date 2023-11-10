package com.sixheroes.onedayheroapplication.user;

import com.sixheroes.onedayheroapplication.user.request.UserServiceUpdateRequest;
import com.sixheroes.onedayheroapplication.user.response.ProfileCitizenResponse;
import com.sixheroes.onedayheroapplication.user.response.ProfileHeroResponse;
import com.sixheroes.onedayheroapplication.user.response.UserResponse;
import com.sixheroes.onedayheroapplication.user.response.UserUpdateResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserService {

    private final UserReader userReader;

    public UserResponse findUser(
        Long userId
    ) {
        throw new UnsupportedOperationException();
    }

    public ProfileCitizenResponse findCitizenProfile(
        Long userId
    ) {
        throw new UnsupportedOperationException();
    }

    public ProfileHeroResponse findHeroProfile(
        Long userId
    ) {
        // TODO 히어로 모드가 활성화 되어있는지 확인
        throw new UnsupportedOperationException();
    }

    @Transactional
    public UserUpdateResponse updateUser(UserServiceUpdateRequest userServiceUpdateRequest) {
        var userId = userServiceUpdateRequest.userId();
        var user = userReader.findOne(userId);

        var userBasicInfo = userServiceUpdateRequest.toUserBasicInfo();
        var userFavoriteWorkingDay = userServiceUpdateRequest.toUserFavoriteWorkingDay();

        user.updateUser(userBasicInfo, userFavoriteWorkingDay);

        return UserUpdateResponse.of(
            user.getId(),
            user.getUserBasicInfo(),
            user.getUserFavoriteWorkingDay()
        );
    }

    @Transactional
    public UserResponse turnHeroModeOn(
        Long userId
    ) {
        throw new UnsupportedOperationException();
    }

    @Transactional
    public UserResponse turnHeroModeOff(
        Long userId
    ) {
        throw new UnsupportedOperationException();
    }
}
