package com.sixheroes.onedayheroapi.review;


import com.sixheroes.onedayheroapi.global.argumentsresolver.authuser.AuthUser;
import com.sixheroes.onedayheroapi.global.response.ApiResponse;
import com.sixheroes.onedayheroapplication.review.ReviewImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/review-images")
@RestController
public class ReviewImageController {

    private final ReviewImageService reviewImageService;

    @DeleteMapping("/{reviewImageId}")
    public ResponseEntity<ApiResponse<Void>> deleteReviewImage(
            @AuthUser Long userId,
            @PathVariable Long reviewImageId
    ) {
        reviewImageService.delete(userId, reviewImageId);

        return new ResponseEntity<>(ApiResponse.noContent(), HttpStatus.NO_CONTENT);
    }
}
