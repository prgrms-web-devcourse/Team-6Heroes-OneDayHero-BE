package com.sixheroes.onedayheroinfraquerydsl.missionproposal;

import com.sixheroes.onedayherodomain.mission.*;
import com.sixheroes.onedayherodomain.mission.repository.MissionCategoryRepository;
import com.sixheroes.onedayherodomain.mission.repository.MissionRepository;
import com.sixheroes.onedayherodomain.missionproposal.MissionProposal;
import com.sixheroes.onedayherodomain.missionproposal.repository.MissionProposalRepository;
import com.sixheroes.onedayherodomain.region.Region;
import com.sixheroes.onedayherodomain.region.repository.RegionRepository;
import com.sixheroes.onedayheroinfraquerydsl.IntegrationQueryDslTest;
import com.sixheroes.onedayheroquerydsl.missionproposal.MissionProposalQueryRepository;
import com.sixheroes.onedayheroquerydsl.missionproposal.dto.MissionProposalQueryDto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.groupingBy;
import static org.assertj.core.api.Assertions.assertThat;

class MissionProposalQueryRepositoryTest extends IntegrationQueryDslTest {

    @Autowired
    private MissionProposalQueryRepository missionProposalQueryRepository;

    @Autowired
    private MissionProposalRepository missionProposalRepository;

    @Autowired
    private MissionRepository  missionRepository;

    @Autowired
    private RegionRepository regionRepository;

    @Autowired
    private MissionCategoryRepository missionCategoryRepository;

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
        var missionProposals = missions.stream()
            .map(Mission::getId)
            .map(missionId -> createMissionProposal(heroId, missionId))
            .toList();

        missionProposalRepository.saveAll(missionProposals);

        var pageRequest = PageRequest.of(0, 5);

        // when
        var slice = missionProposalQueryRepository.findByHeroIdAndPageable(heroId, pageRequest);

        // then
        var content = slice.getContent();
        assertThat(content).hasSize(5);
        assertThat(content).extracting("missionStatus")
                        .containsSequence(MissionStatus.MATCHING, MissionStatus.MATCHING_COMPLETED, MissionStatus.MISSION_COMPLETED, MissionStatus.EXPIRED);
        assertThat(content.stream()
                .collect(groupingBy(MissionProposalQueryDto::missionStatus)).get(MissionStatus.MATCHING))
                .isSortedAccordingTo(comparing(MissionProposalQueryDto::missionCreatedAt).reversed());
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
        var mission1 = Mission.createMission(missionCategory, 1L, regionId,123.123, 123.123, createMissionInfo());

        var mission2 = Mission.createMission(missionCategory, 1L, regionId, 123.123, 123.123, createMissionInfo());

        var mission3 = Mission.createMission(missionCategory, 1L, regionId, 123.123, 123.123, createMissionInfo());
        mission3.changeMissionStatus(MissionStatus.MATCHING_COMPLETED);

        var mission4 = Mission.createMission(missionCategory, 1L, regionId, 123.123, 123.123, createMissionInfo());
        mission4.changeMissionStatus(MissionStatus.MISSION_COMPLETED);

        var mission5 = Mission.createMission(missionCategory, 1L, regionId, 123.123, 123.123, createMissionInfo());
        mission5.changeMissionStatus(MissionStatus.EXPIRED);

        return List.of(mission1, mission2, mission3, mission4, mission5);
    }

    private MissionInfo createMissionInfo() {
        return MissionInfo.builder()
            .missionDate(LocalDate.of(2023, 10, 31))
            .startTime(LocalTime.of(12, 0, 0))
            .endTime(LocalTime.of(16, 0, 0))
            .price(20000)
            .deadlineTime(LocalDateTime.of(2023, 10, 30, 20, 0, 0))
            .title("미션 제목")
            .content("미션 내용")
            .serverTime(LocalDateTime.of(2023, 10, 28, 12, 0, 0))
            .build();
    }

    private MissionProposal createMissionProposal(
        Long heroId,
        Long missionId
    ) {
        return MissionProposal.builder()
            .heroId(heroId)
            .missionId(missionId)
            .build();
    }
}
