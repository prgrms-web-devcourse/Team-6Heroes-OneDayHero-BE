package com.sixheroes.onedayheroapplication.review;

import com.amazonaws.services.s3.model.PutObjectRequest;
import com.sixheroes.onedayheroapplication.global.s3.S3ImageDeleteService;
import com.sixheroes.onedayheroapplication.global.s3.S3ImageUploadService;
import com.sixheroes.onedayheroapplication.global.s3.dto.request.S3ImageUploadServiceRequest;
import com.sixheroes.onedayheroapplication.review.reqeust.ReviewCreateServiceRequest;
import com.sixheroes.onedayheroapplication.review.reqeust.ReviewUpdateServiceRequest;
import com.sixheroes.onedayheroapplication.review.response.ReceivedReviewViewResponse;
import com.sixheroes.onedayheroapplication.review.response.ReviewResponse;
import com.sixheroes.onedayheroapplication.review.response.SentReviewViewResponse;
import com.sixheroes.onedayherodomain.review.repository.ReviewImageRepository;
import com.sixheroes.onedayherodomain.review.repository.ReviewRepository;
import com.sixheroes.onedayheroquerydsl.review.ReviewQueryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewQueryRepository reviewQueryRepository;
    private final ReviewImageRepository reviewImageRepository;
    private final S3ImageUploadService s3ImageUploadService;
    private final S3ImageDeleteService s3ImageDeleteService;

    public ReviewResponse viewReviewDetail(
            Long reviewId
    ) {
        throw new UnsupportedOperationException();
    }

    public SentReviewViewResponse viewSentReviews(
            Pageable pageable,
            Long reviewId
    ) {
        throw new UnsupportedOperationException();
    }

    public ReceivedReviewViewResponse viewReceivedReviews(
            Pageable pageable,
            Long reviewId
    ) {
        throw new UnsupportedOperationException();
    }

    @Transactional
    public ReviewResponse create(
            ReviewCreateServiceRequest request,
            Optional<List<S3ImageUploadServiceRequest>> imageUploadRequests
    ) {
        throw new UnsupportedOperationException();
    }

    @Transactional
    public ReviewResponse update(
            Long reviewId,
            ReviewUpdateServiceRequest request
    ) {
        throw new UnsupportedOperationException();
    }

    @Transactional
    public void delete(
            Long reviewId
    ) {
        throw new UnsupportedOperationException();
    }
}
