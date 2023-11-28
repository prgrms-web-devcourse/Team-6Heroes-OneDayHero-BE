package com.sixheroes.onedayheroapplication.review;


import com.sixheroes.onedayherocommon.error.ErrorCode;
import com.sixheroes.onedayherocommon.exception.EntityNotFoundException;
import com.sixheroes.onedayherodomain.review.Review;
import com.sixheroes.onedayherodomain.review.repository.ReviewRepository;
import com.sixheroes.onedayherodomain.review.repository.dto.ReviewCreateEventDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Component
public class ReviewReader {

    private final ReviewRepository reviewRepository;

    public Review findById(Long reviewId) {
        return reviewRepository.findById(reviewId)
                .orElseThrow(() -> {
                    log.debug("존재하지 않는 리뷰입니다. reviewId : {}", reviewId);
                    return new EntityNotFoundException(ErrorCode.NOT_FOUND_REVIEW);
                });
    }

    public ReviewCreateEventDto findReviewCreateEvent(Long reviewId) {
        return reviewRepository.findReviewCreateEventById(reviewId)
            .orElseThrow(() -> {
                log.debug("존재하지 않는 리뷰입니다. reviewId : {}", reviewId);
                return new EntityNotFoundException(ErrorCode.NOT_FOUND_REVIEW);
            });
    }
}
