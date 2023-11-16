package com.sixheroes.onedayherodomain.user;

import com.sixheroes.onedayherocommon.error.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserTest {

    @DisplayName("히어로 모드를 활성 상태로 바꾼다.")
    @Test
    void changeHeroModeOn() {
        // given
        var user = createUser();

        // when
        user.changeHeroModeOn();

        // then
        assertThat(user.getIsHeroMode()).isTrue();
    }

    @DisplayName("히어로 모드를 활성 상태일 때 활성 상태로 바꿀 수 없다.")
    @Test
    void invalidChangeHeroModeOn() {
        // given
        var user = createUser();
        user.changeHeroModeOn();

        // when & then
        assertThatThrownBy(user::changeHeroModeOn)
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(ErrorCode.EU_010.name());
    }

    @DisplayName("히어로 모드가 아닐 때 미션 제안을 받을 수 없다.")
    @Test
    void impossibleMissionProposedWhenNotHeroMode() {
        // given
        var user = createUser();

        // when & then
        assertThatThrownBy(user::validPossibleMissionProposal)
            .isInstanceOf(IllegalStateException.class)
            .hasMessage(ErrorCode.EU_009.name());
    }

    @DisplayName("히어로 모드가 아닐 때 히어로 프로필을 조회할 수 없다.")
    @Test
    void impossibleHeroProfile() {
        // given
        var user = createUser();

        // when & then
        assertThatThrownBy(user::validPossibleHeroProfile)
            .isInstanceOf(IllegalStateException.class)
            .hasMessage(ErrorCode.EU_009.name());
    }

    @DisplayName("히어로 모드를 비활성 상태로 바꾼다.")
    @Test
    void changeHeroModeOff() {
        // given
        var user = createUser();
        user.changeHeroModeOn();

        // when
        user.changeHeroModeOff();

        // then
        assertThat(user.getIsHeroMode()).isFalse();
    }

    @DisplayName("히어로 모드를 비활성 상태일 때 비활성 상태로 바꿀 수 없다.")
    @Test
    void invalidChangeHeroModeOff() {
        // given
        var user = createUser();

        // when & then
        assertThatThrownBy(user::changeHeroModeOff)
            .isInstanceOf(IllegalStateException.class)
            .hasMessage(ErrorCode.EU_009.name());
    }

    private User createUser() {
        return User.builder()
            .userBasicInfo(createUserBasicInfo())
            .userFavoriteWorkingDay(createUserFavoriteWorkingDay())
            .userSocialType(UserSocialType.KAKAO)
            .userRole(UserRole.MEMBER)
            .email(createEmail())
            .build();
    }

    private UserBasicInfo createUserBasicInfo() {
        return UserBasicInfo.builder()
            .nickname("이름")
            .birth(LocalDate.of(1990, 1, 1))
            .gender(UserGender.MALE)
            .introduce("자기소개")
            .build();
    }

    private UserFavoriteWorkingDay createUserFavoriteWorkingDay() {
        return UserFavoriteWorkingDay.builder()
            .favoriteDate(List.of(Week.MON, Week.THU))
            .favoriteStartTime(LocalTime.of(12, 0, 0))
            .favoriteEndTime(LocalTime.of(18, 0, 0))
            .build();
    }

    private Email createEmail() {
        return Email.builder()
            .email("abc@123.com")
            .build();
    }
}