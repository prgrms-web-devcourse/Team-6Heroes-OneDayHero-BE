package com.sixheroes.onedayheroapplication.missionproposal;

import com.sixheroes.onedayheroapplication.IntegrationApplicationTest;
import com.sixheroes.onedayheroapplication.missionproposal.request.MissionProposalCreateServiceRequest;
import com.sixheroes.onedayherocommon.error.ErrorCode;
import com.sixheroes.onedayherodomain.mission.Mission;
import com.sixheroes.onedayherodomain.mission.MissionCategory;
import com.sixheroes.onedayherodomain.mission.MissionCategoryCode;
import com.sixheroes.onedayherodomain.mission.MissionInfo;
import com.sixheroes.onedayherodomain.missionproposal.MissionProposal;
import com.sixheroes.onedayherodomain.user.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MissionProposalServiceTest extends IntegrationApplicationTest {

    @DisplayName("미션 제안을 생성한다.")
    @Transactional
    @Test
    void createMissionProposal() {
        // given
        var citizenId = 1L;
        var missionCategory = missionCategoryRepository.save(createMissionCategory());
        var mission = missionRepository.save(createMission(citizenId, missionCategory));

        var hero = userRepository.save(createUser());
        hero.changeHeroModeOn();

        var missionProposalCreateServiceRequest = new MissionProposalCreateServiceRequest(
                mission.getId(),
                hero.getId()
        );

        // when
        var missionProposalCreateResponse = missionProposalService.createMissionProposal(citizenId, missionProposalCreateServiceRequest);

        // then
        var missionProposalId = missionProposalCreateResponse.id();
        var missionProposal = missionProposalRepository.findById(missionProposalId);
        assertThat(missionProposal).isNotEmpty();
    }

    @DisplayName("미션 제안을 생성할 때 해당 미션이 존재하지 않으면 예외가 발생한다.")
    @Transactional
    @Test
    void createMissionProposalNotExistMission() {
        // given
        var citizenId = 1L;
        var missionId = 1L;

        var hero = userRepository.save(createUser());
        hero.changeHeroModeOn();

        var missionProposalCreateServiceRequest = new MissionProposalCreateServiceRequest(
                missionId,
                hero.getId()
        );

        // when & then
        assertThatThrownBy(() -> missionProposalService.createMissionProposal(citizenId, missionProposalCreateServiceRequest))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage(ErrorCode.EM_008.name());
    }

    @DisplayName("미션 제안을 생성할 때 해당 히어로가 존재하지 않으면 예외가 발생한다.")
    @Transactional
    @Test
    void createMissionProposalNotExistHero() {
        // given
        var citizenId = 1L;
        var missionCategory = missionCategoryRepository.save(createMissionCategory());
        var mission = missionRepository.save(createMission(citizenId, missionCategory));

        var hero = userRepository.save(createUser());
        hero.changeHeroModeOn();

        var notExistHeroId = 4L;
        var missionProposalCreateServiceRequest = new MissionProposalCreateServiceRequest(
                mission.getId(),
                notExistHeroId
        );

        // when & then
        assertThatThrownBy(() -> missionProposalService.createMissionProposal(citizenId, missionProposalCreateServiceRequest))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage(ErrorCode.EUC_000.name());
    }

    @DisplayName("미션 제안을 승낙한다.")
    @Transactional
    @Test
    void approveMissionProposal() {
        // given
        var citizenId = 1L;
        var missionCategory = missionCategoryRepository.save(createMissionCategory());
        var mission = missionRepository.save(createMission(citizenId, missionCategory));

        var heroId = 1L;
        var missionProposal = missionProposalRepository.save(createMissionProposal(mission.getId(), heroId));

        // when
        var missionProposalApproveResponse = missionProposalService.approveMissionProposal(
                heroId,
                missionProposal.getId()
        );

        // then
        assertThat(missionProposalApproveResponse.id()).isEqualTo(missionProposal.getId());
    }

    @DisplayName("미션 제안이 존재하지 않으면 미션 제안을 승낙할 때 예외가 발생한다.")
    @Transactional
    @Test
    void doNotApproveMissionProposalWhenNotExsistRequest() {
        // given
        var heroId = 1L;
        var missionProposalId = 1L;

        // when
        assertThatThrownBy(() -> missionProposalService.approveMissionProposal(heroId, missionProposalId))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage(ErrorCode.EMP_000.name());
    }

    @DisplayName("미션이 존재하지 않으면 미션 제안을 승낙할 때 예외가 발생한다.")
    @Transactional
    @Test
    void doNotApproveMissionProposalWhenNotExsistMission() {
        // given
        var missionId = 1L;
        var heroId = 1L;
        var missionProposal = missionProposalRepository.save(createMissionProposal(missionId, heroId));
        var missionProposalId = missionProposal.getId();

        // when
        assertThatThrownBy(() -> missionProposalService.approveMissionProposal(heroId, missionProposalId))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage(ErrorCode.EM_008.name());
    }

    @DisplayName("미션 제안을 거절한다.")
    @Transactional
    @Test
    void rejectMissionProposal() {
        // given
        var citizenId = 1L;
        var missionCategory = missionCategoryRepository.save(createMissionCategory());
        var mission = missionRepository.save(createMission(citizenId, missionCategory));

        var heroId = 1L;
        var missionProposal = missionProposalRepository.save(createMissionProposal(mission.getId(), heroId));

        // when
        var missionProposalRejectResponse = missionProposalService.rejectMissionProposal(
                heroId,
                missionProposal.getId()
        );

        // then
        assertThat(missionProposalRejectResponse.id()).isEqualTo(missionProposal.getId());
    }

    @DisplayName("미션 제안이 존재하지 않으면 미션 제안을 거절할 때 예외가 발생한다.")
    @Transactional
    @Test
    void doNotRejectMissionProposalWhenNotExsistRequest() {
        // given
        var heroId = 1L;
        var missionProposalId = 1L;

        // when
        assertThatThrownBy(() -> missionProposalService.rejectMissionProposal(heroId, missionProposalId))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage(ErrorCode.EMP_000.name());
    }

    @DisplayName("미션이 존재하지 않으면 미션 제안을 승낙할 때 예외가 발생한다.")
    @Transactional
    @Test
    void doNotRejectMissionProposalWhenNotExsistMission() {
        // given
        var notExistMissionId = 1L;
        var heroId = 1L;
        var missionProposal = missionProposalRepository.save(createMissionProposal(notExistMissionId, heroId));
        var missionProposalId = missionProposal.getId();

        // when
        assertThatThrownBy(() -> missionProposalService.rejectMissionProposal(heroId, missionProposalId))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage(ErrorCode.EM_008.name());
    }

    private MissionProposal createMissionProposal(
            Long missionId,
            Long heroId
    ) {
        return MissionProposal.builder()
                .missionId(missionId)
                .heroId(heroId)
                .build();
    }

    private Mission createMission(
            Long citizenId,
            MissionCategory missionCategory
    ) {
        return Mission.createMission(missionCategory,
                citizenId,
                1L,
                123.123,
                123.123,
                createMissionInfo());
    }

    private MissionInfo createMissionInfo() {
        return MissionInfo.builder()
                .title("미션 제목")
                .content("미션 내용입니다.")
                .missionDate(LocalDate.of(2023, 10, 30))
                .startTime(LocalTime.of(10, 0, 0))
                .endTime(LocalTime.of(10, 30, 0))
                .deadlineTime(LocalDateTime.of(2023, 10, 29, 20, 0, 0))
                .price(1000)
                .serverTime(LocalDateTime.of(2023, 10, 28, 10, 0, 0))
                .build();
    }

    private MissionCategory createMissionCategory() {
        return MissionCategory.from(MissionCategoryCode.MC_001);
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
