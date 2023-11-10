package com.sixheroes.onedayheroapi.review;


import com.sixheroes.onedayheroapi.global.response.ApiResponse;
import com.sixheroes.onedayheroapi.global.s3.MultipartFileMapper;
import com.sixheroes.onedayheroapi.review.request.ReviewCreateRequest;
import com.sixheroes.onedayheroapplication.global.s3.dto.request.S3ImageDeleteServiceRequest;
import com.sixheroes.onedayheroapplication.global.s3.dto.request.S3ImageUploadServiceRequest;
import com.sixheroes.onedayheroapplication.review.ReviewService;
import com.sixheroes.onedayheroapplication.review.response.ReviewResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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

    @GetMapping("/{reviewId}")
    public ResponseEntity<ApiResponse<?>> detailReview(@PathVariable String reviewId) {
        return null;
    }

    @PatchMapping("/{reviewId}")
    public ResponseEntity<ApiResponse<?>> updateReview(@PathVariable String reviewId) {
        return null;
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<ApiResponse<?>> deleteReview(@PathVariable String reviewId) {
        return null;
    }
}
