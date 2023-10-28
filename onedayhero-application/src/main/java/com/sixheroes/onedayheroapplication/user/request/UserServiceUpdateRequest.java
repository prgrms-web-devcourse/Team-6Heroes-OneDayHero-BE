package com.sixheroes.onedayheroapplication.user.request;

import com.sixheroes.onedayheroapplication.user.dto.UserBasicInfoServiceDto;
import com.sixheroes.onedayheroapplication.user.dto.UserFavoriteWorkingDayServiceDto;
import com.sixheroes.onedayherodomain.user.UserBasicInfo;
import com.sixheroes.onedayherodomain.user.UserFavoriteWorkingDay;
import com.sixheroes.onedayherodomain.user.UserGender;
import com.sixheroes.onedayherodomain.user.Week;
import lombok.Builder;

import java.util.List;
import java.util.Optional;

@Builder
public record UserServiceUpdateRequest(
    Long userId,
    UserBasicInfoServiceDto userBasicInfo,
    UserFavoriteWorkingDayServiceDto userFavoriteWorkingDay
) {

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
            .orElse(null);

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
}
