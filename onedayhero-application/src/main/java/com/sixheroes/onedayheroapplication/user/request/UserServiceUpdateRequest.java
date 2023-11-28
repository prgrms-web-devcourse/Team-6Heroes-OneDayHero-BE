package com.sixheroes.onedayheroapplication.user.request;

import com.sixheroes.onedayherodomain.user.*;
import lombok.Builder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Builder
public record UserServiceUpdateRequest(
    UserBasicInfoServiceRequest userBasicInfo,
    Long userImageId,
    UserFavoriteWorkingDayServiceRequest userFavoriteWorkingDay,
    List<Long> userFavoriteRegions
) {
    static final List<Week> EMPTY = new ArrayList<>();

    public UserBasicInfo toUserBasicInfo() {
        return UserBasicInfo.builder()
            .nickname(userBasicInfo().nickname())
            .gender(UserGender.from(userBasicInfo().gender()))
            .birth(userBasicInfo.birth())
            .introduce(userBasicInfo().introduce())
            .build();
    }

    public UserFavoriteWorkingDay toUserFavoriteWorkingDay() {
        var weeks = Optional.ofNullable(userFavoriteWorkingDay.favoriteDate())
            .map(this::toWeeks)
            .orElse(EMPTY);

        return UserFavoriteWorkingDay.builder()
            .favoriteDate(weeks)
            .favoriteStartTime(userFavoriteWorkingDay.favoriteStartTime())
            .favoriteEndTime(userFavoriteWorkingDay.favoriteEndTime())
            .build();
    }

    private List<Week> toWeeks(List<String> week) {
        return week.stream()
            .map(Week::from)
            .toList();
    }

    public List<UserRegion> toUserRegions(
        User user
    ) {
        return userFavoriteRegions.stream()
            .map(regionId -> UserRegion.builder()
                .user(user)
                .regionId(regionId)
                .build())
            .toList();
    }
}
