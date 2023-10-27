package com.sixheroes.onedayheroapplication.user.request;

import com.sixheroes.onedayherodomain.user.UserBasicInfo;
import com.sixheroes.onedayherodomain.user.UserFavoriteWorkingDay;
import com.sixheroes.onedayherodomain.user.UserGender;
import com.sixheroes.onedayherodomain.user.Week;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record UserServiceUpdateRequest(
    Long userId,
    UserBasicInfo userBasicInfo,
    UserFavoriteWorkingDay userFavoriteWorkingDay
) {

    public static UserServiceUpdateRequest from(
        Long userId,
        String nickname,
        String gender,
        LocalDate birth,
        String introduce,
        List<String> favoriteDate,
        LocalTime favoriteStartTime,
        LocalTime favoriteEndTime
    ) {
        var userBasicInfo = UserBasicInfo.builder()
            .nickname(nickname)
            .gender(UserGender.from(gender))
            .birth(birth)
            .introduce(introduce)
            .build();

        var weeks = favoriteDate.stream()
            .map(Week::from)
            .toList();

        var userFavoriteWorkingDay = UserFavoriteWorkingDay.builder()
            .favoriteDate(weeks)
            .favoriteStartTime(favoriteStartTime)
            .favoriteEndTime(favoriteEndTime)
            .build();

        return new UserServiceUpdateRequest(userId, userBasicInfo, userFavoriteWorkingDay);
    }
}
