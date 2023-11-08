package com.sixheroes.onedayheroapplication.mission;

import com.sixheroes.onedayheroapplication.IntegrationApplicationTest;
import com.sixheroes.onedayheroapplication.mission.request.MissionBookmarkCancelServiceRequest;
import com.sixheroes.onedayheroapplication.mission.request.MissionBookmarkCreateServiceRequest;
import com.sixheroes.onedayherodomain.mission.*;
import com.sixheroes.onedayherodomain.mission.repository.MissionCategoryRepository;
import com.sixheroes.onedayherodomain.mission.repository.MissionRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Point;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.assertj.core.api.SoftAssertions.assertSoftly;


@Transactional
@ActiveProfiles("test")
class MissionBookmarkServiceTest extends IntegrationApplicationTest {

    @Autowired
    private MissionBookmarkService missionBookmarkService;

    @Autowired
    private MissionCategoryRepository missionCategoryRepository;

    @Autowired
    private MissionRepository missionRepository;

    @BeforeAll
    public static void setUp(@Autowired MissionCategoryRepository missionCategoryRepository) {
        MissionCategory missionCategory = MissionCategory.from(MissionCategoryCode.MC_001);
        missionCategoryRepository.save(missionCategory);
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
                createMissionBookmarkCreateServiceRequest(
                        mission.getId(),
                        bookmarkUserId
                )
        );

        // then
        assertSoftly(soft -> {
            soft.assertThat(response).isNotNull();
            soft.assertThat(mission.getBookmarkCount()).isEqualTo(1);
        });
    }

    @DisplayName("시민은 찜했던 미션에 대해 찜 취소를 할 수 있다.")
    @Test
    void cancelMissionBookmark() {
        // given
        var bookmarkUserId = 1L;
        var citizenId = 2L;
        var mission = createMissionWithMissionStatus(
                citizenId,
                MissionStatus.MATCHING
        );
        missionBookmarkService.createMissionBookmark(
                createMissionBookmarkCreateServiceRequest(
                        mission.getId(),
                        bookmarkUserId
                )
        );

        // when
        var response = missionBookmarkService.cancelMissionBookmark(
                createMissionBookmarkCancelServiceRequest(
                        mission.getId(),
                        bookmarkUserId
                )
        );

        // then
        assertSoftly(soft -> {
            soft.assertThat(response).isNotNull();
            soft.assertThat(mission.getBookmarkCount()).isEqualTo(0);
        });
    }

    private MissionBookmarkCreateServiceRequest createMissionBookmarkCreateServiceRequest(
            Long missionId,
            Long bookmarkUserId
    ) {
        return MissionBookmarkCreateServiceRequest.builder()
                .missionId(missionId)
                .userId(bookmarkUserId)
                .build();
    }

    private MissionBookmarkCancelServiceRequest createMissionBookmarkCancelServiceRequest(
            Long missionId,
            Long userId
    ) {
        return MissionBookmarkCancelServiceRequest.builder()
                .missionId(missionId)
                .userId(userId)
                .build();
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
                .missionDate(LocalDate.of(2023, 10, 10))
                .startTime(LocalTime.of(10, 0))
                .endTime(LocalTime.of(10, 30))
                .deadlineTime(LocalTime.of(10, 0))
                .price(10000)
                .title("서빙")
                .content("서빙 도와주기")
                .serverTime(LocalDateTime.of(
                        LocalDate.of(2023, 10, 9),
                        LocalTime.MIDNIGHT
                ))
                .build();
    }
}
