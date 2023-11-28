package com.sixheroes.onedayherodomain.user;

import com.sixheroes.onedayherocommon.exception.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


class UserGenderTest {

    @DisplayName("올바른 성별 값이면 성별이 생성된다.")
    @CsvSource(value = {"MALE", "FEMALE"})
    @ParameterizedTest
    void createGender(String gender) {
        // given

        // when
        var userGender = UserGender.from(gender);

        // then
        assertThat(userGender.name()).isEqualTo(gender);
    }

    @DisplayName("올바르지 않는 성별 값이면 예외가 발생한다.")
    @Test
    void createInvalidGender() {
        // given
        var gender = "Invalid";

        // when & then
        assertThatThrownBy(() -> UserGender.from(gender))
                .isInstanceOf(EntityNotFoundException.class);
    }
}