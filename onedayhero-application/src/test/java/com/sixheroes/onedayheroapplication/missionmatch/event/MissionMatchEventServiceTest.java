package com.sixheroes.onedayheroapplication.missionmatch.event;

import com.sixheroes.onedayheroapplication.IntegrationApplicationEventTest;
import com.sixheroes.onedayheroapplication.notification.dto.AlarmPayload;
import com.sixheroes.onedayheroapplication.missionmatch.event.dto.MissionMatchAction;
import com.sixheroes.onedayheroapplication.missionmatch.event.dto.MissionMatchCreateEvent;
import com.sixheroes.onedayheroapplication.missionmatch.event.dto.MissionMatchRejectEvent;
import com.sixheroes.onedayherodomain.mission.Mission;
import com.sixheroes.onedayherodomain.mission.MissionInfo;
import com.sixheroes.onedayherodomain.mission.MissionStatus;
import com.sixheroes.onedayherodomain.missionmatch.MissionMatch;
import com.sixheroes.onedayherodomain.user.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;

class MissionMatchEventServiceTest extends IntegrationApplicationEventTest {

    @DisplayName("미션 매칭 생성 알림을 위한 데이터를 조회하고 알림 이벤트를 발행한다.")
    @Transactional
    @Test
    void notifyMissionMatchCreate() {
        // given
        var citizen = createUser();
        userRepository.save(citizen);

        var mission = createMission(citizen.getId());

        var heroId = 3L;
        var missionMatch = createMissionMatch(mission.getId(), heroId);
        missionMatchRepository.save(missionMatch);

        var missionMatchCreateEvent = new MissionMatchCreateEvent(missionMatch.getId());

        // when
        missionMatchEventService.notifyMissionMatchCreate(missionMatchCreateEvent);

        // then
        var alarmPayloadOptional = applicationEvents.stream(AlarmPayload.class).findFirst();
        assertThat(alarmPayloadOptional).isNotEmpty();
        var alarmPayload = alarmPayloadOptional.get();
        assertThat(alarmPayload.alarmType()).isEqualTo(MissionMatchAction.MISSION_MATCH_CREATE.name());
        assertThat(alarmPayload.userId()).isEqualTo(missionMatch.getHeroId());
        assertThat(alarmPayload.data()).contains(
            entry("senderNickname", citizen.getUserBasicInfo().getNickname()),
            entry("missionId", mission.getId()),
            entry("missionTitle", mission.getMissionInfo().getTitle())
        );
    }

    @DisplayName("미션매칭 취소 알림 데이터를 조회하고 시민에게 알림 이벤트를 발행한다.")
    @Transactional
    @Test
    void notifyMissionMatchReject() {
        // given
        var hero = createUser();
        userRepository.save(hero);

        var citizenId = 3L;
        var mission = createMission(citizenId);

        var missionMatch = createMissionMatch(mission.getId(), hero.getId());
        missionMatchRepository.save(missionMatch);

        var missionMatchRejectEvent = MissionMatchRejectEvent.from(hero.getId(), missionMatch);

        // when
        missionMatchEventService.notifyMissionMatchReject(missionMatchRejectEvent);

        // then
        var alarmPayloadOptional = applicationEvents.stream(AlarmPayload.class).findFirst();
        assertThat(alarmPayloadOptional).isNotEmpty();
        var alarmPayload = alarmPayloadOptional.get();
        assertThat(alarmPayload.alarmType()).isEqualTo(MissionMatchAction.MISSION_MATCH_REJECT.name());
        assertThat(alarmPayload.userId()).isEqualTo(citizenId);
        assertThat(alarmPayload.data()).contains(
            entry("senderNickname", hero.getUserBasicInfo().getNickname()),
            entry("missionId", mission.getId()),
            entry("missionTitle", mission.getMissionInfo().getTitle())
        );
    }

    @DisplayName("미션매칭 취소 알림 데이터를 조회하고 히어로에게 알림 이벤트를 발행한다.")
    @Transactional
    @Test
    void notifyMissionMatchRejectCitizen() {
        // given
        var citizen = createUser();
        userRepository.save(citizen);

        var mission = createMission(citizen.getId());

        var heroId = 3L;
        var missionMatch = createMissionMatch(mission.getId(), heroId);
        missionMatchRepository.save(missionMatch);

        var missionMatchRejectEvent = MissionMatchRejectEvent.from(citizen.getId(), missionMatch);

        // when
        missionMatchEventService.notifyMissionMatchReject(missionMatchRejectEvent);

        // then
        var alarmPayloadOptional = applicationEvents.stream(AlarmPayload.class).findFirst();
        assertThat(alarmPayloadOptional).isNotEmpty();
        var alarmPayload = alarmPayloadOptional.get();
        assertThat(alarmPayload.alarmType()).isEqualTo(MissionMatchAction.MISSION_MATCH_REJECT.name());
        assertThat(alarmPayload.userId()).isEqualTo(heroId);
        assertThat(alarmPayload.data()).contains(
            entry("senderNickname", citizen.getUserBasicInfo().getNickname()),
            entry("missionId", mission.getId()),
            entry("missionTitle", mission.getMissionInfo().getTitle())
        );
    }

    private MissionMatch createMissionMatch(
        Long missionId,
        Long heroId
    ) {
        return MissionMatch.createMissionMatch(missionId, heroId);
    }

    private Mission createMission(
        Long citizenId
    ) {
        var mission = Mission.builder()
            .missionInfo(createMissionInfo())
            .missionCategory(missionCategoryRepository.findById(1L).get())
            .bookmarkCount(0)
            .missionStatus(MissionStatus.MATCHING)
            .citizenId(citizenId)
            .regionId(1L)
            .location(Mission.createPoint(1234.56, 1234.78))
            .build();

        return missionRepository.save(mission);
    }

    private Mission createMissionWithStatus(
        Long citizenId,
        MissionStatus missionStatus
    ) {
        var mission = Mission.builder()
            .missionInfo(createMissionInfo())
            .missionCategory(missionCategoryRepository.findById(1L).get())
            .bookmarkCount(0)
            .missionStatus(missionStatus)
            .citizenId(citizenId)
            .regionId(1L)
            .location(Mission.createPoint(1234.56, 1234.56))
            .build();

        return missionRepository.save(mission);
    }

    private MissionInfo createMissionInfo() {
        return MissionInfo.builder()
            .missionDate(LocalDate.of(2023, 10, 10))
            .startTime(LocalTime.of(10, 0))
            .endTime(LocalTime.of(10, 30))
            .deadlineTime(LocalDateTime.of(
                LocalDate.of(2023, 10, 10),
                LocalTime.of(10, 0)
            ))
            .price(10000)
            .title("서빙")
            .content("서빙 도와주기")
            .serverTime(
                LocalDateTime.of(
                    LocalDate.of(2023, 10, 9),
                    LocalTime.MIDNIGHT
                )
            )
            .build();
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
}