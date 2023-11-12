package com.sixheroes.onedayheroapplication.user;

import com.sixheroes.onedayheroapplication.user.request.UserServiceUpdateRequest;
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
        var userQueryDto = userReader.findOne(userId);

        return UserResponse.from(userQueryDto);
    }

    @Transactional
    public UserUpdateResponse updateUser(UserServiceUpdateRequest userServiceUpdateRequest) {
        var userId = userServiceUpdateRequest.userId();
        var user = userReader.findOne(userId);

        var userBasicInfo = userServiceUpdateRequest.toUserBasicInfo();
        var userFavoriteWorkingDay = userServiceUpdateRequest.toUserFavoriteWorkingDay();

        user.updateUser(userBasicInfo, userFavoriteWorkingDay);

        return UserUpdateResponse.from(user);
    }

    @Transactional
    public void turnOnHeroMode(
        Long userId
    ) {
        var user = userReader.findOne(userId);
        user.changeHeroModeOn();
    }

    @Transactional
    public void turnOffHeroMode(
        Long userId
    ) {
        var user = userReader.findOne(userId);
        user.changeHeroModeOff();
    }
}
