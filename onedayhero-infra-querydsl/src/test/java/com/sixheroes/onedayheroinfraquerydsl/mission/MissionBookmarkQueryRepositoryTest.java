package com.sixheroes.onedayheroinfraquerydsl.mission;

import com.sixheroes.onedayherodomain.mission.*;
import com.sixheroes.onedayherodomain.mission.repository.MissionBookmarkRepository;
import com.sixheroes.onedayherodomain.mission.repository.MissionCategoryRepository;
import com.sixheroes.onedayherodomain.mission.repository.MissionRepository;
import com.sixheroes.onedayherodomain.region.Region;
import com.sixheroes.onedayherodomain.region.repository.RegionRepository;
import com.sixheroes.onedayheroinfraquerydsl.IntegrationQueryDslTest;
import com.sixheroes.onedayheroquerydsl.mission.MissionBookmarkQueryRepository;
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
import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;


@Transactional
class MissionBookmarkQueryRepositoryTest extends IntegrationQueryDslTest {

    @Autowired
    private MissionBookmarkQueryRepository missionBookmarkQueryRepository;

    @Autowired
    private MissionCategoryRepository missionCategoryRepository;

    @Autowired
    private MissionRepository missionRepository;

    @Autowired
    private MissionBookmarkRepository missionBookmarkRepository;

    @BeforeAll
    public static void setUp(
            @Autowired MissionCategoryRepository missionCategoryRepository,
            @Autowired RegionRepository regionRepository
    ) {
        MissionCategory missionCategory = MissionCategory.from(MissionCategoryCode.MC_001);
        missionCategoryRepository.save(missionCategory);

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

    @DisplayName("내 미션 찜목록을 조회할 수 있다.")
    @Test
    void viewMeBookmarkMissions() {
        // given
        var bookmarkUserId = 1L;
        var citizenId = 2L;
        createFiveBookmarks(
                citizenId,
                bookmarkUserId
        );

        // when
        var pageRequest = PageRequest.of(0, 5);
        var responses = missionBookmarkQueryRepository.viewMyBookmarks(
                pageRequest,
                bookmarkUserId
        );

//        // then
//        assertThat(responses).hasSize(2);
    }

    private Mission createMissionWithMissionStatus(
            Long citizenId,
            MissionStatus missionStatus
    ) {
        var mission = Mission.builder()
                .missionStatus(missionStatus)
                .missionInfo(createMissionInfo())
                .missionCategory(missionCategoryRepository.findById(1L).get())
                .citizenId(citizenId)
                .regionId(1L)
                .location(new Point(1234, 5678))
                .bookmarkCount(0)
                .build();

        return missionRepository.save(mission);
    }

    private MissionInfo createMissionInfo() {
        return MissionInfo.builder()
                .title("서빙 구함")
                .missionDate(LocalDate.of(2023, 10, 10))
                .startTime(LocalTime.of(10, 0))
                .endTime(LocalTime.of(10, 30))
                .deadlineTime(LocalTime.of(10, 0))
                .price(10000)
                .content("서빙 도와주기")
                .serverTime(LocalDateTime.of(
                        LocalDate.of(2023, 10, 9),
                        LocalTime.MIDNIGHT
                ))
                .build();
    }

    private void createFiveBookmarks(
            long citizenId,
            long bookmarkUserId
    ) {
        IntStream.range(0, 10)
                .forEach(i -> {
                    var mission = createMissionWithMissionStatus(
                            citizenId,
                            MissionStatus.MATCHING
                    );

                    if (i <= 4) {
                        var missionBookmark = MissionBookmark.builder()
                                .mission(mission)
                                .userId(bookmarkUserId)
                                .build();
                        missionBookmarkRepository.save(missionBookmark);
                        mission.addBookmarkCount();
                    }
                });
    }
}
