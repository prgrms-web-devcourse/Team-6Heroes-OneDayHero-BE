package com.sixheroes.onedayherodomain.mission;

import com.sixheroes.onedayherocommon.error.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.data.geo.Point;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MissionTest {

    @DisplayName("미션 매칭이 완료 된 상태가 아니라면 미션을 삭제 할 수 있다.")
    @EnumSource(value = MissionStatus.class, mode = EnumSource.Mode.EXCLUDE, names = "MATCHING_COMPLETED")
    @ParameterizedTest
    void deleteMissionWithNotMatchingCompleteStatus(MissionStatus missionStatus) {
        // given
        var mission = createMission(missionStatus);

        // when & then
        assertThatCode(mission::validAbleDeleteStatus)
                .doesNotThrowAnyException();
    }

    @DisplayName("미션 매칭이 완료 된 상태에서는 미션을 삭제 할 수 없다.")
    @Test
    void deleteMissionWithMatchingCompleteStatus() {
        // given
        var mission = createMission(MissionStatus.MATCHING_COMPLETED);

        // when & then
        assertThatThrownBy(mission::validAbleDeleteStatus)
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(ErrorCode.EM_007.name());
    }

    @DisplayName("미션에 대한 접근은 본인만 가능하다.")
    @Test
    void deleteMissionWithNotOwn() {
        // given
        var mission = createMission(1L);

        var unknownCitizenId = 2L;

        // when & then
        assertThatThrownBy(() -> mission.validOwn(unknownCitizenId))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(ErrorCode.EM_009.name());
    }

    private Mission createMission(MissionStatus missionStatus) {
        return Mission.builder()
                .missionCategory(
                        MissionCategory.builder()
                                .missionCategoryCode(MissionCategoryCode.MC_001)
                                .name(MissionCategoryCode.MC_001.getDescription())
                                .build())
                .missionInfo(
                        MissionInfo.builder()
                                .content("content")
                                .missionDate(LocalDate.now())
                                .startTime(LocalTime.now())
                                .endTime(LocalTime.now())
                                .deadlineTime(LocalTime.now())
                                .price(1000)
                                .build())
                .regionId(1L)
                .citizenId(1L)
                .location(new Point(123456.78, 123456.78))
                .missionStatus(missionStatus)
                .build();
    }

    private Mission createMission(Long citizenId) {
        return Mission.builder()
                .missionCategory(
                        MissionCategory.builder()
                                .missionCategoryCode(MissionCategoryCode.MC_001)
                                .name(MissionCategoryCode.MC_001.getDescription())
                                .build())
                .missionInfo(
                        MissionInfo.builder()
                                .content("content")
                                .missionDate(LocalDate.now())
                                .startTime(LocalTime.now())
                                .endTime(LocalTime.now())
                                .deadlineTime(LocalTime.now())
                                .price(1000)
                                .build())
                .regionId(1L)
                .citizenId(citizenId)
                .location(new Point(123456.78, 123456.78))
                .missionStatus(MissionStatus.MATCHING)
                .build();
    }
}