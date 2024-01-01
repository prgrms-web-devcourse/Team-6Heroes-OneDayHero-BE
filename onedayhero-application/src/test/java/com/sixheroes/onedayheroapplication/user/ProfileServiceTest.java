package com.sixheroes.onedayheroapplication.user;

import com.sixheroes.onedayheroapplication.IntegrationApplicationTest;
import com.sixheroes.onedayheroapplication.user.request.HeroRankServiceRequest;
import com.sixheroes.onedayheroapplication.user.response.HeroRankResponse;
import com.sixheroes.onedayherocommon.exception.EntityNotFoundException;
import com.sixheroes.onedayherodomain.mission.MissionCategoryCode;
import com.sixheroes.onedayherodomain.region.Region;
import com.sixheroes.onedayherodomain.user.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
class ProfileServiceTest extends IntegrationApplicationTest {

    @DisplayName("상대의 시민 프로필을 조회한다.")
    @Test
    void findCitizenProfile() {
        // given
        var user = createUser();
        var savedUser = userRepository.save(user);

        var userImage = createUserImage(savedUser);
        userImageRepository.save(userImage);

        // when
        var userResponse = profileService.findCitizenProfile(savedUser.getId());

        // then
        var userBasicInfo = savedUser.getUserBasicInfo();
        assertThat(userResponse.basicInfo())
                .extracting("nickname", "gender", "birth")
                .containsExactly(userBasicInfo.getNickname(), userBasicInfo.getGender().name(), userBasicInfo.getBirth());
        assertThat(userResponse.image())
                .extracting("originalName", "uniqueName", "path")
                .containsExactly(userImage.getOriginalName(), userImage.getUniqueName(), userImage.getPath());
        assertThat(userResponse.heroScore()).isEqualTo(user.getHeroScore());
    }

