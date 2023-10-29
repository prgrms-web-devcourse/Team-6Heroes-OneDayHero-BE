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

    @DisplayName("히어로 모드는 계정이 비활성 상태이면 활성 상태로 변경할 때 예외가 발생한다.")
    @Test
    void doNotchangeHeroModeWhenNotActive() {
        // given
        var user = createUser();
        user.delete();

        // when & then
        assertThatThrownBy(user::changeHeroModeOn)
            .isInstanceOf(IllegalStateException.class)
            .hasMessage(ErrorCode.EU_010.name());
    }

    @DisplayName("탈퇴하면 계정이 비활성화 상태가 된다.")
    @Test
    void delete() {
        // given
        var user = createUser();

        // when
        user.delete();

        // then
        assertThat(user.getIsActive()).isFalse();
    }

    @DisplayName("이미 탈퇴한 회원이면 계정을 비활성화 상태로 바꿀 때 예외가 발생한다.")
    @Test
    void doNotDeleteWhenNotActive() {
        // given
        var user = createUser();
        user.delete();

        // when & then
        assertThatThrownBy(user::delete)
            .isInstanceOf(IllegalStateException.class)
            .hasMessage(ErrorCode.EU_010.name());
    }

    @DisplayName("히어로 모드가 아닐 때 미션 요청을 받을 수 없다.")
    @Test
    void impossibleMissionRequestedWhenNotHeroMode() {
        // given
        var user = createUser();

        // when & then
        assertThatThrownBy(user::validPossibleMissionRequested)
            .isInstanceOf(IllegalStateException.class)
            .hasMessage(ErrorCode.EU_009.name());
    }

    @DisplayName("계정이 비활성화 상태일 때 미션 요청을 받을 수 없다.")
    @Test
    void impossibleMissionRequestWhenNotActive() {
        // given
        var user = createUser();
        user.changeHeroModeOn();
        user.delete();

        // when & then
        assertThatThrownBy(user::validPossibleMissionRequested)
            .isInstanceOf(IllegalStateException.class)
            .hasMessage(ErrorCode.EU_010.name());
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