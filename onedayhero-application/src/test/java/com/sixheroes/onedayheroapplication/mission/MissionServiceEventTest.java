package com.sixheroes.onedayheroapplication.mission;

import com.sixheroes.onedayheroapplication.IntegrationApplicationEventTest;
import com.sixheroes.onedayheroapplication.mission.event.dto.MissionCompletedEvent;
import com.sixheroes.onedayherodomain.mission.Mission;
import com.sixheroes.onedayherodomain.mission.MissionCategory;
import com.sixheroes.onedayherodomain.mission.MissionInfo;
import com.sixheroes.onedayherodomain.mission.MissionStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;

class MissionServiceEventTest extends IntegrationApplicationEventTest {

    @DisplayName("시민은 미션을 완료 상태로 변경 할 때 이벤트를 발행한다.")
    @Transactional
    @Test
    void completeMission() {
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

        // when
        var missionResponse = missionService.completeMission(savedMission.getId(), citizenId);

        // then
        var missionCompletedEventOptional = applicationEvents.stream(MissionCompletedEvent.class).findFirst();
        assertThat(missionCompletedEventOptional).isNotEmpty();
        var missionCompletedEvent = missionCompletedEventOptional.get();
        assertThat(missionCompletedEvent.missionId()).isEqualTo(savedMission.getId());
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