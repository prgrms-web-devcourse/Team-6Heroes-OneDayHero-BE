package com.sixheroes.onedayheroapi.review;


import com.sixheroes.onedayheroapi.global.response.ApiResponse;
import com.sixheroes.onedayheroapi.global.s3.MultipartFileMapper;
import com.sixheroes.onedayheroapi.review.request.ReviewCreateRequest;
import com.sixheroes.onedayheroapi.review.request.ReviewUpdateRequest;
import com.sixheroes.onedayheroapplication.review.ReviewService;
import com.sixheroes.onedayheroapplication.review.response.ReceivedReviewViewResponse;
import com.sixheroes.onedayheroapplication.review.response.ReviewResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/v1/reviews")
@RestController
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping("/users/{userId}/receive")
    public ResponseEntity<ApiResponse<ReceivedReviewViewResponse>> viewUserReceivedReviews(
            @PageableDefault(size = 5) Pageable pageable,
            @PathVariable Long userId
    ) {
        var viewResponse = reviewService.viewReceivedReviews(
                pageable,
                userId
        );

        return ResponseEntity.ok().body(ApiResponse.ok(viewResponse));
    }

    @GetMapping("/{reviewId}")
    public ResponseEntity<ApiResponse<ReviewResponse>> detailReview(
            @PathVariable Long reviewId
    ) {
        var response = reviewService.viewReviewDetail(reviewId);

        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ReviewResponse>> createReview(
            @Valid @RequestPart ReviewCreateRequest reviewCreateRequest,
            @RequestPart(required = false) List<MultipartFile> images
    ) {
        var response = reviewService.create(
                reviewCreateRequest.toService(),
                MultipartFileMapper.mapToServiceRequests(images)
        );

        return ResponseEntity.created(
                URI.create("/api/v1/reviews/" + response.id()))
                .body(ApiResponse.created(response));
    }

    //리뷰 이미지 추가/제거 기능은 Patch 와는 맞지 않음
    @PatchMapping("/{reviewId}")
    public ResponseEntity<ApiResponse<ReviewResponse>> updateReview(
            @PathVariable Long reviewId,
            @Valid @RequestBody ReviewUpdateRequest reviewUpdateRequest
    ) {
        var response = reviewService.update(
                reviewId,
                reviewUpdateRequest.toService()
        );

        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<ApiResponse<Void>> deleteReview(
            @PathVariable Long reviewId
    ) {
        reviewService.delete(reviewId);

        return new ResponseEntity<>(
                ApiResponse.noContent(null),
                HttpStatus.NO_CONTENT
        );
    }
}
