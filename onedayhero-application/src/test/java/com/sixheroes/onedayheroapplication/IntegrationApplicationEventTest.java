package com.sixheroes.onedayheroapplication;

import com.sixheroes.onedayheroapplication.alarm.AlarmService;
import com.sixheroes.onedayheroapplication.mission.MissionService;
import com.sixheroes.onedayheroapplication.mission.event.MissionEventService;
import com.sixheroes.onedayheroapplication.missionmatch.MissionMatchService;
import com.sixheroes.onedayheroapplication.missionmatch.event.MissionMatchEventService;
import com.sixheroes.onedayheroapplication.missionproposal.MissionProposalService;
import com.sixheroes.onedayheroapplication.missionproposal.event.MissionProposalEventService;
import com.sixheroes.onedayheroapplication.review.ReviewService;
import com.sixheroes.onedayheroapplication.review.event.ReviewEventService;
import com.sixheroes.onedayherodomain.mission.MissionCategory;
import com.sixheroes.onedayherodomain.mission.MissionCategoryCode;
import com.sixheroes.onedayherodomain.mission.repository.MissionCategoryRepository;
import com.sixheroes.onedayherodomain.mission.repository.MissionRepository;
import com.sixheroes.onedayherodomain.missionmatch.repository.MissionMatchRepository;
import com.sixheroes.onedayherodomain.missionproposal.repository.MissionProposalRepository;
import com.sixheroes.onedayherodomain.region.Region;
import com.sixheroes.onedayherodomain.region.repository.RegionRepository;
import com.sixheroes.onedayherodomain.review.repository.ReviewRepository;
import com.sixheroes.onedayherodomain.user.repository.UserRepository;
import com.sixheroes.onedayheromongo.alarm.mongo.AlarmRepository;
import com.sixheroes.onedayheromongo.alarm.mongo.AlarmTemplateRepository;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.RecordApplicationEvents;

import java.util.Arrays;
import java.util.List;

@ActiveProfiles("test")
@SpringBootTest
@RecordApplicationEvents
public abstract class IntegrationApplicationEventTest {

    @Autowired
    protected ApplicationEvents applicationEvents;

    @MockBean
    protected AlarmTemplateRepository alarmTemplateRepository;

    @MockBean
    protected AlarmRepository alarmRepository;

    @Autowired
    protected AlarmService alarmService;

    @Autowired
    protected MissionProposalService missionProposalService;

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected MissionProposalRepository missionProposalRepository;

    @Autowired
    protected MissionCategoryRepository missionCategoryRepository;

    @Autowired
    protected MissionRepository missionRepository;

    @Autowired
    protected MissionProposalEventService missionProposalEventService;

    @Autowired
    protected ReviewService reviewService;

    @Autowired
    protected ReviewRepository reviewRepository;

    @Autowired
    protected ReviewEventService reviewEventService;

    @Autowired
    protected MissionService missionService;

    @Autowired
    protected MissionEventService missionEventService;

    @Autowired
    protected MissionMatchRepository missionMatchRepository;

    @Autowired
    protected MissionMatchService missionMatchService;

    @Autowired
    protected MissionMatchEventService missionMatchEventService;

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
