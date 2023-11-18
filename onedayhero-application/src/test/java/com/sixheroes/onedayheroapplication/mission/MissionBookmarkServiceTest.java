package com.sixheroes.onedayheroapplication.mission;

import com.sixheroes.onedayheroapplication.IntegrationApplicationTest;
import com.sixheroes.onedayheroapplication.mission.request.MissionBookmarkCancelServiceRequest;
import com.sixheroes.onedayheroapplication.mission.request.MissionBookmarkCreateServiceRequest;
import com.sixheroes.onedayherocommon.error.ErrorCode;
import com.sixheroes.onedayherodomain.mission.Mission;
import com.sixheroes.onedayherodomain.mission.MissionInfo;
import com.sixheroes.onedayherodomain.mission.MissionStatus;
import com.sixheroes.onedayherodomain.mission.repository.MissionCategoryRepository;
import com.sixheroes.onedayherodomain.mission.repository.MissionRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
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

    @DisplayName("유저는 미션 찜목록을 조회할 수 있다.")
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
        var pageRequest = PageRequest.of(1, 3);
        var response = missionBookmarkService.viewMyBookmarks(
                pageRequest,
                bookmarkUserId
        );

        // then
        assertSoftly(soft -> {
            soft.assertThat(response.missionBookmarkMeResponses().getContent().size()).isEqualTo(2);
            soft.assertThat(response.missionBookmarkMeResponses().hasNext()).isFalse();
        });
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
                bookmarkUserId,
                createMissionBookmarkCreateServiceRequest(mission.getId())
        );

        // then
        assertSoftly(soft -> {
            soft.assertThat(response).isNotNull();
            soft.assertThat(mission.getBookmarkCount()).isEqualTo(1);
        });
    }

    @DisplayName("시민은 이미 찜한 미션에 또 찜할 수 없다.")
    @Test
    void createMissionBookmarkFail() {
        // given
        var bookmarkUserId = 1L;
        var citizenId = 2L;
        var mission = createMissionWithMissionStatus(
                citizenId,
                MissionStatus.MATCHING
        );
        missionBookmarkService.createMissionBookmark(
                bookmarkUserId,
                createMissionBookmarkCreateServiceRequest(mission.getId())
        );

        // when
        assertThatThrownBy(() -> missionBookmarkService.createMissionBookmark(
                bookmarkUserId,
                createMissionBookmarkCreateServiceRequest(mission.getId())
        )).isInstanceOf(IllegalStateException.class)
                .hasMessage(ErrorCode.T_001.name());
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
                bookmarkUserId,
                createMissionBookmarkCreateServiceRequest(mission.getId())
        );

        // when
        missionBookmarkService.cancelMissionBookmark(
                bookmarkUserId,
                createMissionBookmarkCancelServiceRequest(mission.getId())
        );

        // then
        assertSoftly(soft -> {
            soft.assertThat(mission.getBookmarkCount()).isEqualTo(0);
        });
    }

    private MissionBookmarkCreateServiceRequest createMissionBookmarkCreateServiceRequest(
            Long missionId
    ) {
        return MissionBookmarkCreateServiceRequest.builder()
                .missionId(missionId)
                .build();
    }

    private MissionBookmarkCancelServiceRequest createMissionBookmarkCancelServiceRequest(
            Long missionId
    ) {
        return MissionBookmarkCancelServiceRequest.builder()
                .missionId(missionId)
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
                .location(Mission.createPoint(1234.56, 1234.78))
                .bookmarkCount(0)
                .build();

        return missionRepository.save(mission);
    }

    private MissionInfo createMissionInfo() {
        return MissionInfo.builder()
                .missionDate(LocalDate.of(2023, 10, 10))
                .startTime(LocalTime.of(10, 0))
                .endTime(LocalTime.of(10, 30))
                .deadlineTime(LocalDateTime.of(
                        LocalDate.of(2023, 10, 10),
                        LocalTime.of(10, 0)
                ))
                .price(10000)
                .title("서빙")
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
                        missionBookmarkService.createMissionBookmark(
                                bookmarkUserId,
                                createMissionBookmarkCreateServiceRequest(mission.getId())
                        );
                    }
                });
    }
}
