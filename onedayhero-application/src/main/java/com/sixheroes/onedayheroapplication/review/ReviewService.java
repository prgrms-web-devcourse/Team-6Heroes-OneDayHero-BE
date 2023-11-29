package com.sixheroes.onedayheroapplication.review;

import com.sixheroes.onedayheroapplication.global.s3.S3ImageDeleteService;
import com.sixheroes.onedayheroapplication.global.s3.S3ImageDirectoryProperties;
import com.sixheroes.onedayheroapplication.global.s3.S3ImageUploadService;
import com.sixheroes.onedayheroapplication.global.s3.dto.request.S3ImageDeleteServiceRequest;
import com.sixheroes.onedayheroapplication.global.s3.dto.request.S3ImageUploadServiceRequest;
import com.sixheroes.onedayheroapplication.global.s3.dto.response.S3ImageUploadServiceResponse;
import com.sixheroes.onedayheroapplication.mission.MissionReader;
import com.sixheroes.onedayheroapplication.review.event.dto.ReviewCreateEvent;
import com.sixheroes.onedayheroapplication.review.repository.ReviewQueryRepository;
import com.sixheroes.onedayheroapplication.review.reqeust.ReviewCreateServiceRequest;
import com.sixheroes.onedayheroapplication.review.reqeust.ReviewUpdateServiceRequest;
import com.sixheroes.onedayheroapplication.review.response.ReceivedReviewResponse;
import com.sixheroes.onedayheroapplication.review.response.ReviewDetailResponse;
import com.sixheroes.onedayheroapplication.review.response.ReviewResponse;
import com.sixheroes.onedayheroapplication.review.response.SentReviewResponse;
import com.sixheroes.onedayheroapplication.user.reader.UserReader;
import com.sixheroes.onedayherocommon.error.ErrorCode;
import com.sixheroes.onedayherocommon.exception.BusinessException;
import com.sixheroes.onedayherocommon.exception.EntityNotFoundException;
import com.sixheroes.onedayherodomain.review.Review;
import com.sixheroes.onedayherodomain.review.ReviewImage;
import com.sixheroes.onedayherodomain.review.repository.ReviewImageRepository;
import com.sixheroes.onedayherodomain.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
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
    private final UserReader userReader;
    private final S3ImageDirectoryProperties properties;
    private final S3ImageUploadService s3ImageUploadService;
    private final S3ImageDeleteService s3ImageDeleteService;
    private final ApplicationEventPublisher applicationEventPublisher;

    private final static Function<S3ImageUploadServiceResponse, ReviewImage> reviewImageMapper = uploadServiceResponse ->
            ReviewImage.createReviewImage(
                    uploadServiceResponse.originalName(),
                    uploadServiceResponse.uniqueName(),
                    uploadServiceResponse.path()
            );

    public final static Function<ReviewImage, S3ImageDeleteServiceRequest> s3DeleteRequestMapper = reviewImage ->
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
            log.warn("리뷰 상세 조회에 필요한 데이터가 존재하지 않습니다. reviewId : {}", reviewId);
            throw new BusinessException(ErrorCode.NOT_FOUND_REVIEW);
        }

        var receiveUser = userReader.findOne(queryResponse.get().receiverId());

        return ReviewDetailResponse.of(
                queryResponse.get(),
                optionalReviewImages,
                receiveUser.getUserBasicInfo().getNickname()
        );
    }

    public Slice<SentReviewResponse> viewSentReviews(
            Pageable pageable,
            Long userId
    ) {
        var queryResponse = reviewQueryRepository.viewSentReviews(
                pageable,
                userId
        );

        return queryResponse.map(SentReviewResponse::from);
    }

    public Slice<ReceivedReviewResponse> viewReceivedReviews(
            Pageable pageable,
            Long userId
    ) {
        var queryResponse = reviewQueryRepository.viewReceivedReviews(
                pageable,
                userId
        );

        return queryResponse.map(ReceivedReviewResponse::from);
    }

    @Transactional
    public ReviewResponse create(
            ReviewCreateServiceRequest request,
            List<S3ImageUploadServiceRequest> imageUploadRequests
    ) {
        var mission = missionReader.findOne(request.missionId());
        mission.validateMissionCompleted();

        var review = request.toEntity();
        var reviewImageUploadResponse = s3ImageUploadService.uploadImages(imageUploadRequests, properties.getReviewDir());
        addReviewImages(reviewImageUploadResponse, review);

        var createdReview = reviewRepository.save(review);

        var reviewCreateEvent = ReviewCreateEvent.from(review);
        applicationEventPublisher.publishEvent(reviewCreateEvent);

        return ReviewResponse
                .builder()
                .id(createdReview.getId())
                .build();
    }

    @Transactional
    public ReviewResponse update(
            Long reviewId,
            ReviewUpdateServiceRequest request,
            List<S3ImageUploadServiceRequest> imageUploadRequests
    ) {
        var review = reviewReader.findById(reviewId);
        review.update(request.content(), request.starScore());

        var reviewImageUploadResponse = s3ImageUploadService.uploadImages(imageUploadRequests, properties.getReviewDir());
        addReviewImages(reviewImageUploadResponse, review);

        return ReviewResponse
                .builder()
                .id(review.getId())
                .build();
    }

    @Transactional
    public void delete(
            Long reviewId
    ) {
        var review = reviewReader.findById(reviewId);
        deleteReviewImages(review);

        reviewRepository.delete(review);
    }

    private void addReviewImages(
            List<S3ImageUploadServiceResponse> response,
            Review review
    ) {
        if (response.isEmpty()) {
            return;
        }

        response.stream()
                .map(reviewImageMapper)
                .forEach(review::addImage);
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
