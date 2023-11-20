package com.sixheroes.onedayheroapplication;

import com.sixheroes.onedayheroapplication.mission.repository.MissionBookmarkQueryRepository;
import com.sixheroes.onedayheroapplication.mission.repository.MissionQueryRepository;
import com.sixheroes.onedayheroapplication.missionproposal.repository.MissionProposalQueryRepository;
import com.sixheroes.onedayherodomain.mission.MissionCategory;
import com.sixheroes.onedayherodomain.mission.MissionCategoryCode;
import com.sixheroes.onedayherodomain.mission.repository.MissionBookmarkRepository;
import com.sixheroes.onedayherodomain.mission.repository.MissionCategoryRepository;
import com.sixheroes.onedayherodomain.mission.repository.MissionRepository;
import com.sixheroes.onedayherodomain.missionproposal.repository.MissionProposalRepository;
import com.sixheroes.onedayherodomain.region.Region;
import com.sixheroes.onedayherodomain.region.repository.RegionRepository;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;

@ActiveProfiles("test")
@SpringBootTest
public abstract class IntegrationQueryDslTest {

    @Autowired
    protected RegionRepository regionRepository;

    @Autowired
    protected MissionCategoryRepository missionCategoryRepository;

    @Autowired
    protected MissionRepository missionRepository;

    @Autowired
    protected MissionQueryRepository missionQueryRepository;

    @Autowired
    protected MissionProposalQueryRepository missionProposalQueryRepository;

    @Autowired
    protected MissionProposalRepository missionProposalRepository;

    @Autowired
    protected MissionBookmarkQueryRepository missionBookmarkQueryRepository;

    @Autowired
    protected MissionBookmarkRepository missionBookmarkRepository;

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
}
