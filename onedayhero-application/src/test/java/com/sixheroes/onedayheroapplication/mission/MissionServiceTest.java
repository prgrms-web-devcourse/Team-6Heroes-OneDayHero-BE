package com.sixheroes.onedayheroapplication.mission;

import com.sixheroes.onedayheroapplication.IntegrationApplicationTest;
import com.sixheroes.onedayheroapplication.mission.request.MissionCreateServiceRequest;
import com.sixheroes.onedayheroapplication.mission.request.MissionInfoServiceRequest;
import com.sixheroes.onedayherodomain.mission.MissionCategory;
import com.sixheroes.onedayherodomain.mission.MissionCategoryCode;
import com.sixheroes.onedayherodomain.mission.repository.MissionCategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@ActiveProfiles("test")
class MissionServiceTest extends IntegrationApplicationTest {

    @Autowired
    private MissionCategoryRepository missionCategoryRepository;

    @Autowired
    private MissionService missionService;

    @BeforeEach
    void setUp() {
        List<MissionCategory> missionCategories = Arrays.stream(MissionCategoryCode.values())
                .map(MissionCategory::from)
                .toList();

        missionCategoryRepository.saveAll(missionCategories);
    }

    @DisplayName("시민은 미션을 생성 할 수 있다.")
    @Test
    void createMission() {
        // given
        var missionDate = LocalDate.of(2023, 10, 10);
        var startTime = LocalTime.of(10, 0);
        var endTime = LocalTime.of(10, 30);
        var deadlineTime = LocalTime.of(10, 0);

        var missionInfoServiceRequest = createMissionInfoServiceRequest(missionDate, startTime, endTime, deadlineTime);
        var missionCreateServiceRequest = createMissionCreateServiceRequest(missionInfoServiceRequest);

        // when
        var result = missionService.createMission(missionCreateServiceRequest);

        // then
        assertThat(result).isNotNull();
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