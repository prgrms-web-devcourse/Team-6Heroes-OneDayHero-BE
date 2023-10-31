package com.sixheroes.onedayheroinfraquerydsl.mission;

import com.sixheroes.onedayherodomain.mission.*;
import com.sixheroes.onedayherodomain.mission.repository.MissionCategoryRepository;
import com.sixheroes.onedayherodomain.mission.repository.MissionRepository;
import com.sixheroes.onedayheroinfraquerydsl.IntegrationQueryDslTest;
import com.sixheroes.onedayheroquerydsl.mission.MissionRepositoryCustom;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Point;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

class MissionRepositoryCustomImplTest extends IntegrationQueryDslTest {

    @Autowired
    private MissionCategoryRepository missionCategoryRepository;

    @Autowired
    private MissionRepository missionRepository;

    @Autowired
    private MissionRepositoryCustom missionRepositoryCustom;

    @BeforeAll
    public static void setUp(@Autowired MissionCategoryRepository missionCategoryRepository) {
        var missionCategories = Arrays.stream(MissionCategoryCode.values())
                .map(MissionCategory::from)
                .toList();

        missionCategoryRepository.saveAll(missionCategories);
    }

    @Transactional
    @DisplayName("카테고리 ID로 미션을 조회 할 수 있다.")
    @Test
    void findByCategoryId() {
        // given
        var missionCategory = missionCategoryRepository.findById(1L).get();

        var missionDate = LocalDate.of(2023, 10, 10);
        var startTime = LocalTime.of(10, 0);
        var endTime = LocalTime.of(10, 30);
        var deadlineTime = LocalTime.of(10, 0);

        var endTimeB = LocalTime.of(11, 0);

        var missionInfoA = createMissionInfo(missionDate, startTime, endTime, deadlineTime);
        var missionInfoB = createMissionInfo(missionDate, startTime, endTimeB, deadlineTime);


        var missionA = createMission(1L, missionCategory, missionInfoA);
        var missionB = createMission(2L, missionCategory, missionInfoB);

        missionRepository.save(missionA);
        missionRepository.save(missionB);

        // when
        var missions = missionRepositoryCustom.findByCategoryId(1L);

        // then
        assertThat(missions).hasSize(2);
    }

    private MissionInfo createMissionInfo(
            LocalDate missionDate,
            LocalTime startTime,
            LocalTime endTime,
            LocalTime deadlineTime
    ) {
        return MissionInfo.builder()
                .content("내용")
                .missionDate(missionDate)
                .startTime(startTime)
                .endTime(endTime)
                .deadlineTime(deadlineTime)
                .price(10000)
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
}