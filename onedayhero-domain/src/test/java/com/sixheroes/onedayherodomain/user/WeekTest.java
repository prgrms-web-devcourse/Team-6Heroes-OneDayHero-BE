package com.sixheroes.onedayherodomain.user;

import com.sixheroes.onedayherocommon.exception.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class WeekTest {

    @DisplayName("올바른 요일 값이면 요일이 생성된다.")
    @CsvSource(value = {"MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN"})
    @ParameterizedTest
    void createWeek(String week) {
        // given

        // when
        var createWeek = Week.from(week);

        // then
        assertThat(createWeek.name()).isEqualTo(week);
    }

    @DisplayName("올바르지 않은 요일 값이면 예외가 발생한다.")
    @Test
    void createInvalidWeek() {
        // given
        var week = "Invalid";

        // when & then
        assertThatThrownBy(() -> Week.from(week))
                .isInstanceOf(EntityNotFoundException.class);
    }
}