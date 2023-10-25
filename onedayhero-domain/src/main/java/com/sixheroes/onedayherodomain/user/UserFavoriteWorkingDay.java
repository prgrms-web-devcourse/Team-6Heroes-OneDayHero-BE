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

    @Column(name = "favorite_start_date", nullable = true)
    private LocalTime favoriteStartDate;

    @Column(name = "favorite_end_date", nullable = true)
    private LocalTime favoriteEndDate;

    @Builder
    public UserFavoriteWorkingDay(
        List<Week> favoriteDate,
        LocalTime favoriteStartDate,
        LocalTime favoriteEndDate
    ) {
        validStartAndEndDate(favoriteStartDate, favoriteEndDate);

        this.favoriteDate = favoriteDate;
        this.favoriteStartDate = favoriteStartDate;
        this.favoriteEndDate = favoriteEndDate;
    }

    private void validStartAndEndDate(
        LocalTime favoriteStartDate,
        LocalTime favoriteEndDate
    ) {
        if (favoriteStartDate.isAfter(favoriteEndDate)) {
            log.debug("시작 시간은 종료 시간보다 미래이면 안됩니다. startDate = {}, endDate = {}", favoriteStartDate, favoriteEndDate);
            throw new IllegalArgumentException(ErrorCode.EU_005.name());
        }
    }
}
