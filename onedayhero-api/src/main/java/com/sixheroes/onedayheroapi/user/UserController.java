package com.sixheroes.onedayheroapi.user;

import com.sixheroes.onedayheroapi.global.argumentsresolver.authuser.AuthUser;
import com.sixheroes.onedayheroapi.global.response.ApiResponse;
import com.sixheroes.onedayheroapi.user.request.UserUpadateRequest;
import com.sixheroes.onedayheroapplication.mission.MissionBookmarkService;
import com.sixheroes.onedayheroapplication.mission.response.MissionBookmarkMeViewResponse;
import com.sixheroes.onedayheroapplication.review.ReviewService;
import com.sixheroes.onedayheroapplication.review.response.ReceivedReviewViewResponse;
import com.sixheroes.onedayheroapplication.review.response.SentReviewViewResponse;
import com.sixheroes.onedayheroapplication.user.UserService;
import com.sixheroes.onedayheroapplication.user.response.UserResponse;
import com.sixheroes.onedayheroapplication.user.response.UserUpdateResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/v1/me")
@RestController
public class UserController {

    private final UserService userService;
    private final MissionBookmarkService missionBookmarkService;
    private final ReviewService reviewService;

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserResponse>> findUser(
        @PathVariable Long userId // TODO JWT 생성 시 변경
    ) {
        var userResponse = userService.findUser(userId);

        return ResponseEntity.ok(ApiResponse.ok(userResponse));
    }

    @PatchMapping
    public ResponseEntity<ApiResponse<UserUpdateResponse>> updateUser(
        @Valid @RequestBody UserUpadateRequest userUpadateRequest
    ) {
        var userUpdateResponse = userService.updateUser(userUpadateRequest.toService());

        return ResponseEntity.ok(ApiResponse.ok(userUpdateResponse));
    }

    @GetMapping("/reviews/send")
    public ResponseEntity<ApiResponse<SentReviewViewResponse>> viewSentReviews(
            @PageableDefault(size = 5) Pageable pageable,
            @AuthUser Long userId
    ) {
        var viewResponse = reviewService.viewSentReviews(pageable, userId);

        return ResponseEntity.ok().body(ApiResponse.ok(viewResponse));
    }

    @GetMapping("/reviews/receive")
    public ResponseEntity<ApiResponse<ReceivedReviewViewResponse>> viewReceivedReviews(
            @PageableDefault(size = 5) Pageable pageable,
            @AuthUser Long userId
    ) {
        var viewResponse = reviewService.viewReceivedReviews(pageable, userId);

        return ResponseEntity.ok().body(ApiResponse.ok(viewResponse));
    }

    @GetMapping("/bookmarks")
    public ResponseEntity<ApiResponse<MissionBookmarkMeViewResponse>> viewBookmarks(
            @PageableDefault(size = 3) Pageable pageable,
            @AuthUser Long userId
    ) {
        var viewResponse = missionBookmarkService.viewMyBookmarks(
                pageable,
                userId
        );

        return ResponseEntity.ok().body(ApiResponse.ok(viewResponse));
    }

    @PatchMapping("/change-hero")
    public ResponseEntity<ApiResponse<UserResponse>> turnHeroModeOn(
        // TODO JWT 생기면 userId 받기
    ) {
        var userId = 1L;
        var userResponse = userService.turnHeroModeOn(userId);

        return ResponseEntity.ok(ApiResponse.ok(userResponse));
    }

    @PatchMapping("/change-citizen")
    public ResponseEntity<ApiResponse<UserResponse>> turnHeroModeOff(
        // TODO JWT 생기면 userId 받기
    ) {
        var userId = 1L;
        var userResponse = userService.turnHeroModeOff(userId);

        return ResponseEntity.ok(ApiResponse.ok(userResponse));
    }
}
