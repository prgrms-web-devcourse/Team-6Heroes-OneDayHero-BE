package com.sixheroes.onedayheroapplication.missionproposal;

import com.sixheroes.onedayheroapplication.IntegrationApplicationEventTest;
import com.sixheroes.onedayheroapplication.missionproposal.event.dto.MissionProposalApproveEvent;
import com.sixheroes.onedayheroapplication.missionproposal.event.dto.MissionProposalCreateEvent;
import com.sixheroes.onedayheroapplication.missionproposal.event.dto.MissionProposalRejectEvent;
import com.sixheroes.onedayheroapplication.missionproposal.request.MissionProposalCreateServiceRequest;
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

import static org.assertj.core.api.Assertions.assertThat;

class MissionProposalServiceEventTest extends IntegrationApplicationEventTest {

    @DisplayName("미션 제안을 생성할 때 알림 이벤트를 발행한다.")
    @Transactional
    @Test
    void callEventWhenCreateMissionProposal() {
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
        var missionProposalCreateEventOptional= applicationEvents.stream(MissionProposalCreateEvent.class).findFirst();
        assertThat(missionProposalCreateEventOptional).isNotEmpty();
        var missionProposalCreateEvent = missionProposalCreateEventOptional.get();
        assertThat(missionProposalCreateEvent.missionProposalId()).isEqualTo(missionProposalCreateResponse.id());
    }

    @DisplayName("미션 제안을 승낙할 때 알림 이벤트를 발행한다.")
    @Transactional
    @Test
    void callEventWhenApproveMissionProposal() {
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
        var missionProposalCreateEventOptional= applicationEvents.stream(MissionProposalApproveEvent.class).findFirst();
        assertThat(missionProposalCreateEventOptional).isNotEmpty();
        var missionProposalCreateEvent = missionProposalCreateEventOptional.get();
        assertThat(missionProposalCreateEvent.missionProposalId()).isEqualTo(missionProposalApproveResponse.id());
    }

    @DisplayName("미션 제안을 거절할 때 알림 이벤트를 발행한다.")
    @Transactional
    @Test
    void callEventWhenRejectMissionProposal() {
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
        var missionProposalCreateEventOptional= applicationEvents.stream(MissionProposalRejectEvent.class).findFirst();
        assertThat(missionProposalCreateEventOptional).isNotEmpty();
        var missionProposalCreateEvent = missionProposalCreateEventOptional.get();
        assertThat(missionProposalCreateEvent.missionProposalId()).isEqualTo(missionProposalRejectResponse.id());
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
