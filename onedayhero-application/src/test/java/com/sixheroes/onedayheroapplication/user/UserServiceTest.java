package com.sixheroes.onedayheroapplication.user;

import com.sixheroes.onedayheroapplication.IntegrationApplicationTest;
import com.sixheroes.onedayheroapplication.user.request.UserBasicInfoServiceRequest;
import com.sixheroes.onedayheroapplication.user.request.UserFavoriteWorkingDayServiceRequest;
import com.sixheroes.onedayheroapplication.user.request.UserServiceUpdateRequest;
import com.sixheroes.onedayherocommon.error.ErrorCode;
import com.sixheroes.onedayherodomain.user.*;
import com.sixheroes.onedayherodomain.user.repository.UserImageRepository;
import com.sixheroes.onedayherodomain.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
class UserServiceTest extends IntegrationApplicationTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserImageRepository userImageRepository;

    @Autowired
    private UserService userService;

    @DisplayName("유저의 기본 정보와 유저가 선호하는 근무일을 변경할 수 있다.")
    @Test
    void updateUser() {
        // given
        var user = createUser();
        var savedUser = userRepository.save(user);

        var nickname = "바뀐 이름";
        var gender = "FEMALE";
        var birth = LocalDate.of(2000, 1, 1);
        var introduce = "바뀐 자기 소개";
        var favoriteDate = List.of("MON", "TUE");
        var favoriteStartTime = LocalTime.of(12, 0, 0);
        var favoriteEndTime = LocalTime.of(18, 0, 0);

        var userBasicInfoServiceDto = UserBasicInfoServiceRequest.builder()
            .nickname(nickname)
            .gender(gender)
            .birth(birth)
            .introduce(introduce)
            .build();

        var userFavoriteWorkingDayServiceDto = UserFavoriteWorkingDayServiceRequest.builder()
            .favoriteDate(favoriteDate)
            .favoriteStartTime(favoriteStartTime)
            .favoriteEndTime(favoriteEndTime)
            .build();

        var userServiceUpdateRequest = UserServiceUpdateRequest.builder()
            .userId(savedUser.getId())
            .userBasicInfo(userBasicInfoServiceDto)
            .userFavoriteWorkingDay(userFavoriteWorkingDayServiceDto)
            .build();

        // when
        var userUpdateResponse = userService.updateUser(userServiceUpdateRequest);

        // then
        assertThat(userUpdateResponse.id()).isEqualTo(savedUser.getId());
        assertThat(userUpdateResponse.basicInfo())
            .extracting("nickname", "gender", "birth", "introduce")
            .contains(nickname, gender, birth, introduce);
        assertThat(userUpdateResponse.favoriteWorkingDay())
            .extracting("favoriteDate", "favoriteStartTime", "favoriteEndTime")
            .contains(favoriteDate, favoriteStartTime, favoriteEndTime);
    }

    @DisplayName("아이디가 일치하는 유저가 존재하지 않는다면 예외가 발생한다.")
    @Test
    void updateUserWhenNotExist() {
        // given
        var user = createUser();
        userRepository.save(user);

        var nickname = "바뀐 이름";
        var gender = "FEMALE";
        var birth = LocalDate.of(2000, 1, 1);
        var introduce = "바뀐 자기 소개";
        var favoriteDate = List.of("MON", "THU");
        var favoriteStartTime = LocalTime.of(12, 0, 0);
        var favoriteEndTime = LocalTime.of(18, 0, 0);

        var userBasicInfoServiceDto = UserBasicInfoServiceRequest.builder()
            .nickname(nickname)
            .gender(gender)
            .birth(birth)
            .introduce(introduce)
            .build();

        var userFavoriteWorkingDayServiceDto = UserFavoriteWorkingDayServiceRequest.builder()
            .favoriteDate(favoriteDate)
            .favoriteStartTime(favoriteStartTime)
            .favoriteEndTime(favoriteEndTime)
            .build();

        var notExsistUserId = 2L;

        var userServiceUpdateRequest = UserServiceUpdateRequest.builder()
            .userId(notExsistUserId)
            .userBasicInfo(userBasicInfoServiceDto)
            .userFavoriteWorkingDay(userFavoriteWorkingDayServiceDto)
            .build();

        // when & then
        assertThatThrownBy(() -> userService.updateUser(userServiceUpdateRequest))
            .isInstanceOf(NoSuchElementException.class)
            .hasMessage(ErrorCode.EUC_000.name());
    }

    @DisplayName("유저의 프로필을 조회한다.")
    @Test
    void findUser() {
        // given
        var user = createUser();
        var savedUser = userRepository.save(user);

        var userImage = createUserImage(savedUser);
        userImageRepository.save(userImage);

        // when
        var userResponse = userService.findUser(savedUser.getId());

        // then
        var userBasicInfo = savedUser.getUserBasicInfo();
        assertThat(userResponse.basicInfo())
            .extracting("nickname", "gender", "birth", "introduce")
            .containsExactly(userBasicInfo.getNickname(), userBasicInfo.getGender().name(), userBasicInfo.getBirth(), userBasicInfo.getIntroduce());
        var userFavoriteWorkingDay = savedUser.getUserFavoriteWorkingDay();
        var favoriteDate = userFavoriteWorkingDay.getFavoriteDate().stream().map(Week::name).toList();
        assertThat(userResponse.favoriteWorkingDay())
            .extracting("favoriteDate", "favoriteStartTime", "favoriteEndTime")
            .containsExactly(favoriteDate, userFavoriteWorkingDay.getFavoriteStartTime(), userFavoriteWorkingDay.getFavoriteEndTime());
        assertThat(userResponse.image())
            .extracting("originalName", "uniqueName", "path")
            .containsExactly(userImage.getOriginalName(), userImage.getUniqueName(), userImage.getPath());
        assertThat(userResponse.heroScore()).isEqualTo(user.getHeroScore());
        assertThat(userResponse.isHeroMode()).isEqualTo(user.getIsHeroMode());
    }

    @DisplayName("유저의 프로필을 조회할 때 존재하지 않는 유저이면 예외가 발생한다.")
    @Transactional(readOnly = true)
    @Test
    void findUserWhenNotExist() {
        // given
        var notExistUserId = 2L;

        // when & then
        assertThatThrownBy(() -> userService.findUser(notExistUserId))
            .isInstanceOf(NoSuchElementException.class)
            .hasMessage(ErrorCode.EUC_000.name());
    }

    @DisplayName("유저의 히어로 모드를 활성화한다.")
    @Test
    void turnOnHeroMode() {
        // given
        var user = createUser();
        var savedUser = userRepository.save(user);

        // when
        userService.turnOnHeroMode(savedUser.getId());

        // then
        assertThat(savedUser.getIsHeroMode()).isTrue();
    }

    @DisplayName("유저의 히어로 모드를 활성화할 때 존재하지 않는 유저이면 예외가 발생한다.")
    @Transactional(readOnly = true)
    @Test
    void turnOnHeroModeWhenNotExisit() {
        // given
        var notExistUserId = 2L;

        // when & then
        assertThatThrownBy(() -> userService.turnOnHeroMode(notExistUserId))
            .isInstanceOf(NoSuchElementException.class)
            .hasMessage(ErrorCode.EUC_000.name());
    }

    @DisplayName("유저의 히어로 모드를 비활성화한다.")
    @Test
    void turnOffHeroMode() {
        // given
        var user = createUser();
        var savedUser = userRepository.save(user);
        savedUser.changeHeroModeOn();

        // when
        userService.turnOffHeroMode(savedUser.getId());

        // then
        assertThat(savedUser.getIsHeroMode()).isFalse();
    }

    @DisplayName("유저의 히어로 모드를 비활성화할 때 존재하지 않는 유저이면 예외가 발생한다.")
    @Transactional(readOnly = true)
    @Test
    void turnOnHeroModeWhenNotExsist() {
        // given
        var notExistUserId = 2L;

        // when & then
        assertThatThrownBy(() -> userService.turnOnHeroMode(notExistUserId))
            .isInstanceOf(NoSuchElementException.class)
            .hasMessage(ErrorCode.EUC_000.name());
    }

    private UserImage createUserImage(
        User user
    ) {
        var originalName = "원본 이름";
        var uniqueName = "고유 이름";
        var path = "http://";

        return UserImage.createUserImage(
            user,
            originalName,
            uniqueName,
            path
        );
    }

    private User createUser() {
        return User.builder()
            .email(createEamil())
            .userBasicInfo(createUserBasicInfo())
            .userFavoriteWorkingDay(createUserFavoriteWorkingDay())
            .userSocialType(UserSocialType.KAKAO)
            .userRole(UserRole.MEMBER)
            .build();
    }

    private Email createEamil() {
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