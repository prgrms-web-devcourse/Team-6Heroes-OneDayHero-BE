package com.sixheroes.onedayherodomain.user;

import com.sixheroes.onedayherocommon.error.ErrorCode;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalTime;
import java.util.List;

@Slf4j
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class UserFavoriteWorkingDay {

    @Convert(converter = FavoriteDateConverter.class)
    @Column(name = "favorite_date", length = 45, nullable = true)
    private List<Week> favoriteDate;

    @Column(name = "favorite_start_time", nullable = true)
    private LocalTime favoriteStartTime;

    @Column(name = "favorite_end_time", nullable = true)
    private LocalTime favoriteEndTime;

    @Builder
    private UserFavoriteWorkingDay(
        List<Week> favoriteDate,
        LocalTime favoriteStartTime,
        LocalTime favoriteEndTime
    ) {
        validStartAndEndDate(favoriteStartTime, favoriteEndTime);

        this.favoriteDate = favoriteDate;
        this.favoriteStartTime = favoriteStartTime;
        this.favoriteEndTime = favoriteEndTime;
    }

    private void validStartAndEndDate(
        LocalTime favoriteStartTime,
        LocalTime favoriteEndTime
    ) {
        if (favoriteStartTime.isAfter(favoriteEndTime)) {
            log.debug("시작 시간은 종료 시간보다 미래이면 안됩니다. startDate = {}, endDate = {}", favoriteStartTime, favoriteEndTime);
            throw new IllegalArgumentException(ErrorCode.EU_005.name());
        }
    }
}
