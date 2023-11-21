package com.sixheroes.onedayheroapplication.missionproposal.event;

import com.sixheroes.onedayheroapplication.IntegrationApplicationTest;
import com.sixheroes.onedayheroapplication.alarm.dto.AlarmPayload;
import com.sixheroes.onedayheroapplication.missionproposal.event.dto.MissionProposalAction;
import com.sixheroes.onedayheroapplication.missionproposal.event.dto.MissionProposalCreateEvent;
import com.sixheroes.onedayherocommon.error.ErrorCode;
import com.sixheroes.onedayherodomain.mission.*;
import com.sixheroes.onedayherodomain.mission.repository.MissionCategoryRepository;
import com.sixheroes.onedayherodomain.mission.repository.MissionRepository;
import com.sixheroes.onedayherodomain.missionproposal.MissionProposal;
import com.sixheroes.onedayherodomain.missionproposal.repository.MissionProposalRepository;
import com.sixheroes.onedayherodomain.user.*;
import com.sixheroes.onedayherodomain.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.RecordApplicationEvents;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.*;

@Transactional
@RecordApplicationEvents
class MissionProposalEventServiceTest extends IntegrationApplicationTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MissionCategoryRepository missionCategoryRepository;

    @Autowired
    private MissionRepository missionRepository;

    @Autowired
    private MissionProposalRepository missionProposalRepository;

    @Autowired
    private MissionProposalEventService missionProposalEventService;

    @Autowired
    private ApplicationEvents applicationEvents;

    @DisplayName("미션 제안 생성 알림을 위한 데이터를 조회한 뒤 알림 이벤트를 발행한다.")
    @Test
    void notifyMissionProposalCreate() {
        // given
        var citizen = createUser();
        userRepository.save(citizen);

        var missionCategory = createMissionCategory();
        missionCategoryRepository.save(missionCategory);

        var mission = createMission(
            missionCategory,
            citizen.getId()
        );
        missionRepository.save(mission);

        var missionProposal = createMissionProposal(mission.getId());
        missionProposalRepository.save(missionProposal);

        var missionProposalCreateEvent = new MissionProposalCreateEvent(missionProposal.getId());

        // when
        missionProposalEventService.notifyMissionProposalCreate(missionProposalCreateEvent);

        // then
        var alarmPayloadOptional = applicationEvents.stream(AlarmPayload.class).findFirst();

        // when
        assertThat(alarmPayloadOptional).isNotEmpty();
        var alarmPayload = alarmPayloadOptional.get();
        assertThat(alarmPayload.alarmType()).isEqualTo(MissionProposalAction.MISSION_PROPOSAL_CREATE.name());
        assertThat(alarmPayload.userId()).isEqualTo(missionProposal.getHeroId());
        assertThat(alarmPayload.data())
            .contains(
                entry("citizenNickname", citizen.getUserBasicInfo().getNickname()),
                entry("missionId", mission.getId()),
                entry("missionTitle", mission.getMissionInfo().getTitle())
            );
    }

    @DisplayName("미션 제안 생성 알림을 위한 데이터를 조회할 때 존재하지 않는 미션 제안 아이디라면 예외가 발생한다.")
    @Test
    void notifyMissionProposalCreateWhenNotExist() {
        // given
        var notExistMissionProposalId = 1L;

        var missionProposalCreateEvent = new MissionProposalCreateEvent(notExistMissionProposalId);

        // when & then
        assertThatThrownBy(() -> missionProposalEventService.notifyMissionProposalCreate(missionProposalCreateEvent))
            .isInstanceOf(NoSuchElementException.class)
            .hasMessage(ErrorCode.EMP_000.name());
    }

    public MissionProposal createMissionProposal(
        Long missionId
    ) {
        return MissionProposal.builder()
            .heroId(2L)
            .missionId(missionId)
            .build();
    }

    private MissionCategory createMissionCategory() {
        return MissionCategory.builder()
            .missionCategoryCode(MissionCategoryCode.MC_001)
            .name(MissionCategoryCode.MC_001.getDescription())
            .build();
    }

    private Mission createMission(
        MissionCategory missionCategory,
        Long citizenId
    ) {
        return Mission.builder()
            .missionCategory(missionCategory)
            .missionInfo(
                MissionInfo.builder()
                    .title("title")
                    .content("content")
                    .missionDate(LocalDate.of(2023, 11, 1))
                    .startTime(LocalTime.of(12, 30))
                    .endTime(LocalTime.of(14, 30))
                    .deadlineTime(LocalDateTime.of(
                        LocalDate.of(2023, 11, 1),
                        LocalTime.of(12, 0)
                    ))
                    .price(1000)
                    .serverTime(LocalDateTime.of(
                        LocalDate.of(2023, 10, 31),
                        LocalTime.MIDNIGHT
                    ))
                    .build())
            .regionId(1L)
            .citizenId(citizenId)
            .location(Mission.createPoint(123456.78, 123456.78))
            .missionStatus(MissionStatus.MATCHING)
            .bookmarkCount(0)
            .build();
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