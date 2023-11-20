package com.sixheroes.onedayheroapplication;

import com.sixheroes.onedayheroapplication.global.s3.S3ImageUploadService;
import com.sixheroes.onedayheroapplication.mission.MissionBookmarkService;
import com.sixheroes.onedayheroapplication.mission.MissionService;
import com.sixheroes.onedayheroapplication.missionmatch.MissionMatchReader;
import com.sixheroes.onedayheroapplication.missionmatch.MissionMatchService;
import com.sixheroes.onedayheroapplication.missionproposal.MissionProposalService;
import com.sixheroes.onedayheroapplication.review.ReviewService;
import com.sixheroes.onedayheroapplication.user.ProfileService;
import com.sixheroes.onedayheroapplication.user.UserService;
import com.sixheroes.onedayherodomain.mission.MissionCategory;
import com.sixheroes.onedayherodomain.mission.MissionCategoryCode;
import com.sixheroes.onedayherodomain.mission.repository.MissionBookmarkRepository;
import com.sixheroes.onedayherodomain.mission.repository.MissionCategoryRepository;
import com.sixheroes.onedayherodomain.mission.repository.MissionRepository;
import com.sixheroes.onedayherodomain.missionproposal.repository.MissionProposalRepository;
import com.sixheroes.onedayherodomain.region.Region;
import com.sixheroes.onedayherodomain.region.repository.RegionRepository;
import com.sixheroes.onedayherodomain.review.repository.ReviewRepository;
import com.sixheroes.onedayherodomain.user.repository.UserImageRepository;
import com.sixheroes.onedayherodomain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;

@ActiveProfiles("test")
@SpringBootTest
public abstract class IntegrationApplicationTest {

    @Autowired
    protected MissionBookmarkRepository missionBookmarkRepository;

    @Autowired
    protected MissionCategoryRepository missionCategoryRepository;

    @Autowired
    protected MissionRepository missionRepository;

    @Autowired
    protected RegionRepository regionRepository;

    @Autowired
    protected MissionService missionService;

    @Autowired
    protected MissionBookmarkService missionBookmarkService;

    @Autowired
    protected MissionMatchService missionMatchService;

    @Autowired
    protected MissionMatchReader missionMatchReader;

    @Autowired
    protected MissionProposalService missionProposalService;

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected MissionProposalRepository missionProposalRepository;

    @Autowired
    protected ReviewService reviewService;

    @Autowired
    protected ReviewRepository reviewRepository;

    @Autowired
    protected ProfileService profileService;

    @Autowired
    protected UserImageRepository userImageRepository;

    @Autowired
    protected UserService userService;

    @MockBean
    protected S3ImageUploadService s3ImageUploadService;

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
