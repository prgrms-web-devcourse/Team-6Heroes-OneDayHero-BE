package com.sixheroes.onedayheroapplication.review;

import com.sixheroes.onedayheroapplication.IntegrationApplicationTest;
import com.sixheroes.onedayheroapplication.global.s3.dto.response.S3ImageUploadServiceResponse;
import com.sixheroes.onedayheroapplication.review.reqeust.ReviewCreateServiceRequest;
import com.sixheroes.onedayheroapplication.review.response.ReviewResponse;
import com.sixheroes.onedayherodomain.mission.Mission;
import com.sixheroes.onedayherodomain.mission.MissionInfo;
import com.sixheroes.onedayherodomain.mission.MissionStatus;
import com.sixheroes.onedayherodomain.review.Review;
import com.sixheroes.onedayherodomain.review.ReviewImage;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.SoftAssertions.assertSoftly;


public class ReviewImageServiceTest extends IntegrationApplicationTest {

    @Transactional
    @DisplayName("유저는 리뷰의 이미지를 삭제할 수 있다.")
    @Test
    void deleteReviewImage() {
        // given
        var citizenId = 1L;
        var heroId = 2L;
        var mission = createMissionWithMissionStatus(
                citizenId,
                MissionStatus.MISSION_COMPLETED
        );

        var review = createReviewCreateServiceRequest(
                mission,
                citizenId,
                heroId
        ).toEntity();
        setReviewImage(review);
        reviewRepository.save(review);

        // when
        reviewImageService.delete(review.getSenderId(), 2L);

        var reviewImage = reviewImageRepository.findById(2L);
        Assertions.assertThat(reviewImage).isEmpty();
    }

    private void setReviewImage(
            Review review
    ) {
        var reviewImageA = ReviewImage.createReviewImage(
                "originalA.png",
                "aws-bucket-s3-unique",
                "https:image/path");
        var reviewImageB = ReviewImage.createReviewImage(
                "originalB.png",
                "aws-bucket-s3-unique",
                "https:image/path");

        review.addImage(reviewImageA);
        review.addImage(reviewImageB);
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
