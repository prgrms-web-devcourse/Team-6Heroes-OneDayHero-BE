package com.sixheroes.onedayherodomain.user;

import com.sixheroes.onedayherocommon.error.ErrorCode;
import com.sixheroes.onedayherocommon.exception.BusinessException;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class UserFavoriteWorkingDay {

    @Convert(converter = FavoriteDateConverter.class)
    @Column(name = "favorite_date", length = 45, nullable = true)
    private List<Week> favoriteDate = new ArrayList<>();

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
        validFavoriteStartBeforeThanEndTime(favoriteStartTime, favoriteEndTime);

        this.favoriteDate = favoriteDate;
        this.favoriteStartTime = favoriteStartTime;
        this.favoriteEndTime = favoriteEndTime;
    }

    private void validFavoriteStartBeforeThanEndTime(
            LocalTime favoriteStartTime,
            LocalTime favoriteEndTime
    ) {
        if (Objects.nonNull(favoriteStartTime) && favoriteStartTime.isAfter(favoriteEndTime)) {
            log.debug("시작 시간은 종료 시간보다 미래이면 안됩니다. startDate = {}, endDate = {}", favoriteStartTime, favoriteEndTime);
            throw new BusinessException(ErrorCode.INVALID_WORKING_TIME);
        }
    }
}
