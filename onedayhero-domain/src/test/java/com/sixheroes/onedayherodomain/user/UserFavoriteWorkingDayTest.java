package com.sixheroes.onedayherodomain.user;

import com.sixheroes.onedayherocommon.error.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserFavoriteWorkingDayTest {

    @DisplayName("희망 근무 시작 시간이 종료 시간보다 과거라면 희망 근무 날짜가 생성된다.")
    @Test
    void validFavoriteStartBeforeThanEndTime() {
        // given
        var startTime = LocalTime.of(12, 0, 0);
        var endTime = LocalTime.of(12, 0, 1);

        // when
        var userFavoriteWorkingDay = UserFavoriteWorkingDay.builder()
            .favoriteDate(List.of(Week.MON))
            .favoriteStartTime(startTime)
            .favoriteEndTime(endTime)
            .build();

        // then
        assertThat(userFavoriteWorkingDay.getFavoriteStartTime()).isEqualTo(startTime);
        assertThat(userFavoriteWorkingDay.getFavoriteEndTime()).isEqualTo(endTime);
    }

    @DisplayName("희망 근무 시작 시간이 종료 시간보다 미래라면 예외가 발생한다.")
    @Test
    void invalidFavoriteStartAfterThanEndDateExTest() {
        // given
        var startTime = LocalTime.of(12, 0, 1);
        var endTime = LocalTime.of(12, 0, 0);

        // when & then
        assertThatThrownBy(() -> UserFavoriteWorkingDay.builder()
            .favoriteDate(List.of(Week.MON))
            .favoriteStartTime(startTime)
            .favoriteEndTime(endTime)
            .build())
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(ErrorCode.EU_005.name());
    }
}