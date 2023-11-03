package com.sixheroes.onedayheroinfraquerydsl.mission;

import com.sixheroes.onedayherodomain.mission.*;
import com.sixheroes.onedayherodomain.mission.repository.MissionCategoryRepository;
import com.sixheroes.onedayherodomain.mission.repository.MissionRepository;
import com.sixheroes.onedayherodomain.region.Region;
import com.sixheroes.onedayherodomain.region.repository.RegionRepository;
import com.sixheroes.onedayheroinfraquerydsl.IntegrationQueryDslTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.geo.Point;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class MissionQueryRepositoryTest extends IntegrationQueryDslTest {

    @Autowired
    private RegionRepository regionRepository;

    @Autowired
    private MissionCategoryRepository missionCategoryRepository;

    @Autowired
    private MissionRepository missionRepository;

    @Autowired
    private com.sixheroes.onedayheroquerydsl.mission.MissionQueryRepository missionQueryRepository;

    @BeforeAll
    public static void setUp(
            @Autowired MissionCategoryRepository missionCategoryRepository,
            @Autowired RegionRepository regionRepository
    ) {
        var missionCategories = Arrays.stream(MissionCategoryCode.values())
                .map(MissionCategory::from)
                .toList();

        missionCategoryRepository.saveAll(missionCategories);

        var regionA = Region.builder()
                .si("서울시")
                .gu("강남구")
                .dong("역삼동")
                .build();

        var regionB = Region.builder()
                .si("서울시")
                .gu("강남구")
                .dong("서초동")
                .build();

        regionRepository.saveAll(List.of(regionA, regionB));
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


        var missionA = createMission(1L, missionCategory, missionInfoA, 1L);
        var missionB = createMission(2L, missionCategory, missionInfoB, 1L);

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

        var mission = createMission(1L, missionCategory, missionInfo, 1L);

        var savedMission = missionRepository.save(mission);

        // when
        var missionQueryResponse = missionQueryRepository.fetchOne(savedMission.getId());

        // then
        assertThat(missionQueryResponse).isNotEmpty();
    }

    @Transactional
    @DisplayName("미션을 필터에 따라 다르게 적용 할 수 있다.")
    @Test
    void findAllByDynamicCondition() {
        // given
        var serverTime = LocalDateTime.of(
                LocalDate.of(2023, 10, 30),
                LocalTime.MIDNIGHT
        );

        var citizenId = 1L;
        var missionCategoryA = missionCategoryRepository.findById(1L).get();
        var missionInfoA = createMissionInfo(LocalDate.of(2023, 10, 31), serverTime);
        var missionA = createMission(citizenId, missionCategoryA, missionInfoA, 1L);

        var missionCategoryB = missionCategoryRepository.findById(2L).get();
        var missionInfoB = createMissionInfo(LocalDate.of(2023, 11, 4), serverTime);
        var missionB = createMission(citizenId, missionCategoryB, missionInfoB, 1L);

        var pageRequest = PageRequest.of(0, 3);

        missionRepository.saveAll(List.of(missionA, missionB));

        var missionCategoryIds = List.of(1L, 2L);
        var regionIds = List.of(1L, 2L);
        var localDates = List.of(LocalDate.of(2023, 10, 31), LocalDate.of(2023, 11, 4));

        // when
        var result = missionQueryRepository.findByDynamicCondition(pageRequest, missionCategoryIds, regionIds, localDates);

        // then
        assertThat(result).hasSize(2);
    }

    @Transactional
    @DisplayName("미션을 필터에 따라 다르게 적용 할 수 있다. 카테고리 아이디만")
    @Test
    void findAllByDynamicConditionWithCategoryIds() {
        // given
        var serverTime = LocalDateTime.of(
                LocalDate.of(2023, 10, 30),
                LocalTime.MIDNIGHT
        );

        var citizenId = 1L;
        var missionCategoryA = missionCategoryRepository.findById(1L).get();
        var missionInfoA = createMissionInfo(LocalDate.of(2023, 10, 31), serverTime);
        var missionA = createMission(citizenId, missionCategoryA, missionInfoA, 1L);

        var missionCategoryB = missionCategoryRepository.findById(2L).get();
        var missionInfoB = createMissionInfo(LocalDate.of(2023, 11, 4), serverTime);
        var missionB = createMission(citizenId, missionCategoryB, missionInfoB, 1L);

        var pageRequest = PageRequest.of(0, 3);

        missionRepository.saveAll(List.of(missionA, missionB));

        var missionCategoryIds = List.of(1L);

        // when
        var result = missionQueryRepository.findByDynamicCondition(pageRequest, missionCategoryIds, Collections.emptyList(), Collections.emptyList());

        // then
        assertThat(result).hasSize(1);
    }

    @Transactional
    @DisplayName("미션을 필터에 따라 다르게 적용 할 수 있다. 조건 X")
    @Test
    void findAllByDynamicConditionWithEmptyCondition() {
        // given
        var serverTime = LocalDateTime.of(
                LocalDate.of(2023, 10, 30),
                LocalTime.MIDNIGHT
        );

        var citizenId = 1L;
        var missionCategoryA = missionCategoryRepository.findById(1L).get();
        var missionInfoA = createMissionInfo(LocalDate.of(2023, 10, 31), serverTime);
        var missionA = createMission(citizenId, missionCategoryA, missionInfoA, 1L);

        var missionCategoryB = missionCategoryRepository.findById(2L).get();
        var missionInfoB = createMissionInfo(LocalDate.of(2023, 11, 4), serverTime);
        var missionB = createMission(citizenId, missionCategoryB, missionInfoB, 1L);

        var pageRequest = PageRequest.of(0, 3);

        missionRepository.saveAll(List.of(missionA, missionB));

        // when
        var result = missionQueryRepository.findByDynamicCondition(pageRequest, Collections.emptyList(), Collections.emptyList(), Collections.emptyList());

        // then
        assertThat(result).hasSize(2);
    }

    private MissionInfo createMissionInfo(
            LocalDate missionDate,
            LocalDateTime serverTime
    ) {
        return MissionInfo.builder()
                .content("내용")
                .missionDate(missionDate)
                .startTime(LocalTime.of(10, 0))
                .endTime(LocalTime.of(10, 30))
                .deadlineTime(LocalTime.of(10, 0))
                .price(10000)
                .serverTime(serverTime)
                .build();
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
            MissionInfo missionInfo,
            Long regionId
    ) {
        return Mission.builder()
                .missionCategory(missionCategory)
                .missionInfo(missionInfo)
                .regionId(regionId)
                .citizenId(citizenId)
                .location(new Point(123456.78, 123456.78))
                .missionStatus(MissionStatus.MATCHING)
                .bookmarkCount(0)
                .build();
    }
}