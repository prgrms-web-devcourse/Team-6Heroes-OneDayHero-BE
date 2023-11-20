package com.sixheroes.onedayheroapplication.review;


import com.sixheroes.onedayherocommon.error.ErrorCode;
import com.sixheroes.onedayherodomain.review.Review;
import com.sixheroes.onedayherodomain.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;


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
                    return new NoSuchElementException(ErrorCode.T_001.name());
                });
    }
}
