package com.sixheroes.onedayheroapplication.review;

import com.sixheroes.onedayheroapplication.IntegrationApplicationEventTest;
import com.sixheroes.onedayheroapplication.review.event.dto.ReviewCreateEvent;
import com.sixheroes.onedayheroapplication.review.reqeust.ReviewCreateServiceRequest;
import com.sixheroes.onedayherodomain.mission.Mission;
import com.sixheroes.onedayherodomain.mission.MissionInfo;
import com.sixheroes.onedayherodomain.mission.MissionStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

class ReviewServiceEventTest extends IntegrationApplicationEventTest {

    @DisplayName("리뷰를 작성하고 알림 이벤트를 발행한다.")
    @Transactional
    @Test
    void createReview() {
        // given
        var citizenId = 1L;
        var heroId = 2L;
        var mission = createMissionWithMissionStatus(
            citizenId,
            MissionStatus.MISSION_COMPLETED
        );

        // when
        var request = createReviewCreateServiceRequest(
            mission,
            citizenId,
            heroId
        );
        var response = reviewService.create(
            request,
            Collections.emptyList()
        );

        // then
        var reviewCreateEventOptional= applicationEvents.stream(ReviewCreateEvent.class).findFirst();
        assertThat(reviewCreateEventOptional).isNotEmpty();
        var missionProposalCreateEvent = reviewCreateEventOptional.get();
        assertThat(missionProposalCreateEvent.reviewId()).isEqualTo(response.id());
    }

    private ReviewCreateServiceRequest createReviewCreateServiceRequest(
        Mission mission,
        Long citizenId,
        Long heroId
    ) {
        return ReviewCreateServiceRequest.builder()
            .senderId(citizenId)
            .receiverId(heroId)
            .missionId(mission.getId())
            .categoryId(mission.getMissionCategory().getId())
            .missionTitle(mission.getMissionInfo().getTitle())
            .starScore(5)
            .content("히어로에 대한 평가 리뷰 내용")
            .build();
    }

    private Mission createMissionWithMissionStatus(
        Long citizenId,
        MissionStatus missionStatus
    ) {
        var mission = Mission.builder()
            .citizenId(citizenId)
            .missionStatus(missionStatus)
            .missionInfo(createMissionInfo())
            .missionCategory(missionCategoryRepository.findAll().get(0))
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
}