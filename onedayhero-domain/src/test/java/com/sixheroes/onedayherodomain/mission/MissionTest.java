package com.sixheroes.onedayherodomain.mission;

import com.sixheroes.onedayherocommon.error.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.data.geo.Point;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.*;

class MissionTest {

    @DisplayName("미션 매칭이 완료 된 상태가 아니라면 미션을 삭제 할 수 있다.")
    @EnumSource(value = MissionStatus.class, mode = EnumSource.Mode.EXCLUDE, names = "MATCHING_COMPLETED")
    @ParameterizedTest
    void deleteMissionWithNotMatchingCompleteStatus(
            MissionStatus missionStatus
    ) {
        // given
        var mission = createMission(missionStatus);

        // when & then
        assertThatCode(() -> mission.validAbleDelete(mission.getCitizenId()))
                .doesNotThrowAnyException();
    }

    @DisplayName("미션 매칭이 완료 된 상태에서는 미션을 삭제 할 수 없다.")
    @Test
    void deleteMissionWithMatchingCompleteStatus() {
        // given
        var mission = createMission(MissionStatus.MATCHING_COMPLETED);

        // when & then
        assertThatThrownBy(() -> mission.validAbleDelete(mission.getCitizenId()))
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
        assertThatThrownBy(() -> mission.validAbleDelete(unknownCitizenId))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(ErrorCode.EM_100.name());
    }

    @DisplayName("시민은 미션을 수정 할 수 있다.")
    @Test
    void updateMission() {
        // given
        var citizenId = 1L;
        var mission = createMission(citizenId);

        var missionCategory = MissionCategory.builder()
                .missionCategoryCode(MissionCategoryCode.MC_002)
                .name(MissionCategoryCode.MC_002.name())
                .build();

        var serverTime = LocalDateTime.of(LocalDate.of(2023, 10, 19), LocalTime.MIDNIGHT);

        var missionDate = LocalDate.of(2023, 10, 20);
        var startTime = LocalTime.of(10, 0, 0);
        var endTime = LocalTime.of(10, 30, 0);
        var deadlineTime = LocalTime.of(10, 0, 0);

        var missionInfo = MissionInfo.builder()
                .content("수정하는 내용")
                .missionDate(missionDate)
                .startTime(startTime)
                .endTime(endTime)
                .deadlineTime(deadlineTime)
                .price(15000)
                .serverTime(serverTime)
                .build();

        var newMission = createMission(citizenId, missionCategory, missionInfo);

        // when
        mission.update(newMission);

        // then
        assertThat(mission)
                .extracting(
                        "missionCategory",
                        "citizenId",
                        "regionId",
                        "location",
                        "missionInfo",
                        "bookmarkCount",
                        "missionStatus"
                )
                .containsExactly(
                        missionCategory,
                        newMission.getCitizenId(),
                        newMission.getRegionId(),
                        newMission.getLocation(),
                        missionInfo,
                        0,
                        MissionStatus.MATCHING
                );
    }

    @DisplayName("본인의 미션이 아니라면 수정이 불가능하다.")
    @Test
    void updateMissionWithInvalidCitizen() {
        // given
        var citizenId = 1L;
        var mission = createMission(citizenId);

        var missionCategory = MissionCategory.builder()
                .missionCategoryCode(MissionCategoryCode.MC_002)
                .name(MissionCategoryCode.MC_002.name())
                .build();

        var serverTime = LocalDateTime.of(LocalDate.of(2023, 10, 19), LocalTime.MIDNIGHT);

        var missionDate = LocalDate.of(2023, 10, 20);
        var startTime = LocalTime.of(10, 0, 0);
        var endTime = LocalTime.of(10, 30, 0);
        var deadlineTime = LocalTime.of(10, 0, 0);

        var missionInfo = MissionInfo.builder()
                .content("수정하는 내용")
                .missionDate(missionDate)
                .startTime(startTime)
                .endTime(endTime)
                .deadlineTime(deadlineTime)
                .price(15000)
                .serverTime(serverTime)
                .build();

        var unknownCitizenId = 2L;
        var newMission = createMission(unknownCitizenId, missionCategory, missionInfo);

        // when & then
        assertThatThrownBy(() -> mission.update(newMission))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(ErrorCode.EM_100.name());
    }

