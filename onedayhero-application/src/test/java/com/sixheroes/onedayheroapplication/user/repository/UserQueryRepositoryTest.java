package com.sixheroes.onedayheroapplication.user.repository;

import com.sixheroes.onedayheroapplication.IntegrationApplicationTest;
import com.sixheroes.onedayheroapplication.user.DefaultNicknameGenerator;
import com.sixheroes.onedayheroapplication.user.repository.dto.HeroRankQueryResponse;
import com.sixheroes.onedayherodomain.user.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class UserQueryRepositoryTest extends IntegrationApplicationTest {

    @DisplayName("히어로 지수가 높은 순으로 조회한다")
    @Transactional
    @Test
    void findHeroesRank() {
        // given
        var users = createUser();
        var regionId = createUserRegion(users);
        var missionCategoryId = createUserMissionCategory(users);

        var request = PageRequest.of(0, 3);

        // when
        var heroesRank = userQueryRepository.findHeroesRank(regionId, missionCategoryId, request);

        // then
        assertThat(heroesRank).hasSize(3)
            .isSortedAccordingTo(Comparator.comparing(HeroRankQueryResponse::heroScore).reversed());
    }

    private List<User> createUser() {
        var email = Email.builder()
            .email("abc@123.com")
            .build();

        var userBasicInfo1 = UserBasicInfo.initStatus(DefaultNicknameGenerator.generate());
        var user1 = User.signUp(email, UserSocialType.KAKAO, UserRole.MEMBER, userBasicInfo1);

        var userBasicInfo2 = UserBasicInfo.initStatus(DefaultNicknameGenerator.generate());
        var user2 = User.signUp(email, UserSocialType.KAKAO, UserRole.MEMBER, userBasicInfo2);

        var userBasicInfo3 = UserBasicInfo.initStatus(DefaultNicknameGenerator.generate());
        var user3 = User.signUp(email, UserSocialType.KAKAO, UserRole.MEMBER, userBasicInfo3);

        var users = userRepository.saveAll(List.of(user1, user2, user3));

        user1.minusHeroScore(10);
        user3.sumHeroScore(20);

        return users;
    }

    private Long createUserRegion(
        List<User> users
    ) {
        var regions = regionRepository.findAll();
        var regionId = regions.get(0).getId();
        for (User user : users) {
            var userRegion = UserRegion.builder()
                .user(user)
                .regionId(regionId)
                .build();
            userRegionRepository.save(userRegion);
        }
        return regionId;
    }

    private Long createUserMissionCategory(
        List<User> users
    ) {
        var missionCategories = missionCategoryRepository.findAll();
        var missionCategoryId = missionCategories.get(0).getId();
        for (User user : users) {
            var userMissionCategory = UserMissionCategory.builder()
                .missionCategoryId(missionCategoryId)
                .user(user)
                .build();
            userMissionCategoryRepository.save(userMissionCategory);
        }
        return missionCategoryId;
    }
}