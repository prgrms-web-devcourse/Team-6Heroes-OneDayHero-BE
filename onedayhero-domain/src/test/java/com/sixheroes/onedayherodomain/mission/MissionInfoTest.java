package com.sixheroes.onedayherodomain.mission;

import com.sixheroes.onedayherocommon.error.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ActiveProfiles("test")
class MissionInfoTest {

    @DisplayName("미션에 대한 미션 정보를 생성 할 수 있다.")
    @Test
    void createMissionInfo() {
        // given
        var content = "내용";
        var missionDate = LocalDate.of(2023, 10, 10);
        var startTime = LocalTime.of(10, 0);
        var endTime = LocalTime.of(10, 30);
        var deadlineTime = LocalTime.of(10, 0);
        var price = 1000;

        var missionInfo = MissionInfo.builder()
                .content("내용")
                .missionDate(missionDate)
                .startTime(startTime)
                .endTime(endTime)
                .deadlineTime(deadlineTime)
                .price(1000)
                .build();

        // when & then
        assertThat(missionInfo).isNotNull();
        assertThat(missionInfo)
                .extracting("content", "missionDate", "startTime", "endTime", "deadlineTime", "price")
                .containsExactly(content, missionDate, startTime, endTime, deadlineTime, price);
    }

    @DisplayName("미션 정보를 입력 받을 때 미션의 내용은 공백 일 수 없다.")
    @ParameterizedTest
    @ValueSource(strings = {"", " "})
    void MissionInfoWithEmptyContent(String content) {

        // when & then
        assertThatThrownBy(() -> createMissionInfo(content))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorCode.EM_001.name());
    }

    @DisplayName("미션 정보를 입력 받을 때 미션의 내용은 1000자를 초과 할 수 없다.")
    @Test
    void MissionInfoOutOfRangeContent() {
        // given
        var content = new String(new char[1001]).replace('\0', 'a');

        // when & then
        assertThatThrownBy(() -> createMissionInfo(content))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorCode.EM_002.name());
    }

    @DisplayName("미션 정보를 입력 받을 때 미션의 수행 날짜가 생성 날짜보다 이전 일 수 없다.")
    @Test
    void MissionInfoWithMissionDateBeforeToday() {
        // given
        var today = LocalDateTime.of(2023, 10, 21, 0, 0);

        var missionDate = LocalDate.of(2023, 10, 20);
        var startTime = LocalTime.of(10, 0, 0);
        var endTime = LocalTime.of(10, 30, 0);
        var deadlineTime = LocalTime.of(10, 0, 0);

        var missionInfo = createMissionInfo(missionDate, startTime, endTime, deadlineTime);

        // when & then
        assertThatThrownBy(() -> missionInfo.validMissionDateTimeInRange(today))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorCode.EM_003.name());
    }

    @DisplayName("미션 정보를 입력 받을 때 미션의 종료 시간이 시작 시간 이전 일 수 없다.")
    @Test
    void MissionInfoWithEndTimeBeforeStartTime() {
        // given
        var today = LocalDateTime.of(2023, 10, 20, 0, 0);

        var missionDate = LocalDate.of(2023, 10, 20);
        var startTime = LocalTime.of(10, 0, 0);
        var endTime = LocalTime.of(9, 30, 0);
        var deadlineTime = LocalTime.of(10, 0, 0);

        var missionInfo = createMissionInfo(missionDate, startTime, endTime, deadlineTime);

        // when & then
        assertThatThrownBy(() -> missionInfo.validMissionDateTimeInRange(today))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorCode.EM_004.name());
    }

    @DisplayName("미션 정보를 입력 받을 때 미션의 마감 시간이 시작 시간 이후 일 수 없다.")
    @Test
    void MissionInfoWithDeadLineTimeAfterStartTime() {
        // given
        var today = LocalDateTime.of(2023, 10, 20, 0, 0);

        var missionDate = LocalDate.of(2023, 10, 20);
        var startTime = LocalTime.of(10, 0, 0);
        var endTime = LocalTime.of(10, 30, 0);
        var deadlineTime = LocalTime.of(10, 10, 0);

        var missionInfo = createMissionInfo(missionDate, startTime, endTime, deadlineTime);

        // when & then
        assertThatThrownBy(() -> missionInfo.validMissionDateTimeInRange(today))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorCode.EM_005.name());
    }


    private MissionInfo createMissionInfo(
            LocalDate missionDate,
            LocalTime startTime,
            LocalTime endTime,
            LocalTime deadlineTime
    ) {
        return MissionInfo.builder()
                .content("content")
                .missionDate(missionDate)
                .startTime(startTime)
                .endTime(endTime)
                .deadlineTime(deadlineTime)
                .price(1000)
                .build();
    }

    private MissionInfo createMissionInfo(
            String content
    ) {
        return MissionInfo.builder()
                .content(content)
                .missionDate(LocalDate.of(2023, 10, 10))
                .startTime(LocalTime.of(10, 0, 0))
                .endTime(LocalTime.of(10, 30, 0))
                .deadlineTime(LocalTime.of(10, 0, 0))
                .price(1000)
                .build();
    }
}