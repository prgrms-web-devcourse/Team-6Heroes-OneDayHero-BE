package com.sixheroes.onedayheroapplication.missionmatch;

import com.sixheroes.onedayheroapplication.IntegrationApplicationEventTest;
import com.sixheroes.onedayheroapplication.missionmatch.event.dto.MissionMatchCreateEvent;
import com.sixheroes.onedayheroapplication.missionmatch.event.dto.MissionMatchRejectEvent;
import com.sixheroes.onedayheroapplication.missionmatch.request.MissionMatchCancelServiceRequest;
import com.sixheroes.onedayheroapplication.missionmatch.request.MissionMatchCreateServiceRequest;
import com.sixheroes.onedayherodomain.mission.Mission;
import com.sixheroes.onedayherodomain.mission.MissionInfo;
import com.sixheroes.onedayherodomain.mission.MissionStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;

class MissionMatchServiceEventTest extends IntegrationApplicationEventTest {

    @DisplayName("매칭완료 상태를 가지는 미션매칭을 생성할 때 이벤트를 발행한다.")
    @Transactional
    @Test
    void createMissionMatch() {
        // given
        var citizenId = 1L;
        var heroId = 2L;
        var mission = createMission(citizenId);

        // when
        var request = createMissionMatchCreateServiceRequest(
            mission,
            heroId
        );
        var response = missionMatchService.createMissionMatch(
            citizenId,
            request
        );

        // then
        var missionMatchCreateEventOptional = applicationEvents.stream(MissionMatchCreateEvent.class).findFirst();
        assertThat(missionMatchCreateEventOptional).isNotEmpty();
        var missionMatchCreateEvent = missionMatchCreateEventOptional.get();
        assertThat(missionMatchCreateEvent.missionMatchId()).isEqualTo(response.id());
    }

    @DisplayName("미션매칭에 대해 취소 상태로 변경할 때 이벤트를 발행한다.")
    @Transactional
    @Test
    void cancelMissionMatch() {
        // given
        var citizenId = 1L;
        var heroId = 2L;
        var mission = createMission(citizenId);
        missionMatchService.createMissionMatch(
            citizenId,
            createMissionMatchCreateServiceRequest(
                mission,
                heroId
            )
        );

        // when
        var request = createMissionMatchCancelServiceRequest(
            mission.getId()
        );
        var response = missionMatchService.cancelMissionMatch(citizenId, request);

        // then
        var missionMatchCreateEventOptional = applicationEvents.stream(MissionMatchRejectEvent.class).findFirst();
        assertThat(missionMatchCreateEventOptional).isNotEmpty();
        var missionMatchCreateEvent = missionMatchCreateEventOptional.get();
        assertThat(missionMatchCreateEvent.missionMatchId()).isEqualTo(response.id());
    }

    private MissionMatchCreateServiceRequest createMissionMatchCreateServiceRequest(
        Mission mission,
        Long heroId
    ) {
        return MissionMatchCreateServiceRequest.builder()
            .missionId(mission.getId())
            .heroId(heroId)
            .build();
    }

    private MissionMatchCancelServiceRequest createMissionMatchCancelServiceRequest(
        Long missionId
    ) {
        return MissionMatchCancelServiceRequest.builder()
            .missionId(missionId)
            .build();
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
}