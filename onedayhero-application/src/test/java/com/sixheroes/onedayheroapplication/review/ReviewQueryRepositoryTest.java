package com.sixheroes.onedayheroapplication.review;

import com.sixheroes.onedayheroapplication.IntegrationQueryDslTest;
import com.sixheroes.onedayherodomain.global.DefaultNicknameGenerator;
import com.sixheroes.onedayherodomain.mission.Mission;
import com.sixheroes.onedayherodomain.mission.MissionInfo;
import com.sixheroes.onedayherodomain.mission.MissionStatus;
import com.sixheroes.onedayherodomain.review.Review;
import com.sixheroes.onedayherodomain.user.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;

public class ReviewQueryRepositoryTest extends IntegrationQueryDslTest {

    @Transactional
    @DisplayName("유저는 작성한 리뷰들을 확인할 수 있다.")
    @Test
    void viewSentReviews() {
        // given
        var reviewSender = userRepository.save(createUserA());
        var reviewReceiver = userRepository.save(createUserB());
        var createdReview = createReview(reviewSender.getId(), reviewReceiver.getId());
        reviewRepository.save(createdReview);

        // when
        var queryResponses = reviewQueryRepository.viewSentReviews(PageRequest.of(0, 5), reviewSender.getId());

        // then
        assertThat(queryResponses.getContent()).hasSize(1);
    }


    @Transactional
    @DisplayName("유저는 받은 리뷰들을 확인할 수 있다.")
    @Test
    void viewReceivedReviews() {
        // given
        var reviewSenderA = userRepository.save(createUserA());
        var reviewSenderC = userRepository.save(createUserC());
        var reviewReceiver = userRepository.save(createUserB());
        var createdReviewA = createReview(reviewSenderA.getId(), reviewReceiver.getId());
        var createdReviewC = createReview(reviewSenderC.getId(), reviewReceiver.getId());

        reviewRepository.save(createdReviewA);
        reviewRepository.save(createdReviewC);

        // when
        var queryResponses = reviewQueryRepository.viewReceivedReviews(PageRequest.of(0, 5), reviewReceiver.getId());

        // then
        assertThat(queryResponses.getContent()).hasSize(2);
    }


    private User createUserA() {
        var createdEmail = Email.builder()
                .email("userA@email.com")
                .build();

        return User.signUp(
                createdEmail,
                UserSocialType.KAKAO,
                UserRole.ADMIN,
                UserBasicInfo.initStatus(DefaultNicknameGenerator.generate())
        );
    }

    private User createUserB() {
        var createdEmail = Email.builder()
                .email("userB@email.com")
                .build();

        return User.signUp(
                createdEmail,
                UserSocialType.KAKAO,
                UserRole.ADMIN,
                UserBasicInfo.initStatus(DefaultNicknameGenerator.generate())
        );    }

    private User createUserC() {
        var createdEmail = Email.builder()
                .email("userC@email.com")
                .build();

        return User.signUp(
                createdEmail,
                UserSocialType.KAKAO,
                UserRole.ADMIN,
                UserBasicInfo.initStatus(DefaultNicknameGenerator.generate())
        );    }

    private Review createReview(
            Long senderId,
            Long receiverId
    ) {
        return Review.builder()
                .categoryId(1L)
                .missionTitle("리뷰 타이틀")
                .senderId(senderId)
                .receiverId(receiverId)
                .starScore(3)
                .content("리뷰 내용")
                .build();
    }


    private Mission createMission(
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
