package com.sixheroes.onedayherodomain.mission;

import com.sixheroes.onedayherocommon.error.ErrorCode;
import com.sixheroes.onedayherodomain.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.geo.Point;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MissionTest {

    @DisplayName("미션 소유자이고 미션 매칭 중인 상태일 때 미션을 제안할 수 있다.")
    @Test
    void validPossibleMissionRequest() {
        // given
        var citizenId = 1L;
        var mission = createMission(citizenId);

        // when
        mission.validMissionRequestPossible(citizenId);

        // then
        assertThat(mission.getMissionStatus()).isEqualTo(MissionStatus.MATCHING);
        assertThat(mission.getCitizenId()).isEqualTo(citizenId);
    }

    @DisplayName("미션 소유자가 아닐 때 미션을 제안하면 예외가 발생한다.")
    @Test
    void invalidPossibleMissionRequestWhenNotOwner() {
        // given
        var citizenId = 1L;
        var requestCitizenId = 2L;
        var mission = createMission(citizenId);

        // when & then
        assertThatThrownBy(() -> mission.validMissionRequestPossible(requestCitizenId))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(ErrorCode.EM_007.name());
    }

    private User createHero() {
        return User.builder()
            .
            .build();
    }

    private Mission createMission(
        Long citizenId
    ) {
        return Mission.builder()
            .regionId(1L)
            .citizenId(citizenId)
            .missionCategory(createMissionCategory())
            .missionInfo(createMissionInfo())
            .location(new Point(123.123, 123.123))
            .missionInfo(createMissionInfo())
            .build();
    }

    private MissionInfo createMissionInfo() {
        return MissionInfo.builder()
            .content("미션 내용입니다.")
            .missionDate(LocalDate.of(2023, 10, 10))
            .startTime(LocalTime.of(10, 0, 0))
            .endTime(LocalTime.of(10, 30, 0))
            .deadlineTime(LocalTime.of(10, 0, 0))
            .price(1000)
            .build();
    }

    private MissionCategory createMissionCategory() {
        return MissionCategory.from(MissionCategoryCode.MC_001);
    }
}