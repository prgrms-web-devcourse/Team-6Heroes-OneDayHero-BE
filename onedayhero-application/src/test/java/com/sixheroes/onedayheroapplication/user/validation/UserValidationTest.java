package com.sixheroes.onedayheroapplication.user.validation;

import com.sixheroes.onedayheroapplication.IntegrationApplicationTest;
import com.sixheroes.onedayherocommon.exception.BusinessException;
import com.sixheroes.onedayherodomain.user.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserValidationTest extends IntegrationApplicationTest {

    @DisplayName("닉네임이 중복되지만 동일한 유저이면 예외가 발생하지 않는다.")
    @Test
    void validNicknameWhenSameUser() {
        // given
        var user = createUser();
        userRepository.save(user);

        var duplicatedNickname = user.getUserBasicInfo().getNickname();

        // when & then
        userValidation.validUserNickname(user.getId(), duplicatedNickname);
    }

    @DisplayName("닉네임이 중복되는 유저가 있으면 예외가 발생한다.")
    @Test
    void validNickname() {
        // given
        var user = createUser();
        userRepository.save(user);

        var anotherUser = user.getId() + 1L;
        var duplicatedNickname = user.getUserBasicInfo().getNickname();

        // when & then
        assertThatThrownBy(() -> userValidation.validUserNickname(anotherUser, duplicatedNickname))
            .isInstanceOf(BusinessException.class);
    }

    private User createUser() {
        return User.builder()
            .email(createEmail())
            .userBasicInfo(createUserBasicInfo())
            .userFavoriteWorkingDay(createUserFavoriteWorkingDay())
            .userSocialType(UserSocialType.KAKAO)
            .userRole(UserRole.MEMBER)
            .build();
    }

    private Email createEmail() {
        return Email.builder()
            .email("abc@123.com")
            .build();
    }

    private UserBasicInfo createUserBasicInfo() {
        return UserBasicInfo.builder()
            .nickname("닉네임")
            .birth(LocalDate.of(1990, 1, 1))
            .gender(UserGender.MALE)
            .introduce("자기 소개")
            .build();
    }

    private UserFavoriteWorkingDay createUserFavoriteWorkingDay() {
        return UserFavoriteWorkingDay.builder()
            .favoriteDate(List.of(Week.MON, Week.FRI))
            .favoriteStartTime(LocalTime.of(12, 0, 0))
            .favoriteEndTime(LocalTime.of(18, 0, 0))
            .build();
    }

}