package com.sixheroes.onedayheroapplication.mission;

import com.sixheroes.onedayheroapplication.IntegrationQueryDslTest;
import com.sixheroes.onedayheroapplication.mission.repository.request.MissionFindFilterQueryRequest;
import com.sixheroes.onedayherodomain.mission.Mission;
import com.sixheroes.onedayherodomain.mission.MissionCategory;
import com.sixheroes.onedayherodomain.mission.MissionInfo;
import com.sixheroes.onedayherodomain.mission.MissionStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
class MissionQueryRepositoryTest extends IntegrationQueryDslTest {

    @DisplayName("미션을 한 번의 쿼리로 단 건 조회 할 수 있다.")
    @Test
    void findOneWithFetchJoin() {
        // given
        var missionCategory = missionCategoryRepository.findById(1L).get();

        var serverTime = LocalDateTime.of(LocalDate.of(2023, 10, 9), LocalTime.MIDNIGHT);

        var missionDate = LocalDate.of(2023, 10, 10);
        var startTime = LocalTime.of(10, 0);
        var endTime = LocalTime.of(10, 30);
        var deadlineTime = LocalDateTime.of(missionDate, startTime);

        var missionInfo = createMissionInfo(missionDate, startTime, endTime, deadlineTime, serverTime);

        var mission = createMission(1L, missionCategory, missionInfo, 1L);

        var savedMission = missionRepository.save(mission);

        // when
        var missionQueryResponse = missionQueryRepository.fetchOne(savedMission.getId());

        // then
        assertThat(missionQueryResponse).isNotEmpty();
    }

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
        var missionDates = List.of(LocalDate.of(2023, 10, 31), LocalDate.of(2023, 11, 4));

        var request = MissionFindFilterQueryRequest.builder()
                .userId(citizenId)
                .missionCategoryIds(missionCategoryIds)
                .regionIds(regionIds)
                .missionDates(missionDates)
                .build();

        // when
        var result = missionQueryRepository.findByDynamicCondition(pageRequest, request);

