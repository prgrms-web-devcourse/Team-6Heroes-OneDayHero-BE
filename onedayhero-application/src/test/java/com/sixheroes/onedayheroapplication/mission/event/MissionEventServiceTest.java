package com.sixheroes.onedayheroapplication.mission.event;

import com.sixheroes.onedayheroapplication.IntegrationApplicationEventTest;
import com.sixheroes.onedayheroapplication.alarm.dto.AlarmPayload;
import com.sixheroes.onedayheroapplication.mission.event.dto.MissionCompletedEvent;
import com.sixheroes.onedayheroapplication.mission.event.dto.MissionEventAction;
import com.sixheroes.onedayherodomain.mission.Mission;
import com.sixheroes.onedayherodomain.mission.MissionCategory;
import com.sixheroes.onedayherodomain.mission.MissionInfo;
import com.sixheroes.onedayherodomain.mission.MissionStatus;
import com.sixheroes.onedayherodomain.missionmatch.MissionMatch;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;

class MissionEventServiceTest extends IntegrationApplicationEventTest {

    @DisplayName("미션 완료 알림을 위한 데이터를 조회하고 알림 이벤트를 발행한다.")
    @Transactional
    @Test
    void notifyMissionCompleted() {
        // given
        var citizenId = 1L;
        var serverTime = LocalDateTime.of(2023, 10, 10, 0, 0);

        var missionCategory = missionCategoryRepository.findById(1L).get();

        var missionDate = LocalDate.of(2023, 10, 10);
        var startTime = LocalTime.of(10, 0);
        var endTime = LocalTime.of(10, 30);
        var deadlineTime = LocalDateTime.of(missionDate, startTime);

        var mission = createMission(
            missionCategory,
            citizenId,
            missionDate,
            startTime,
            endTime,
            deadlineTime,
            serverTime,
            MissionStatus.MATCHING_COMPLETED
        );
        var savedMission = missionRepository.save(mission);

        var missionMatch = createMissionMatch(mission.getId());
        missionMatchRepository.save(missionMatch);

        var missionCompletedEvent = new MissionCompletedEvent(savedMission.getId());

        // when
        missionEventService.notifyMissionCompleted(missionCompletedEvent);

        // then
        var alarmPayloadOptional = applicationEvents.stream(AlarmPayload.class).findFirst();
        assertThat(alarmPayloadOptional).isNotEmpty();
        var alarmPayload = alarmPayloadOptional.get();
        assertThat(alarmPayload.alarmType()).isEqualTo(MissionEventAction.MISSION_COMPLETED.name());
        assertThat(alarmPayload.userId()).isEqualTo(missionMatch.getHeroId());
        assertThat(alarmPayload.data()).contains(
            entry("missionId", savedMission.getId()),
            entry("missionTitle", savedMission.getMissionInfo().getTitle())
        );
    }

    private MissionMatch createMissionMatch(
        Long missionId
    ) {
        return MissionMatch.createMissionMatch(missionId, 1L);
    }

    private Mission createMission(
        MissionCategory missionCategory,
        Long citizenId,
        LocalDate missionDate,
        LocalTime startTime,
        LocalTime endTime,
        LocalDateTime deadlineTime,
        LocalDateTime serverTime,
        MissionStatus missionStatus
    ) {
        return Mission.builder()
            .missionCategory(missionCategory)
            .missionInfo(
                MissionInfo.builder()
                    .title("title")
                    .content("content")
                    .missionDate(missionDate)
                    .startTime(startTime)
                    .endTime(endTime)
                    .deadlineTime(deadlineTime)
                    .price(1000)
                    .serverTime(serverTime)
                    .build())
            .regionId(1L)
            .citizenId(citizenId)
            .location(Mission.createPoint(123456.78, 123456.89))
            .bookmarkCount(0)
            .missionStatus(missionStatus)
            .build();
    }
}