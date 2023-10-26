package com.sixheroes.onedayheroapplication.mission;

import com.sixheroes.onedayheroapplication.IntegrationApplicationTest;
import com.sixheroes.onedayheroapplication.mission.request.MissionCreateServiceRequest;
import com.sixheroes.onedayheroapplication.mission.request.MissionInfoServiceRequest;
import com.sixheroes.onedayheroapplication.mission.response.MissionCategoryResponse;
import com.sixheroes.onedayherocommon.error.ErrorCode;
import com.sixheroes.onedayherodomain.mission.MissionCategory;
import com.sixheroes.onedayherodomain.mission.MissionCategoryCode;
import com.sixheroes.onedayherodomain.mission.MissionStatus;
import com.sixheroes.onedayherodomain.mission.repository.MissionCategoryRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Point;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MissionServiceTest extends IntegrationApplicationTest {

    @Autowired
    private MissionService missionService;

    @BeforeAll
    public static void setUp(@Autowired MissionCategoryRepository missionCategoryRepository) {
        var missionCategory = MissionCategory.from(MissionCategoryCode.MC_001);
        missionCategoryRepository.save(missionCategory);
    }

    @Transactional
    @DisplayName("시민은 미션을 생성 할 수 있다.")
    @Test
    void createMission() {
        // given
        var today = LocalDateTime.of(2023, 10, 10, 0, 0);

        var missionDate = LocalDate.of(2023, 10, 10);
        var startTime = LocalTime.of(10, 0);
        var endTime = LocalTime.of(10, 30);
        var deadlineTime = LocalTime.of(10, 0);

        var missionInfoServiceRequest = createMissionInfoServiceRequest(missionDate, startTime, endTime, deadlineTime);
        var missionCreateServiceRequest = createMissionCreateServiceRequest(missionInfoServiceRequest);

        // when
        var result = missionService.createMission(missionCreateServiceRequest, today);

        // then
        assertThat(result)
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
                        MissionCategoryResponse.builder()
                                .categoryId(missionCreateServiceRequest.missionCategoryId())
                                .code(MissionCategoryCode.MC_001.name())
                                .name(MissionCategoryCode.MC_001.getDescription())
                                .build(),
                        missionCreateServiceRequest.citizenId(),
                        missionCreateServiceRequest.regionId(),
                        new Point(missionCreateServiceRequest.latitude(), missionCreateServiceRequest.longitude()),
                        result.missionInfo(),
                        0,
                        MissionStatus.MATCHING.name()
                );
    }

    @Transactional
    @DisplayName("시민이 미션을 생성 할 때 미션의 수행 날짜가 생성 날짜보다 이전 일 수 없다.")
    @Test
    void createMissionWithMissionDateBeforeToday() {
        // given
        var today = LocalDateTime.of(2023, 10, 21, 0, 0);

        var missionDate = LocalDate.of(2023, 10, 20);
        var startTime = LocalTime.of(10, 0, 0);
        var endTime = LocalTime.of(10, 30, 0);
        var deadlineTime = LocalTime.of(10, 0, 0);

        var missionInfoServiceRequest = createMissionInfoServiceRequest(missionDate, startTime, endTime, deadlineTime);
        var missionCreateServiceRequest = createMissionCreateServiceRequest(missionInfoServiceRequest);

        // when & then
        assertThatThrownBy(() -> missionService.createMission(missionCreateServiceRequest, today))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorCode.EM_003.name());
    }

    @Transactional
    @DisplayName("시민이 미션을 생성 할 때 미션의 종료 시간이 시작 시간 이전 일 수 없다.")
    @Test
    void createMissionWithEndTimeBeforeStartTime() {
        // given
        var today = LocalDateTime.of(2023, 10, 20, 0, 0);

        var missionDate = LocalDate.of(2023, 10, 20);
        var startTime = LocalTime.of(10, 0, 0);
        var endTime = LocalTime.of(9, 30, 0);
        var deadlineTime = LocalTime.of(10, 0, 0);

        var missionInfoServiceRequest = createMissionInfoServiceRequest(missionDate, startTime, endTime, deadlineTime);
        var missionCreateServiceRequest = createMissionCreateServiceRequest(missionInfoServiceRequest);

        // when & then
        assertThatThrownBy(() -> missionService.createMission(missionCreateServiceRequest, today))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorCode.EM_004.name());
    }

    @Transactional
    @DisplayName("시민이 미션을 생성 할 때 미션의 마감 시간이 시작 시간 이후 일 수 없다.")
    @Test
    void createMissionWithDeadLineTimeAfterStartTime() {
        // given
        var today = LocalDateTime.of(2023, 10, 20, 0, 0);

        var missionDate = LocalDate.of(2023, 10, 20);
        var startTime = LocalTime.of(10, 0, 0);
        var endTime = LocalTime.of(10, 30, 0);
        var deadlineTime = LocalTime.of(10, 10, 0);

        var missionInfoServiceRequest = createMissionInfoServiceRequest(missionDate, startTime, endTime, deadlineTime);
        var missionCreateServiceRequest = createMissionCreateServiceRequest(missionInfoServiceRequest);

        // when & then
        assertThatThrownBy(() -> missionService.createMission(missionCreateServiceRequest, today))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorCode.EM_005.name());
    }

    private MissionCreateServiceRequest createMissionCreateServiceRequest(
            MissionInfoServiceRequest missionInfoServiceRequest
    ) {
        return MissionCreateServiceRequest.builder()
                .missionCategoryId(1L)
                .citizenId(1L)
                .regionId(1L)
                .latitude(1234252.23)
                .longitude(1234277.388)
                .missionInfo(missionInfoServiceRequest)
                .build();
    }

    private MissionInfoServiceRequest createMissionInfoServiceRequest(
            LocalDate missionDate,
            LocalTime startTime,
            LocalTime endTime,
            LocalTime deadlineTime
    ) {
        return MissionInfoServiceRequest
                .builder()
                .content("내용")
                .missionDate(missionDate)
                .startTime(startTime)
                .endTime(endTime)
                .deadlineTime(deadlineTime)
                .price(10000)
                .build();
    }
}