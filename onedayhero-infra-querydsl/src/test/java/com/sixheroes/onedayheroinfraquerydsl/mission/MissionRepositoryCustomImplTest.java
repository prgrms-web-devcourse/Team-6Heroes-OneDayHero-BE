package com.sixheroes.onedayheroinfraquerydsl.mission;

import com.sixheroes.onedayherodomain.mission.*;
import com.sixheroes.onedayherodomain.mission.repository.MissionCategoryRepository;
import com.sixheroes.onedayherodomain.mission.repository.MissionRepository;
import com.sixheroes.onedayherodomain.region.Region;
import com.sixheroes.onedayherodomain.region.repository.RegionRepository;
import com.sixheroes.onedayheroinfraquerydsl.IntegrationQueryDslTest;
import com.sixheroes.onedayheroquerydsl.mission.MissionQueryRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Point;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

class MissionRepositoryCustomImplTest extends IntegrationQueryDslTest {

    @Autowired
    private RegionRepository regionRepository;

    @Autowired
    private MissionCategoryRepository missionCategoryRepository;

    @Autowired
    private MissionRepository missionRepository;

    @Autowired
    private MissionQueryRepository missionQueryRepository;

    @BeforeAll
    public static void setUp(
            @Autowired MissionCategoryRepository missionCategoryRepository,
            @Autowired RegionRepository regionRepository
    ) {
        var missionCategories = Arrays.stream(MissionCategoryCode.values())
                .map(MissionCategory::from)
                .toList();

        missionCategoryRepository.saveAll(missionCategories);

        var region = Region.builder()
                .si("서울시")
                .gu("강남구")
                .dong("역삼동")
                .build();

        regionRepository.save(region);
    }

    @Transactional
    @DisplayName("카테고리 ID로 미션을 조회 할 수 있다.")
    @Test
    void findByCategoryId() {
        // given
        var missionCategory = missionCategoryRepository.findById(1L).get();

        var serverTime = LocalDateTime.of(LocalDate.of(2023, 10, 9), LocalTime.MIDNIGHT);

        var missionDate = LocalDate.of(2023, 10, 10);
        var startTime = LocalTime.of(10, 0);
        var endTime = LocalTime.of(10, 30);
        var deadlineTime = LocalTime.of(10, 0);

        var endTimeB = LocalTime.of(11, 0);

        var missionInfoA = createMissionInfo(missionDate, startTime, endTime, deadlineTime, serverTime);
        var missionInfoB = createMissionInfo(missionDate, startTime, endTimeB, deadlineTime, serverTime);


        var missionA = createMission(1L, missionCategory, missionInfoA);
        var missionB = createMission(2L, missionCategory, missionInfoB);

        missionRepository.save(missionA);
        missionRepository.save(missionB);

        // when
        var missions = missionQueryRepository.findByCategoryId(1L);

        // then
        assertThat(missions).hasSize(2);
    }

    @Transactional
    @DisplayName("미션을 한 번의 쿼리로 단 건 조회 할 수 있다.")
    @Test
    void findOneWithFetchJoin() {
        // given
        var missionCategory = missionCategoryRepository.findById(1L).get();

        var serverTime = LocalDateTime.of(LocalDate.of(2023, 10, 9), LocalTime.MIDNIGHT);

        var missionDate = LocalDate.of(2023, 10, 10);
        var startTime = LocalTime.of(10, 0);
        var endTime = LocalTime.of(10, 30);
        var deadlineTime = LocalTime.of(10, 0);

        var missionInfo = createMissionInfo(missionDate, startTime, endTime, deadlineTime, serverTime);

        var mission = createMission(1L, missionCategory, missionInfo);

        var savedMission = missionRepository.save(mission);

        // when
        var missionQueryResponse = missionQueryRepository.fetchOne(savedMission.getId());

        // then
        assertThat(missionQueryResponse).isNotEmpty();
    }

    private MissionInfo createMissionInfo(
            LocalDate missionDate,
            LocalTime startTime,
            LocalTime endTime,
            LocalTime deadlineTime,
            LocalDateTime serverTime
    ) {
        return MissionInfo.builder()
                .content("내용")
                .missionDate(missionDate)
                .startTime(startTime)
                .endTime(endTime)
                .deadlineTime(deadlineTime)
                .price(10000)
                .serverTime(serverTime)
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