        // then
        assertThat(result).hasSize(2);
    }

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

        var request = MissionFindFilterQueryRequest.builder()
                .userId(citizenId)
                .missionCategoryIds(missionCategoryIds)
                .regionIds(Collections.emptyList())
                .missionDates(Collections.emptyList())
                .build();

        // when
        var result = missionQueryRepository.findByDynamicCondition(pageRequest, request);

        // then
        assertThat(result).hasSize(1);
    }

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

        var request = MissionFindFilterQueryRequest.builder()
                .userId(citizenId)
                .missionCategoryIds(Collections.emptyList())
                .regionIds(Collections.emptyList())
                .missionDates(Collections.emptyList())
                .build();

        // when
        var result = missionQueryRepository.findByDynamicCondition(pageRequest, request);

        // then
        assertThat(result).hasSize(2);
    }

    @DisplayName("유저가 현재 진행중인 미션 목록을 조회 할 수 있다.")
    @Test
    void findProgressMissionByUserId() {
        // given
        var missionCategory = missionCategoryRepository.findById(1L).get();
        var region = regionRepository.findById(1L).get();

        var serverTime = LocalDateTime.of(LocalDate.of(2023, 10, 9), LocalTime.MIDNIGHT);

        var missionDate = LocalDate.of(2023, 10, 10);
        var startTime = LocalTime.of(10, 0);
        var endTime = LocalTime.of(10, 30);
        var deadlineTime = LocalDateTime.of(missionDate, startTime);

        var missionInfo = createMissionInfo(missionDate, startTime, endTime, deadlineTime, serverTime);
        var citizenId = 1L;

        var mission = createMission(citizenId, missionCategory, missionInfo, region.getId());
        var completedMission = createMission(citizenId, missionCategory, missionInfo, region.getId(), MissionStatus.MISSION_COMPLETED);
        var matchedMission = createMission(citizenId, missionCategory, missionInfo, region.getId(), MissionStatus.MATCHING_COMPLETED);
        var expiredMission = createMission(citizenId, missionCategory, missionInfo, region.getId(), MissionStatus.EXPIRED);


        var pageRequest = PageRequest.of(0, 3);

        var savedMission = missionRepository.saveAll(
                List.of(mission, completedMission, matchedMission, expiredMission)
        );

        // when
        var missionQueryResponse = missionQueryRepository.findProgressMissionByUserId(pageRequest, citizenId);

        // then
        assertThat(missionQueryResponse).hasSize(2);
        assertThat(missionQueryResponse.get(0))
                .extracting(
                        "title",
                        "categoryId",
                        "categoryCode",
                        "categoryName",
                        "si",
                        "gu",
                        "dong",
                        "missionDate",
                        "bookmarkCount",
                        "missionStatus")
                .containsExactly(
                        mission.getMissionInfo().getTitle(),
                        mission.getMissionCategory().getId(),
                        mission.getMissionCategory().getMissionCategoryCode(),
                        mission.getMissionCategory().getMissionCategoryCode().getDescription(),
                        region.getSi(),
                        region.getGu(),
                        region.getDong(),
                        mission.getMissionInfo().getMissionDate(),
                        mission.getBookmarkCount(),
                        mission.getMissionStatus()
                );
    }

    @DisplayName("유저가 현재 완료 된 미션 목록을 조회 할 수 있다.")
    @Test
    void findCompletedMissionByUserId() {
        // given
        var missionCategory = missionCategoryRepository.findById(1L).get();
        var region = regionRepository.findById(1L).get();

        var serverTime = LocalDateTime.of(LocalDate.of(2023, 10, 9), LocalTime.MIDNIGHT);

        var missionDate = LocalDate.of(2023, 10, 10);
        var startTime = LocalTime.of(10, 0);
        var endTime = LocalTime.of(10, 30);
        var deadlineTime = LocalDateTime.of(missionDate, startTime);

        var missionInfo = createMissionInfo(missionDate, startTime, endTime, deadlineTime, serverTime);
        var citizenId = 1L;

        var mission = createMission(citizenId, missionCategory, missionInfo, region.getId());
        var completedMission = createMission(citizenId, missionCategory, missionInfo, region.getId(), MissionStatus.MISSION_COMPLETED);
        var matchedMission = createMission(citizenId, missionCategory, missionInfo, region.getId(), MissionStatus.MATCHING_COMPLETED);
        var expiredMission = createMission(citizenId, missionCategory, missionInfo, region.getId(), MissionStatus.EXPIRED);

        var pageRequest = PageRequest.of(0, 3);

        missionRepository.saveAll(List.of(mission, completedMission, matchedMission, expiredMission));

        // when
        var missionQueryResponse = missionQueryRepository.findCompletedMissionByUserId(pageRequest, citizenId);

        // then
        assertThat(missionQueryResponse).hasSize(1);
        assertThat(missionQueryResponse.get(0))
                .extracting(
                        "title",
                        "categoryId",
                        "categoryCode",
                        "categoryName",
                        "si",
                        "gu",
                        "dong",
                        "missionDate",
                        "bookmarkCount",
                        "missionStatus")
                .containsExactly(
                        mission.getMissionInfo().getTitle(),
                        mission.getMissionCategory().getId(),
                        mission.getMissionCategory().getMissionCategoryCode(),
                        mission.getMissionCategory().getMissionCategoryCode().getDescription(),
                        region.getSi(),
                        region.getGu(),
                        region.getDong(),
                        mission.getMissionInfo().getMissionDate(),
                        mission.getBookmarkCount(),
                        MissionStatus.MISSION_COMPLETED
                );
    }

    private MissionInfo createMissionInfo(
            LocalDate missionDate,
            LocalDateTime serverTime
    ) {
        return MissionInfo.builder()
                .title("제목")
                .content("내용")
                .missionDate(missionDate)
                .startTime(LocalTime.of(10, 0))
                .endTime(LocalTime.of(10, 30))
                .deadlineTime(LocalDateTime.of(
                        missionDate,
                        LocalTime.of(10, 0)
                ))
                .price(10000)
                .serverTime(serverTime)
                .build();
    }

    private MissionInfo createMissionInfo(
            LocalDate missionDate,
            LocalTime startTime,
            LocalTime endTime,
            LocalDateTime deadlineTime,
            LocalDateTime serverTime
    ) {
        return MissionInfo.builder()
                .title("제목")
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
                .location(Mission.createPoint(123456.78, 123456.78))
                .missionStatus(MissionStatus.MATCHING)
                .bookmarkCount(0)
                .build();
    }

    private Mission createMission(
            Long citizenId,
            MissionCategory missionCategory,
            MissionInfo missionInfo,
            Long regionId,
            MissionStatus missionStatus
    ) {
        return Mission.builder()
                .missionCategory(missionCategory)
                .missionInfo(missionInfo)
                .regionId(regionId)
                .citizenId(citizenId)
                .location(Mission.createPoint(123456.78, 123456.78))
                .missionStatus(missionStatus)
                .bookmarkCount(0)
                .build();
    }
}
