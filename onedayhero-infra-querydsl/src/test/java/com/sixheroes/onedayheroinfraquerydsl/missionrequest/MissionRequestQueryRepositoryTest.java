package com.sixheroes.onedayheroinfraquerydsl.missionrequest;

import com.sixheroes.onedayherodomain.mission.*;
import com.sixheroes.onedayherodomain.mission.repository.MissionCategoryRepository;
import com.sixheroes.onedayherodomain.mission.repository.MissionRepository;
import com.sixheroes.onedayherodomain.missionrequest.MissionRequest;
import com.sixheroes.onedayherodomain.missionrequest.repository.MissionRequestRepository;
import com.sixheroes.onedayherodomain.region.Region;
import com.sixheroes.onedayherodomain.region.repository.RegionRepository;
import com.sixheroes.onedayheroinfraquerydsl.IntegrationQueryDslTest;
import com.sixheroes.onedayheroquerydsl.missionrequest.MissionRequestQueryRepository;
import com.sixheroes.onedayheroquerydsl.missionrequest.dto.MissionRequestQueryDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.geo.Point;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.groupingBy;
import static org.assertj.core.api.Assertions.assertThat;

class MissionRequestQueryRepositoryTest extends IntegrationQueryDslTest {

    @Autowired
    private MissionRequestQueryRepository missionRequestQueryRepository;

    @Autowired
    private MissionRequestRepository missionRequestRepository;

    @Autowired
    private MissionRepository  missionRepository;

    @Autowired
    private RegionRepository regionRepository;

    @Autowired
    private MissionCategoryRepository missionCategoryRepository;

    @DisplayName("히어로 아이디를 통해 제안받은 미션을 페이징 조회한다.")
    @Transactional
    @Test
    void findByHeroIdAndPageable() {
        // given
        var region = createRegion();
        var missionCategory = createMissionCategory();

        regionRepository.save(region);
        missionCategoryRepository.save(missionCategory);

        var missions = createMissions(region.getId(), missionCategory);

        missionRepository.saveAll(missions);

        var heroId = 1L;
        var missionRequests = missions.stream()
            .map(Mission::getId)
            .map(missionId -> createMissionRequest(heroId, missionId))
            .toList();

        missionRequestRepository.saveAll(missionRequests);

        var pageRequest = PageRequest.of(0, 5);

        // when
        var slice = missionRequestQueryRepository.findByHeroIdAndPageable(heroId, pageRequest);

        // then
        var content = slice.getContent();
        assertThat(content).hasSize(5);
        assertThat(content).extracting("missionStatus")
                        .containsSequence(MissionStatus.MATCHING, MissionStatus.MATCHING_COMPLETED, MissionStatus.MISSION_COMPLETED, MissionStatus.EXPIRED);
        assertThat(content.stream()
                .collect(groupingBy(MissionRequestQueryDto::missionStatus)).get(MissionStatus.MATCHING))
                .isSortedAccordingTo(comparing(MissionRequestQueryDto::missionCreatedAt).reversed());
        assertThat(slice.hasNext()).isFalse();
    }

    private Region createRegion() {
        return Region.builder()
            .si("서울시")
            .gu("프로구")
            .dong("래머동")
            .build();
    }

    private MissionCategory createMissionCategory() {
        return MissionCategory.from(MissionCategoryCode.MC_001);
    }

    private List<Mission> createMissions(
        Long regionId,
        MissionCategory missionCategory
    ) {
        var mission1 = Mission.builder()
            .citizenId(1L)
            .regionId(regionId)
            .missionCategory(missionCategory)
            .missionInfo(createMissionInfo())
            .location(new Point(123.123, 123.123))
            .build();

        var mission2 = Mission.builder()
            .citizenId(1L)
            .regionId(regionId)
            .missionCategory(missionCategory)
            .missionInfo(createMissionInfo())
            .location(new Point(123.123, 123.123))
            .build();

        var mission3 = Mission.builder()
            .citizenId(1L)
            .regionId(regionId)
            .missionCategory(missionCategory)
            .missionInfo(createMissionInfo())
            .location(new Point(123.123, 123.123))
            .build();
        mission3.changeMissionStatus(MissionStatus.MATCHING_COMPLETED);

        var mission4 = Mission.builder()
            .citizenId(1L)
            .regionId(regionId)
            .missionCategory(missionCategory)
            .missionInfo(createMissionInfo())
            .location(new Point(123.123, 123.123))
            .build();
        mission4.changeMissionStatus(MissionStatus.MISSION_COMPLETED);

        var mission5 = Mission.builder()
            .citizenId(1L)
            .regionId(regionId)
            .missionCategory(missionCategory)
            .missionInfo(createMissionInfo())
            .location(new Point(123.123, 123.123))
            .build();
        mission5.changeMissionStatus(MissionStatus.EXPIRED);

        return List.of(mission1, mission2, mission3, mission4, mission5);
    }

    private MissionInfo createMissionInfo() {
        return MissionInfo.builder()
            .missionDate(LocalDate.of(2023, 10, 31))
            .startTime(LocalTime.of(12, 0, 0))
            .endTime(LocalTime.of(16, 0, 0))
            .price(20000)
            .deadlineTime(LocalTime.of(16, 0, 0))
            .content("미션 내용")
            .build();
    }

    private MissionRequest createMissionRequest(
        Long heroId,
        Long missionId
    ) {
        return MissionRequest.builder()
            .heroId(heroId)
            .missionId(missionId)
            .build();
    }
}