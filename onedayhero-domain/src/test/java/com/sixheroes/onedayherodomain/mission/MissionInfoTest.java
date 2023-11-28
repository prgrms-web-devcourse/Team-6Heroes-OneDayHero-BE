package com.sixheroes.onedayherodomain.mission;

import com.sixheroes.onedayherocommon.exception.BusinessException;
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
        var serverTime = LocalDateTime.of(2023, 10, 10, 0, 0);

        var title = "제목";
        var content = "내용";
        var missionDate = LocalDate.of(2023, 10, 10);
        var startTime = LocalTime.of(10, 0);
        var endTime = LocalTime.of(10, 30);
        var deadlineTime = LocalDateTime.of(missionDate, startTime);
        var price = 1000;

        var missionInfo = MissionInfo.builder()
                .title(title)
                .content(content)
                .missionDate(missionDate)
                .startTime(startTime)
                .endTime(endTime)
                .deadlineTime(deadlineTime)
                .price(1000)
                .serverTime(serverTime)
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
        assertThatThrownBy(() -> createMissionInfo("제목", content))
                .isInstanceOf(BusinessException.class);
    }

    @DisplayName("미션 정보를 입력 받을 때 미션의 내용은 1000자를 초과 할 수 없다.")
    @Test
    void MissionInfoOutOfRangeContent() {
        // given
        var content = new String(new char[1001]).replace('\0', 'a');

        // when & then
        assertThatThrownBy(() -> createMissionInfo("제목", content))
                .isInstanceOf(BusinessException.class);
    }

    @DisplayName("미션 정보를 입력 받을 때 미션의 제목은 공백 일 수 없다.")
    @ParameterizedTest
    @ValueSource(strings = {"", " "})
    void MissionInfoWithEmptyTitle(String title) {

        // when & then
        assertThatThrownBy(() -> createMissionInfo(title, "content"))
                .isInstanceOf(BusinessException.class);
    }

    @DisplayName("미션 정보를 입력 받을 때 미션의 제목은 100자를 초과 할 수 없다.")
    @Test
    void MissionInfoOutOfRangeTitle() {
        // given
        var title = new String(new char[101]).replace('\0', 'a');

        // when & then
        assertThatThrownBy(() -> createMissionInfo(title, "content"))
                .isInstanceOf(BusinessException.class);
    }

    @DisplayName("미션 정보를 입력 받을 때 미션의 수행 날짜가 생성 날짜보다 이전 일 수 없다.")
    @Test
    void MissionInfoWithMissionDateBeforeToday() {
        // given
        var serverTime = LocalDateTime.of(2023, 10, 21, 0, 0);

        var missionDate = LocalDate.of(2023, 10, 20);
        var startTime = LocalTime.of(10, 0, 0);
        var endTime = LocalTime.of(10, 30, 0);
        var deadlineTime = LocalDateTime.of(missionDate, startTime);


        // when & then
        assertThatThrownBy(() -> createMissionInfo(missionDate, startTime, endTime, deadlineTime, serverTime))
                .isInstanceOf(BusinessException.class);
    }

    @DisplayName("미션 정보를 입력 받을 때 미션의 종료 시간이 시작 시간 이전 일 수 없다.")
    @Test
    void MissionInfoWithEndTimeBeforeStartTime() {
        // given
        var serverTime = LocalDateTime.of(2023, 10, 20, 0, 0);

        var missionDate = LocalDate.of(2023, 10, 20);
        var startTime = LocalTime.of(10, 0, 0);
        var endTime = LocalTime.of(9, 30, 0);
        var deadlineTime = LocalDateTime.of(missionDate, startTime);

        // when & then
        assertThatThrownBy(() -> createMissionInfo(missionDate, startTime, endTime, deadlineTime, serverTime))
                .isInstanceOf(BusinessException.class);
    }

    @DisplayName("미션 정보를 입력 받을 때 미션의 마감 시간이 시작 시간 이후 일 수 없다.")
    @Test
    void MissionInfoWithDeadLineTimeAfterStartTime() {
        // given
        var serverTime = LocalDateTime.of(2023, 10, 20, 0, 0);

        var missionDate = LocalDate.of(2023, 10, 20);
        var startTime = LocalTime.of(10, 0, 0);
        var endTime = LocalTime.of(10, 30, 0);
        var deadlineTime = LocalDateTime.of(missionDate, startTime.plusMinutes(30));

        // when & then
        assertThatThrownBy(() -> createMissionInfo(missionDate, startTime, endTime, deadlineTime, serverTime))
                .isInstanceOf(BusinessException.class);
    }


    private MissionInfo createMissionInfo(
            LocalDate missionDate,
            LocalTime startTime,
            LocalTime endTime,
            LocalDateTime deadlineTime,
            LocalDateTime serverTime
    ) {
        return MissionInfo.builder()
                .title("title")
                .content("content")
                .missionDate(missionDate)
                .startTime(startTime)
                .endTime(endTime)
                .deadlineTime(deadlineTime)
                .price(1000)
                .serverTime(serverTime)
                .build();
    }

    private MissionInfo createMissionInfo(
            String title,
            String content
    ) {
        return MissionInfo.builder()
                .title(title)
                .content(content)
                .missionDate(LocalDate.of(2023, 10, 10))
                .startTime(LocalTime.of(10, 0, 0))
                .endTime(LocalTime.of(10, 30, 0))
                .deadlineTime(LocalDateTime.of(
                        LocalDate.of(2023, 10, 10),
                        LocalTime.of(10, 0, 0)
                ))
                .price(1000)
                .serverTime(LocalDateTime.of(
                        LocalDate.of(2023, 10, 9),
                        LocalTime.MIDNIGHT))
                .build();
    }
}