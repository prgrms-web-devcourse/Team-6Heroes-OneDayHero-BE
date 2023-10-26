package com.sixheroes.onedayheroapplication.mission;

import com.sixheroes.onedayheroapplication.IntegrationApplicationTest;
import com.sixheroes.onedayheroapplication.mission.request.MissionBookmarkCreateRequest;
import com.sixheroes.onedayherodomain.mission.*;
import com.sixheroes.onedayherodomain.mission.repository.MissionCategoryRepository;
import com.sixheroes.onedayherodomain.mission.repository.MissionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Point;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.SoftAssertions.*;


@Transactional
@ActiveProfiles("test")
class MissionBookmarkServiceTest extends IntegrationApplicationTest {

    @Autowired
    private MissionBookmarkService missionBookmarkService;

    @Autowired
    private MissionCategoryRepository missionCategoryRepository;

    @Autowired
    private MissionRepository missionRepository;

    @BeforeEach
    void setUp() {
        missionRepository.deleteAll();
        missionCategoryRepository.deleteAll();
    }

    @DisplayName("시민은 매칭중인 미션을 찜 할 수 있다.")
    @Test
    void createMissionBookmark() {
        // given
        var bookmarkUserId = 1L;
        var citizenId = 2L;
        var mission = createMissionWithMissionStatus(
                citizenId,
                MissionStatus.MATCHING
        );

        // when
        var response = missionBookmarkService.createMissionBookmark(
                createMissionBookmarkCreateRequest(
                        mission,
                        bookmarkUserId
                )
        );

        // then
        assertSoftly(soft -> {
            soft.assertThat(response).isNotNull();
            soft.assertThat(mission.getBookmarkCount()).isEqualTo(1);
        });
    }

    private MissionBookmarkCreateRequest createMissionBookmarkCreateRequest(
            Mission mission,
            long bookmarkUserId) {
        return MissionBookmarkCreateRequest.builder()
                .missionId(mission.getId())
                .userId(bookmarkUserId)
                .build();
    }

    private Mission createMissionWithMissionStatus(
            Long citizenId,
            MissionStatus missionStatus
    ) {
        var mission = Mission.builder()
                .missionStatus(missionStatus)
                .missionInfo(createMissionInfo())
                .missionCategory(createMissionCategory())
                .citizenId(citizenId)
                .regionId(1L)
                .location(new Point(1234, 5678))
                .build();

        return missionRepository.save(mission);
    }

    private MissionInfo createMissionInfo() {
        return MissionInfo.builder()
                .missionDate(LocalDate.of(2023, 10, 10))
                .startTime(LocalTime.of(10, 0))
                .endTime(LocalTime.of(10, 30))
                .deadlineTime(LocalTime.of(10, 0))
                .price(10000)
                .content("서빙 도와주기")
                .build();
    }

    private MissionCategory createMissionCategory() {
        var missionCategory = MissionCategory.builder()
                .missionCategoryCode(MissionCategoryCode.MC_001)
                .name("서빙")
                .build();

        return missionCategoryRepository.save(missionCategory);
    }
}
