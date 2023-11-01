package com.sixheroes.onedayherodomain.user;

import com.sixheroes.onedayherocommon.error.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class EmailTest {

    @DisplayName("이메일 형식은 올바르면 이메일이 생성된다")
    @Test
    void validEmailRegex() {
        // given
        var email = "123@abc.com";

        // when
        var createEmail = Email.builder()
                                .email(email)
                                .build();

        // then
        assertThat(createEmail.getEmail()).isEqualTo(email);
    }

    @DisplayName("이메일 형식이 올바르지 않으면 예외가 발생한다.")
    @CsvSource(value = { "123@abc", "@abc", "123@", "abc", "123@abc.", "123@.com", "123@." })
    @ParameterizedTest
    void invalidEmailRegex(String email) {
        // given

        // when & then
        assertThatThrownBy(() -> Email.builder()
            .email(email)
            .build())
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(ErrorCode.EU_001.name());
    }

    @DisplayName("이메일 길이가 255자 이하이면 이메일이 생성된다.")
    @Test
    void validEmailLength() {
        // given
        var random = new Random();
        var email = "%s@abc.com".formatted(random.ints('a', 'z' + 1)
            .limit(247)
            .collect(
                StringBuilder::new,
                StringBuilder::appendCodePoint,
                StringBuilder::append
            ).toString());

        // when
        var createEmail = Email.builder()
            .email(email)
            .build();

        // then
        assertThat(createEmail.getEmail()).isEqualTo(email);
    }

    @DisplayName("이메일 길이가 255자를 초과하면 예외가 발생한다.")
    @Test
    void invalidOutOfRangeEmailLength() {
        // given
        var random = new Random();
        var email = "%s@abc.com".formatted(random.ints('a', 'z' + 1)
            .limit(248)
            .collect(
                StringBuilder::new,
                StringBuilder::appendCodePoint,
                StringBuilder::append
            ).toString());

        // when & then
        assertThatThrownBy(() -> Email.builder()
            .email(email)
            .build())
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(ErrorCode.EU_002.name());
    }
}