package com.sixheroes.onedayheroapplication.review.event;


import com.sixheroes.onedayheroapplication.alarm.dto.AlarmPayload;
import com.sixheroes.onedayheroapplication.review.event.dto.ReviewEventAction;
import com.sixheroes.onedayherodomain.review.repository.dto.ReviewCreateEventDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ReviewEventMapper {

    public static AlarmPayload toAlarmPayload(
        ReviewCreateEventDto reviewCreateEventDto
    ) {
        return AlarmPayload.builder()
            .alarmType(ReviewEventAction.REVIEW_CREATE.name())
            .userId(reviewCreateEventDto.receiverId())
            .data(reviewCreateEventDto.toMap())
            .build();
    }
}
