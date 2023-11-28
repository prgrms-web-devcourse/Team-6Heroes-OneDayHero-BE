package com.sixheroes.onedayheroapplication.review;


import com.sixheroes.onedayherocommon.error.ErrorCode;
import com.sixheroes.onedayherocommon.exception.EntityNotFoundException;
import com.sixheroes.onedayherodomain.review.ReviewImage;
import com.sixheroes.onedayherodomain.review.repository.ReviewImageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Component
public class ReviewImageReader {

    private final ReviewImageRepository reviewImageRepository;

    public ReviewImage findById(
            Long reviewImageId
    ) {
        return reviewImageRepository.findById(reviewImageId)
                .orElseThrow(() -> {
                    log.debug("존재하지 않는 이미지 아이디입니다. reviewImageId : {}", reviewImageId);
                    return new EntityNotFoundException(ErrorCode.NOT_FOUND_IMAGE);
        });
    }
}