    @DisplayName("미션이 매칭 중인 상태가 아니라면 수정이 불가능하다.")
    @EnumSource(value = MissionStatus.class, mode = EnumSource.Mode.EXCLUDE, names = "MATCHING")
    @ParameterizedTest
    void updateMissionWithNotMatching(
            MissionStatus missionStatus
    ) {
        // given
        var mission = createMission(missionStatus);

        var missionCategory = MissionCategory.builder()
                .missionCategoryCode(MissionCategoryCode.MC_002)
                .name(MissionCategoryCode.MC_002.name())
                .build();

        var serverTime = LocalDateTime.of(LocalDate.of(2023, 10, 19), LocalTime.MIDNIGHT);

        var missionDate = LocalDate.of(2023, 10, 20);
        var startTime = LocalTime.of(10, 0, 0);
        var endTime = LocalTime.of(10, 30, 0);
        var deadlineTime = LocalTime.of(10, 0, 0);

        var missionInfo = MissionInfo.builder()
                .content("수정하는 내용")
                .missionDate(missionDate)
                .startTime(startTime)
                .endTime(endTime)
                .deadlineTime(deadlineTime)
                .price(15000)
                .serverTime(serverTime)
                .build();

        var newMission = createMission(mission.getCitizenId(), missionCategory, missionInfo);

        // when & then
        assertThatThrownBy(() -> mission.update(newMission))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(ErrorCode.EM_009.name());
    }

    @DisplayName("미션이 만료된 상태가 아니라면 연장이 불가능하다.")
    @EnumSource(value = MissionStatus.class, mode = EnumSource.Mode.EXCLUDE, names = "EXPIRED")
    @ParameterizedTest
    void extendMissionWithNotExpired(MissionStatus missionStatus) {
        // given
        var mission = createMission(missionStatus);

        // when & then
        assertThatThrownBy(() -> mission.extend(mission))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(ErrorCode.T_001.name());
    }

    private Mission createMission(
            MissionStatus missionStatus
    ) {
        return Mission.builder()
                .missionCategory(
                        MissionCategory.builder()
                                .missionCategoryCode(MissionCategoryCode.MC_001)
                                .name(MissionCategoryCode.MC_001.getDescription())
                                .build())
                .missionInfo(
                        MissionInfo.builder()
                                .content("content")
                                .missionDate(LocalDate.of(2023, 11, 1))
                                .startTime(LocalTime.of(12, 30))
                                .endTime(LocalTime.of(14, 30))
                                .deadlineTime(LocalTime.of(12, 0))
                                .price(1000)
                                .serverTime(LocalDateTime.of(
                                        LocalDate.of(2023, 10, 31),
                                        LocalTime.MIDNIGHT
                                ))
                                .build())
                .regionId(1L)
                .citizenId(1L)
                .location(new Point(123456.78, 123456.78))
                .missionStatus(missionStatus)
                .bookmarkCount(0)
                .build();
    }

    private Mission createMission(
            Long citizenId,
            MissionCategory missionCategory,
            MissionInfo missionInfo
    ) {
        return Mission.builder()
                .missionCategory(missionCategory)
                .missionInfo(missionInfo)
                .regionId(1L)
                .citizenId(citizenId)
                .location(new Point(123456.78, 123456.78))
                .missionStatus(MissionStatus.MATCHING)
                .bookmarkCount(0)
                .build();
    }

    private Mission createMission(
            Long citizenId
    ) {
        return Mission.builder()
                .missionCategory(
                        MissionCategory.builder()
                                .missionCategoryCode(MissionCategoryCode.MC_001)
                                .name(MissionCategoryCode.MC_001.getDescription())
                                .build())
                .missionInfo(
                        MissionInfo.builder()
                                .content("content")
                                .missionDate(LocalDate.of(2023, 11, 1))
                                .startTime(LocalTime.of(12, 30))
                                .endTime(LocalTime.of(14, 30))
                                .deadlineTime(LocalTime.of(12, 0))
                                .price(1000)
                                .serverTime(LocalDateTime.of(
                                        LocalDate.of(2023, 10, 31),
                                        LocalTime.MIDNIGHT
                                ))
                                .build())
                .regionId(1L)
                .citizenId(citizenId)
                .location(new Point(123456.78, 123456.78))
                .missionStatus(MissionStatus.MATCHING)
                .bookmarkCount(0)
                .build();
    }
}