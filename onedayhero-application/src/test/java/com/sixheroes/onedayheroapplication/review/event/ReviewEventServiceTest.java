package com.sixheroes.onedayheroapplication.review.event;

import com.sixheroes.onedayheroapplication.IntegrationApplicationEventTest;
import com.sixheroes.onedayheroapplication.alarm.dto.AlarmPayload;
import com.sixheroes.onedayheroapplication.review.event.dto.ReviewCreateEvent;
import com.sixheroes.onedayheroapplication.review.event.dto.ReviewEventAction;
import com.sixheroes.onedayherodomain.mission.Mission;
import com.sixheroes.onedayherodomain.mission.MissionInfo;
import com.sixheroes.onedayherodomain.mission.MissionStatus;
import com.sixheroes.onedayherodomain.review.Review;
import com.sixheroes.onedayherodomain.user.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;

@Transactional
class ReviewEventServiceTest extends IntegrationApplicationEventTest {

    @DisplayName("리뷰 생성 알림을 위한 데이터를 조회하고 알림 이벤트를 발행한다.")
    @Test
    void notifyReviewCreateEvent() {
        // given
        var hero = createUser();
        userRepository.save(hero);

        var citizenId = 2L;
        var review = createReview(hero.getId(), citizenId);
        reviewRepository.save(review);

        var reviewCreateEvent = new ReviewCreateEvent(review.getId());

        // when
        reviewEventService.notifyReviewCreate(reviewCreateEvent);

        // then
        var alarmPayloadOptional= applicationEvents.stream(AlarmPayload.class).findFirst();
        assertThat(alarmPayloadOptional).isNotEmpty();
        var alarmPayload = alarmPayloadOptional.get();
        assertThat(alarmPayload.alarmType()).isEqualTo(ReviewEventAction.REVIEW_CREATE.name());
        assertThat(alarmPayload.userId()).isEqualTo(citizenId);
        assertThat(alarmPayload.data()).contains(
            entry("senderNickname", hero.getUserBasicInfo().getNickname()),
            entry("reviewId", review.getId()),
            entry("missionTitle", review.getMissionTitle())
        );
    }

    private Review createReview(
        Long heroId,
        Long citizenId
    ) {
        return Review.builder()
            .senderId(heroId)
            .receiverId(citizenId)
            .missionTitle("[급구!!] 편의점 알바 대타 8시간 구합니다.")
            .categoryId(1L)
            .starScore(5)
            .content("좋았어요")
            .build();
    }

    private User createUser() {
        return User.builder()
            .email(createEmail())
            .userBasicInfo(createUserBasicInfo())
            .userFavoriteWorkingDay(createUserFavoriteWorkingDay())
            .userSocialType(UserSocialType.KAKAO)
            .userRole(UserRole.MEMBER)
            .build();
    }

    private Email createEmail() {
        return Email.builder()
            .email("abc@123.com")
            .build();
    }

    private UserBasicInfo createUserBasicInfo() {
        return UserBasicInfo.builder()
            .nickname("닉네임")
            .birth(LocalDate.of(1990, 1, 1))
            .gender(UserGender.MALE)
            .introduce("자기 소개")
            .build();
    }

    private UserFavoriteWorkingDay createUserFavoriteWorkingDay() {
        return UserFavoriteWorkingDay.builder()
            .favoriteDate(List.of(Week.MON, Week.FRI))
            .favoriteStartTime(LocalTime.of(12, 0, 0))
            .favoriteEndTime(LocalTime.of(18, 0, 0))
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