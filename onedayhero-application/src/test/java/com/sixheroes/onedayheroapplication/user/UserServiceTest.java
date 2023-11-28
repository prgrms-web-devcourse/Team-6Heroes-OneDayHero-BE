package com.sixheroes.onedayheroapplication.user;

import com.sixheroes.onedayheroapplication.IntegrationApplicationTest;
import com.sixheroes.onedayheroapplication.global.s3.dto.request.S3ImageUploadServiceRequest;
import com.sixheroes.onedayheroapplication.global.s3.dto.response.S3ImageDeleteServiceResponse;
import com.sixheroes.onedayheroapplication.global.s3.dto.response.S3ImageUploadServiceResponse;
import com.sixheroes.onedayheroapplication.user.request.UserBasicInfoServiceRequest;
import com.sixheroes.onedayheroapplication.user.request.UserFavoriteWorkingDayServiceRequest;
import com.sixheroes.onedayheroapplication.user.request.UserServiceUpdateRequest;
import com.sixheroes.onedayherocommon.exception.EntityNotFoundException;
import com.sixheroes.onedayherodomain.region.Region;
import com.sixheroes.onedayherodomain.user.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@Transactional
class UserServiceTest extends IntegrationApplicationTest {

    @DisplayName("처음 OAUTH 로그인을 시도하면 기본 설정값으로 유저가 저장된다.")
    @Test
    void signUp() {
        // given
        var initUserbasicInfo = UserBasicInfo.initStatus(DefaultNicknameGenerator.generate());
        var email = Email.builder()
                .email("test@email.com")
                .build();
        var user = User.signUp(
                email,
                UserSocialType.findByName("KAKAO"),
                UserRole.MEMBER,
                initUserbasicInfo
        );

        // when
        var createdUser = userRepository.save(user);


        // then
        assertSoftly(soft -> {
            soft.assertThat(createdUser.getEmail().getEmail()).isEqualTo(email.getEmail());
            soft.assertThat(createdUser.getUserBasicInfo().getNickname()).isEqualTo(initUserbasicInfo.getNickname());
            soft.assertThat(createdUser.getUserBasicInfo().getIntroduce()).isEqualTo("-");
            soft.assertThat(createdUser.getUserBasicInfo().getGender()).isEqualTo(UserGender.OTHER);
            soft.assertThat(createdUser.getUserBasicInfo().getBirth()).isEqualTo(LocalDate.of(2023, 1, 1));
        });
    }

    @DisplayName("유저의 기본 정보와 유저가 선호하는 근무일, 선호하는 지역, 유저 이미지를 변경할 수 있다.")
    @Test
    void updateUser() throws IOException {
        // given
        var user = createUser();
        var savedUser = userRepository.save(user);
        var userImage = createUserImage(user);
        userImageRepository.save(userImage);

        var nickname = "바뀐 이름";
        var gender = "FEMALE";
        var birth = LocalDate.of(2000, 1, 1);
        var introduce = "바뀐 자기 소개";
        var favoriteDate = List.of("MON", "TUE");
        var favoriteStartTime = LocalTime.of(12, 0, 0);
        var favoriteEndTime = LocalTime.of(18, 0, 0);

        var userBasicInfoServiceDto = createUserBasicInfoService(nickname, gender, birth, introduce);

        var userFavoriteWorkingDayServiceDto = createUserFavoriteWorkingDayServiceRequest(favoriteDate, favoriteStartTime, favoriteEndTime);

        var regionIds = regionRepository.findAll().stream()
                .map(Region::getId)
                .toList();

        var userServiceUpdateRequest = createUserServiceUpdateRequest(userBasicInfoServiceDto, userFavoriteWorkingDayServiceDto, regionIds);

        var images = createMockMultipartFile();
        var s3ImageUploadServiceRequest = new S3ImageUploadServiceRequest(images.getInputStream(), images.getOriginalFilename(), images.getContentType(), images.getSize());
        var s3ImageUploadServiceRequests = List.of(s3ImageUploadServiceRequest);

        var changedOriginalName = "수정할 원본 이름";
        var changedUniqueName = "수정할 고유 이름";
        var changedPath = "https://changed";
        var s3ImageUploadServiceResponses = List.of(new S3ImageUploadServiceResponse(changedOriginalName, changedUniqueName, changedPath));

        given(s3ImageDeleteService.deleteImages(anyList())).willReturn(List.of(new S3ImageDeleteServiceResponse(1L)));
        given(s3ImageUploadService.uploadImages(anyList(), isNull())).willReturn(s3ImageUploadServiceResponses);

        // when
        var userUpdateResponse = userService.updateUser(user.getId(), userServiceUpdateRequest, s3ImageUploadServiceRequests);

        // then
        assertThat(userUpdateResponse.id()).isEqualTo(savedUser.getId());
        assertThat(userRegionRepository.findAllByUser(savedUser)).hasSize(regionIds.size());
        var userImageByUserId = userImageRepository.findUserImageByUser_Id(savedUser.getId());
        assertThat(userImageByUserId).isNotEmpty();
        assertThat(userImageByUserId.get()).extracting("originalName", "uniqueName", "path")
                .containsExactly(changedOriginalName, changedUniqueName, changedPath);
        verify(s3ImageUploadService, times(1)).uploadImages(anyList(), isNull());
        verify(s3ImageDeleteService, times(1)).deleteImages(anyList());
    }

