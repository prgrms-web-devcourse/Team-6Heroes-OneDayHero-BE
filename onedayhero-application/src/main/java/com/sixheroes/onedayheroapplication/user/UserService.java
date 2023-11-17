package com.sixheroes.onedayheroapplication.user;

import com.sixheroes.onedayheroapplication.region.RegionReader;
import com.sixheroes.onedayheroapplication.user.request.UserServiceUpdateRequest;
import com.sixheroes.onedayheroapplication.user.response.UserResponse;
import com.sixheroes.onedayheroapplication.user.response.UserUpdateResponse;
import com.sixheroes.onedayherocommon.error.ErrorCode;
import com.sixheroes.onedayherodomain.region.Region;
import com.sixheroes.onedayherodomain.user.User;
import com.sixheroes.onedayherodomain.user.repository.UserRegionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserService {

    private final UserReader userReader;
    private final UserRegionReader userRegionReader;
    private final RegionReader regionReader;

    private final UserRegionRepository userRegionRepository;

    public UserResponse findUser(
        Long userId
    ) {
        var user = userReader.findOne(userId);
        var regions = regionReader.findByUser(user);

        return UserResponse.from(user, regions);
    }

    @Transactional
    public UserUpdateResponse updateUser(
        Long userId,
        UserServiceUpdateRequest userServiceUpdateRequest
    ) {
        var user = userReader.findOne(userId);

        var userBasicInfo = userServiceUpdateRequest.toUserBasicInfo();
        var userFavoriteWorkingDay = userServiceUpdateRequest.toUserFavoriteWorkingDay();

        user.updateUser(userBasicInfo, userFavoriteWorkingDay);

        // 기존의 유저 선호 지역 모두 삭제
        deleteUserRegions(user);

        // 변경된 유저 선호 지역 모두 넣기
        insertUserRegions(user, userServiceUpdateRequest);

        return UserUpdateResponse.from(user);
    }

    @Transactional
    public UserUpdateResponse turnOnHeroMode(
        Long userId
    ) {
        var user = userReader.findOne(userId);
        user.changeHeroModeOn();

        return UserUpdateResponse.from(user);
    }

    @Transactional
    public UserUpdateResponse turnOffHeroMode(
        Long userId
    ) {
        var user = userReader.findOne(userId);
        user.changeHeroModeOff();

        return UserUpdateResponse.from(user);
    }

    private void deleteUserRegions(
        User user
    ) {
        var findUserRegions = userRegionReader.findAll(user);
        userRegionRepository.deleteAllInBatch(findUserRegions);
    }

    private void insertUserRegions(
        User user,
        UserServiceUpdateRequest userServiceUpdateRequest
    ) {
        var regionIds = userServiceUpdateRequest.userFavoriteRegions();
        if (regionIds.isEmpty()) {
            return;
        }

        var regions = regionReader.findAll(regionIds);
        validRegions(userServiceUpdateRequest.userFavoriteRegions(), regions);

        var userRegions = userServiceUpdateRequest.toUserRegions(user);
        userRegionRepository.saveAll(userRegions);
    }

    private void validRegions(
        List<Long> regionIds,
        List<Region> regions
    ) {
        var notExistRegionIds = regionIds.stream()
            .filter(id ->
                regions.stream()
                    .map(Region::getId)
                    .noneMatch(regionId -> Objects.equals(regionId, id))
            ).toList();

        if (!notExistRegionIds.isEmpty()) {
            log.debug("존재하지 않는 지역 아이디입니다. regionIds = {}", notExistRegionIds);
            throw new NoSuchElementException(ErrorCode.ER_000.name());
        }
    }
}
