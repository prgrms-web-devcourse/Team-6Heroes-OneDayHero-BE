package com.sixheroes.onedayheroapplication.review;

import com.sixheroes.onedayheroapplication.IntegrationApplicationTest;
import com.sixheroes.onedayheroapplication.review.reqeust.ReviewCreateServiceRequest;
import com.sixheroes.onedayheroapplication.review.reqeust.ReviewUpdateServiceRequest;
import com.sixheroes.onedayherodomain.mission.Mission;
import com.sixheroes.onedayherodomain.mission.MissionInfo;
import com.sixheroes.onedayherodomain.mission.MissionStatus;
import com.sixheroes.onedayherodomain.mission.repository.MissionCategoryRepository;
import com.sixheroes.onedayherodomain.mission.repository.MissionRepository;
import com.sixheroes.onedayherodomain.review.repository.ReviewRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

class ReviewServiceTest extends IntegrationApplicationTest {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private MissionRepository missionRepository;

    @Autowired
    private MissionCategoryRepository missionCategoryRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @DisplayName("유저는 완료된 미션에 대해 상대방에 대한 리뷰를 작성할 수 있다.")
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
                Optional.empty()
        );

        // then
        assertSoftly(soft -> {
            soft.assertThat(response).isNotNull();
            soft.assertThat(response.senderId()).isEqualTo(request.senderId());
            soft.assertThat(response.receiverId()).isEqualTo(request.receiverId());
            soft.assertThat(response.categoryId()).isEqualTo(request.categoryId());
            soft.assertThat(response.missionTitle()).isEqualTo(request.missionTitle());
            soft.assertThat(response.starScore()).isEqualTo(request.starScore());
            soft.assertThat(response.content()).isEqualTo(request.content());
            soft.assertThat(response.reviewImageResponses()).hasSize(0);
        });
    }

    @Transactional
    @DisplayName("유저는 리뷰의 내용과 별점을 수정할 수 있다.")
    @Test
    void updateReview() {
        // given
        var citizenId = 1L;
        var heroId = 2L;
        var mission = createMissionWithMissionStatus(
                citizenId,
                MissionStatus.MISSION_COMPLETED
        );
        var createServiceRequest = createReviewCreateServiceRequest(
                mission,
                citizenId,
                heroId
        );
        var reviewCreateResponse = reviewService.create(
                createServiceRequest,
                Optional.empty()
        );

        // when
        var request = createReviewUpdateServiceRequest();
        var response = reviewService.update(
                reviewCreateResponse.id(),
                request
        );

        // then
        assertSoftly(soft -> {
            soft.assertThat(response).isNotNull();
            soft.assertThat(response.content()).isEqualTo(request.content());
            soft.assertThat(response.starScore()).isEqualTo(request.starScore());
        });
    }

    @Transactional
    @DisplayName("유저는 리뷰를 삭제할 수 있다.")
    @Test
    void deleteReview() {
        // given
        var citizenId = 1L;
        var heroId = 2L;
        var mission = createMissionWithMissionStatus(
                citizenId,
                MissionStatus.MISSION_COMPLETED
        );
        var createServiceRequest = createReviewCreateServiceRequest(
                mission,
                citizenId,
                heroId
        );
        var reviewCreateResponse = reviewService.create(
                createServiceRequest,
                Optional.empty()
        );

        // when
        reviewService.delete(reviewCreateResponse.id());

        // then
        assertSoftly(soft -> {
            soft.assertThat(reviewRepository.findAll()).hasSize(0);
        });
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

    private ReviewUpdateServiceRequest createReviewUpdateServiceRequest() {
        return ReviewUpdateServiceRequest.builder()
                .content("updatedContent")
                .starScore(5)
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
                .missionCategory(missionCategoryRepository.findById(1L).get())
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