    @DisplayName("상대의 시민 프로필을 조회할 때 존재하지 않는 유저라면 예외가 발생한다")
    @Transactional(readOnly = true)
    @Test
    void findCitizenProfileWhenNotExist() {
        // given
        var notExistUserId = 2L;

        // when & then
        assertThatThrownBy(() -> profileService.findCitizenProfile(notExistUserId))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @DisplayName("상대의 히어로 프로필을 조회한다.")
    @Test
    void findHeroProfile() {
        // given
        var user = createUser();
        user.changeHeroModeOn();
        var savedUser = userRepository.save(user);

        var userImage = createUserImage(savedUser);
        userImageRepository.save(userImage);

        var userRegions = regionRepository.findAll().stream()
                .map(Region::getId)
                .map(regionId -> UserRegion.builder()
                        .user(user)
                        .regionId(regionId)
                        .build())
                .toList();
        userRegionRepository.saveAll(userRegions);

        // when
        var userResponse = profileService.findHeroProfile(savedUser.getId());

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
    }

    @DisplayName("상대의 프로필 시민 프로필을 조회할 때 존재하지 않는 유저라면 예외가 발생한다")
    @Transactional(readOnly = true)
    @Test
    void findHeroProfileWhenNotExist() {
        // given
        var notExistUserId = 2L;

        // when & then
        assertThatThrownBy(() -> profileService.findHeroProfile(notExistUserId))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @DisplayName("히어로를 닉네임으로 검색한다.")
    @Test
    void searchHeroes() {
        // given
        var user1 = createUser("별님");
        user1.changeHeroModeOn();
        userRepository.save(user1);
        var userImage1 = createUserImage(user1);
        userImageRepository.save(userImage1);

        var missionCategories = missionCategoryRepository.findAll();
        var userMissionCategory = createUserMissionCategory(missionCategories.get(0).getId(), user1);
        userMissionCategoryRepository.save(userMissionCategory);

        var user2 = createUser("달님");
        user2.changeHeroModeOn();
        userRepository.save(user2);
        var userImage2 = createUserImage(user2);
        userImageRepository.save(userImage2);

        var user3 = createUser("햇님");
        user3.changeHeroModeOn();
        userRepository.save(user3);
        var userImage3 = createUserImage(user3);
        userImageRepository.save(userImage3);

        var nickname = "님";
        var pageRequest = PageRequest.of(0, 3);

        // when
        var heroSearchResponses = profileService.searchHeroes(nickname, pageRequest);

        // then
        var content = heroSearchResponses.getContent();
        assertThat(content).hasSize(3);
        assertThat(content)
                .extracting("nickname")
                .isSorted();
        assertThat(content)
                .filteredOn("nickname", "별님")
                .extracting("favoriteMissionCategories")
                .hasSize(1);
    }

    @DisplayName("히어로를 랭킹과 함께 조회한다.")
    @Test
    void findHeroesRank() {
        // given
        var users = createUsers();
        var regionName = createUserRegion(users);
        var missionCategoryCode = createUserMissionCategory(users);

        var request = PageRequest.of(1, 2);
        var heroRankServiceRequest = HeroRankServiceRequest.of(regionName, missionCategoryCode.name());

        // when
        var heroesRank = profileService.findHeroesRank(heroRankServiceRequest, request);

        // then
        var content = heroesRank.getContent();
        assertThat(content)
            .isSortedAccordingTo(Comparator.comparing(HeroRankResponse::heroScore).reversed())
            .extracting("rank")
            .contains(3, 4);
    }

    private List<User> createUsers() {
        var email = Email.builder()
            .email("abc@123.com")
            .build();

        var userBasicInfo1 = UserBasicInfo.initStatus(DefaultNicknameGenerator.generate());
        var user1 = User.signUp(email, UserSocialType.KAKAO, UserRole.MEMBER, userBasicInfo1);

        var userBasicInfo2 = UserBasicInfo.initStatus(DefaultNicknameGenerator.generate());
        var user2 = User.signUp(email, UserSocialType.KAKAO, UserRole.MEMBER, userBasicInfo2);

        var userBasicInfo3 = UserBasicInfo.initStatus(DefaultNicknameGenerator.generate());
        var user3 = User.signUp(email, UserSocialType.KAKAO, UserRole.MEMBER, userBasicInfo3);

        var userBasicInfo4 = UserBasicInfo.initStatus(DefaultNicknameGenerator.generate());
        var user4 = User.signUp(email, UserSocialType.KAKAO, UserRole.MEMBER, userBasicInfo4);

        var users = userRepository.saveAll(List.of(user1, user2, user3, user4));

        user1.minusHeroScore(10);
        user3.sumHeroScore(20);
        user4.sumHeroScore(50);

        return users;
    }

    private String createUserRegion(
        List<User> users
    ) {
        var regions = regionRepository.findAll();
        var region = regions.get(0);
        for (User user : users) {
            var userRegion = UserRegion.builder()
                .user(user)
                .regionId(region.getId())
                .build();
            userRegionRepository.save(userRegion);
        }
        return region.getDong();
    }

    private MissionCategoryCode createUserMissionCategory(
        List<User> users
    ) {
        var missionCategories = missionCategoryRepository.findAll();
        var missionCategory = missionCategories.get(0);
        for (User user : users) {
            var userMissionCategory = UserMissionCategory.builder()
                .missionCategoryId(missionCategory.getId())
                .user(user)
                .build();
            userMissionCategoryRepository.save(userMissionCategory);
        }
        return missionCategory.getMissionCategoryCode();
    }

    private UserMissionCategory createUserMissionCategory(
            Long missionCategoryId,
            User user
    ) {
        return UserMissionCategory.builder()
                .missionCategoryId(missionCategoryId)
                .user(user)
                .build();
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

    private User createUser(
            String nickname
    ) {
        return User.builder()
                .email(createEmail())
                .userBasicInfo(createUserBasicInfo(nickname))
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

    private UserBasicInfo createUserBasicInfo(
            String nickname
    ) {
        return UserBasicInfo.builder()
                .nickname(nickname)
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