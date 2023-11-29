package com.sixheroes.onedayheroapplication.review.event;

import com.sixheroes.onedayheroapplication.review.ReviewReader;
import com.sixheroes.onedayheroapplication.review.event.dto.ReviewCreateEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ReviewEventService {

    private final ReviewReader reviewReader;
    private final ApplicationEventPublisher applicationEventPublisher;

    public void notifyReviewCreate(
        ReviewCreateEvent reviewCreateEvent
    ) {
        var reviewId = reviewCreateEvent.reviewId();
        var reviewCreateEventDto = reviewReader.findReviewCreateEvent(reviewId);

        var alarmPayload = ReviewEventMapper.toAlarmPayload(reviewCreateEventDto);
        applicationEventPublisher.publishEvent(alarmPayload);
    }
}
