package com.sixheroes.onedayheroapplication.review;

import com.sixheroes.onedayheroapplication.global.s3.S3ImageDeleteService;
import com.sixheroes.onedayheroapplication.global.s3.S3ImageDirectoryProperties;
import com.sixheroes.onedayheroapplication.global.s3.S3ImageUploadService;
import com.sixheroes.onedayheroapplication.global.s3.dto.request.S3ImageDeleteServiceRequest;
import com.sixheroes.onedayheroapplication.global.s3.dto.request.S3ImageUploadServiceRequest;
import com.sixheroes.onedayheroapplication.global.s3.dto.response.S3ImageUploadServiceResponse;
import com.sixheroes.onedayheroapplication.mission.MissionReader;
import com.sixheroes.onedayheroapplication.review.repository.ReviewQueryRepository;
import com.sixheroes.onedayheroapplication.review.repository.response.ReviewDetailQueryResponse;
import com.sixheroes.onedayheroapplication.review.reqeust.ReviewCreateServiceRequest;
import com.sixheroes.onedayheroapplication.review.reqeust.ReviewUpdateServiceRequest;
import com.sixheroes.onedayheroapplication.review.response.*;
import com.sixheroes.onedayherocommon.error.ErrorCode;
import com.sixheroes.onedayherodomain.review.Review;
import com.sixheroes.onedayherodomain.review.ReviewImage;
import com.sixheroes.onedayherodomain.review.repository.ReviewImageRepository;
import com.sixheroes.onedayherodomain.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ReviewService {

    private final ReviewReader reviewReader;
    private final ReviewRepository reviewRepository;
    private final ReviewImageRepository reviewImageRepository;
    private final ReviewQueryRepository reviewQueryRepository;
    private final MissionReader missionReader;
    private final S3ImageDirectoryProperties properties;
    private final S3ImageUploadService s3ImageUploadService;
    private final S3ImageDeleteService s3ImageDeleteService;

    private final static Function<S3ImageUploadServiceResponse, ReviewImage> reviewImageMapper = uploadServiceResponse ->
            ReviewImage.createReviewImage(
                    uploadServiceResponse.originalName(),
                    uploadServiceResponse.uniqueName(),
                    uploadServiceResponse.path()
            );

    private final static Function<ReviewImage, S3ImageDeleteServiceRequest> s3DeleteRequestMapper = reviewImage ->
            S3ImageDeleteServiceRequest.builder()
                    .imageId(reviewImage.getId())
                    .uniqueName(reviewImage.getUniqueName())
                    .build();

    public ReviewDetailResponse viewReviewDetail(
            Long reviewId
    ) {
        var optionalReviewImages = reviewImageRepository.findByReviewId(reviewId);
        var queryResponse = reviewQueryRepository.viewReviewDetail(reviewId);

        if (queryResponse.isEmpty()) {
            log.debug("리뷰 상세 조회에 필요한 데이터가 존재하지 않습니다.");
            throw new IllegalStateException(ErrorCode.T_001.name());
        }

        return ReviewDetailResponse.of(
                queryResponse.get(),
                optionalReviewImages
        );
    }

    public SentReviewViewResponse viewSentReviews(
            Pageable pageable,
            Long userId
    ) {
        var queryResponse = reviewQueryRepository.viewSentReviews(
                pageable,
                userId
        );

        return SentReviewViewResponse.of(
                userId,
                queryResponse
        );
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
        var mission = missionReader.findOne(request.missionId());
        mission.validateMissionCompleted();

        var review = request.toEntity();
        setReviewImages(
                imageUploadRequests,
                review
        );

        var createdReview = reviewRepository.save(review);

        return ReviewResponse.from(createdReview);
    }

    @Transactional
    public ReviewResponse update(
            Long reviewId,
            ReviewUpdateServiceRequest request
    ) {
        var review = reviewReader.findById(reviewId);
        review.update(
                request.content(),
                request.starScore()
        );

        return ReviewResponse.from(review);
    }

    @Transactional
    public void delete(
            Long reviewId
    ) {
        var review = reviewReader.findById(reviewId);
        deleteReviewImages(review);

        reviewRepository.delete(review);
    }

    private void setReviewImages(
            Optional<List<S3ImageUploadServiceRequest>> imageUploadRequests,
            Review review
    ) {
        if (imageUploadRequests.isEmpty()) {
            return;
        }

        var s3ImageUploadServiceResponses = s3ImageUploadService.uploadImages(
                imageUploadRequests.get(),
                properties.getReviewDir()
        );
        var reviewImages = s3ImageUploadServiceResponses
                .stream()
                .map(reviewImageMapper)
                .toList();

        review.setReviewImages(reviewImages);
    }

    private void deleteReviewImages(
            Review review
    ) {
        if (!review.hasImage()) {
            return;
        }

        var s3ImageDeleteServiceRequests = review.getReviewImages()
                .stream()
                .map(s3DeleteRequestMapper)
                .toList();

        s3ImageDeleteService.deleteImages(s3ImageDeleteServiceRequests);
    }
}
