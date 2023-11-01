package com.sixheroes.onedayheroapplication.user;

import com.sixheroes.onedayheroapplication.IntegrationApplicationTest;
import com.sixheroes.onedayheroapplication.user.dto.UserBasicInfoServiceDto;
import com.sixheroes.onedayheroapplication.user.dto.UserFavoriteWorkingDayServiceDto;
import com.sixheroes.onedayheroapplication.user.request.UserServiceUpdateRequest;
import com.sixheroes.onedayherocommon.error.ErrorCode;
import com.sixheroes.onedayherodomain.user.Email;
import com.sixheroes.onedayherodomain.user.User;
import com.sixheroes.onedayherodomain.user.UserBasicInfo;
import com.sixheroes.onedayherodomain.user.UserFavoriteWorkingDay;
import com.sixheroes.onedayherodomain.user.UserGender;
import com.sixheroes.onedayherodomain.user.UserRole;
import com.sixheroes.onedayherodomain.user.UserSocialType;
import com.sixheroes.onedayherodomain.user.Week;
import com.sixheroes.onedayherodomain.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.*;

class UserServiceTest extends IntegrationApplicationTest {

    @Autowired
    private UserRepository userRepository;

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

        var userBasicInfoServiceDto = UserBasicInfoServiceDto.builder()
            .nickname(nickname)
            .gender(gender)
            .birth(birth)
            .introduce(introduce)
            .build();

        var userFavoriteWorkingDayServiceDto = UserFavoriteWorkingDayServiceDto.builder()
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
        assertThat(userUpdateResponse.userId()).isEqualTo(savedUser.getId());
        assertThat(userUpdateResponse.basicInfo())
            .extracting("nickname", "gender", "birth", "introduce")
            .contains(nickname, gender, birth, introduce);
        assertThat(userUpdateResponse.favoriteWorkingDay())
            .extracting("favoriteDate", "favoriteStartTime", "favoriteEndTime")
            .contains(favoriteDate, favoriteStartTime, favoriteEndTime);
    }

    @DisplayName("아이디가 일치하는 유저가 존재하지 않는다면 예외가 발생한다.")
    @Test
    void updateUserW() {
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

        var userBasicInfoServiceDto = UserBasicInfoServiceDto.builder()
            .nickname(nickname)
            .gender(gender)
            .birth(birth)
            .introduce(introduce)
            .build();

        var userFavoriteWorkingDayServiceDto = UserFavoriteWorkingDayServiceDto.builder()
            .favoriteDate(favoriteDate)
            .favoriteStartTime(favoriteStartTime)
            .favoriteEndTime(favoriteEndTime)
            .build();

        var userServiceUpdateRequest = UserServiceUpdateRequest.builder()
            .userId(2L)
            .userBasicInfo(userBasicInfoServiceDto)
            .userFavoriteWorkingDay(userFavoriteWorkingDayServiceDto)
            .build();

        // when & then
        assertThatThrownBy(() -> userService.updateUser(userServiceUpdateRequest))
            .isInstanceOf(NoSuchElementException.class)
            .hasMessage(ErrorCode.EUC_001.name());
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