    @DisplayName("아이디가 일치하는 유저가 존재하지 않는다면 예외가 발생한다.")
    @Test
    void updateUserWhenNotExist() {
        // given
        var notExistUserId = 2L;

        var userServiceUpdateRequest = UserServiceUpdateRequest.builder()
                .build();

        // when & then
        assertThatThrownBy(() -> userService.updateUser(notExistUserId, userServiceUpdateRequest, Collections.emptyList()))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @DisplayName("수정할 선호 지역에 존재하지 않는 지역이 있다면 예외가 발생한다.")
    @Test
    void updateUserNotExistRegion() {
        // given
        var user = createUser();
        var savedUser = userRepository.save(user);
        var savedUserId = savedUser.getId();

        var notExistRegions = List.of(100L, 101L);

        var nickname = "바뀐 이름";
        var gender = "FEMALE";
        var birth = LocalDate.of(2000, 1, 1);
        var introduce = "바뀐 자기 소개";
        var favoriteDate = List.of("MON", "TUE");
        var favoriteStartTime = LocalTime.of(12, 0, 0);
        var favoriteEndTime = LocalTime.of(18, 0, 0);

        var userBasicInfoServiceDto = createUserBasicInfoService(nickname, gender, birth, introduce);

        var userFavoriteWorkingDayServiceDto = createUserFavoriteWorkingDayServiceRequest(favoriteDate, favoriteStartTime, favoriteEndTime);

        var userServiceUpdateRequest = UserServiceUpdateRequest.builder()
                .userBasicInfo(userBasicInfoServiceDto)
                .userFavoriteWorkingDay(userFavoriteWorkingDayServiceDto)
                .userFavoriteRegions(notExistRegions)
                .build();

        // when & then
        assertThatThrownBy(() -> userService.updateUser(savedUserId, userServiceUpdateRequest, Collections.emptyList()))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @DisplayName("유저의 프로필을 조회한다.")
    @Test
    void findUser() {
        // given
        var user = createUser();
        var savedUser = userRepository.save(user);

        var userImage = createUserImage(savedUser);
        userImageRepository.save(userImage);

        var regionIds = regionRepository.findAll().stream()
                .map(Region::getId)
                .toList();
        var userRegions = regionIds.stream()
                .map(regionId -> UserRegion.builder()
                        .regionId(regionId)
                        .user(user)
                        .build())
                .toList();
        userRegionRepository.saveAll(userRegions);

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
        assertThat(userResponse.favoriteRegions()).isNotEmpty();
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
                .isInstanceOf(EntityNotFoundException.class);
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
    void turnOnHeroModeWhenNotExist() {
        // given
        var notExistUserId = 2L;

        // when & then
        assertThatThrownBy(() -> userService.turnOnHeroMode(notExistUserId))
                .isInstanceOf(com.sixheroes.onedayherocommon.exception.EntityNotFoundException.class);
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
    void turnOffHeroModeWhenNotExist() {
        // given
        var notExistUserId = 2L;

        // when & then
        assertThatThrownBy(() -> userService.turnOnHeroMode(notExistUserId))
                .isInstanceOf(EntityNotFoundException.class);
    }

    private UserImage createUserImage(
            User user
    ) {
        var originalName = "원본 이름";
        var uniqueName = "고유 이름";
        var path = "https://";

        return UserImage.createUserImage(
                user,
                originalName,
                uniqueName,
                path
        );
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

    private static MockMultipartFile createMockMultipartFile() {
        return new MockMultipartFile(
                "images",
                "imageA.jpeg",
                "image/jpeg",
                "<<jpeg data>>".getBytes()
        );
    }

    private UserServiceUpdateRequest createUserServiceUpdateRequest(UserBasicInfoServiceRequest userBasicInfoServiceDto, UserFavoriteWorkingDayServiceRequest userFavoriteWorkingDayServiceDto, List<Long> regionIds) {
        return UserServiceUpdateRequest.builder()
            .userBasicInfo(userBasicInfoServiceDto)
            .userImageId(null)
            .userFavoriteWorkingDay(userFavoriteWorkingDayServiceDto)
            .userFavoriteRegions(regionIds)
            .build();
    }

    private UserFavoriteWorkingDayServiceRequest createUserFavoriteWorkingDayServiceRequest(List<String> favoriteDate, LocalTime favoriteStartTime, LocalTime favoriteEndTime) {
        return UserFavoriteWorkingDayServiceRequest.builder()
                .favoriteDate(favoriteDate)
                .favoriteStartTime(favoriteStartTime)
                .favoriteEndTime(favoriteEndTime)
                .build();
    }

    private UserBasicInfoServiceRequest createUserBasicInfoService(String nickname, String gender, LocalDate birth, String introduce) {
        return UserBasicInfoServiceRequest.builder()
                .nickname(nickname)
                .gender(gender)
                .birth(birth)
                .introduce(introduce)
                .build();
    }

}
