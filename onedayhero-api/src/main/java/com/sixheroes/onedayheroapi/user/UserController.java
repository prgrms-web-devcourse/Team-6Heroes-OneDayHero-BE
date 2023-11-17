package com.sixheroes.onedayheroapi.user;

import com.sixheroes.onedayheroapi.global.argumentsresolver.authuser.AuthUser;
import com.sixheroes.onedayheroapi.global.response.ApiResponse;
import com.sixheroes.onedayheroapi.user.request.UserUpadateRequest;
import com.sixheroes.onedayheroapplication.mission.MissionBookmarkService;
import com.sixheroes.onedayheroapplication.mission.response.MissionBookmarkMeViewResponse;
import com.sixheroes.onedayheroapplication.review.ReviewService;
import com.sixheroes.onedayheroapplication.review.response.ReceivedReviewResponse;
import com.sixheroes.onedayheroapplication.review.response.SentReviewResponse;
import com.sixheroes.onedayheroapplication.user.UserService;
import com.sixheroes.onedayheroapplication.user.response.UserResponse;
import com.sixheroes.onedayheroapplication.user.response.UserUpdateResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
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

    @GetMapping
    public ResponseEntity<ApiResponse<UserResponse>> findUser(
        @AuthUser Long userId
    ) {
        var userResponse = userService.findUser(userId);

        return ResponseEntity.ok(ApiResponse.ok(userResponse));
    }

    @PatchMapping
    public ResponseEntity<ApiResponse<UserUpdateResponse>> updateUser(
        @AuthUser Long userId,
        @Valid @RequestBody UserUpadateRequest userUpadateRequest
    ) {
        var userUpdateResponse = userService.updateUser(userId, userUpadateRequest.toService());

        return ResponseEntity.ok(ApiResponse.ok(userUpdateResponse));
    }

    @GetMapping("/reviews/send")
    public ResponseEntity<ApiResponse<Slice<SentReviewResponse>>> viewSentReviews(
            @PageableDefault(size = 5) Pageable pageable,
            @AuthUser Long userId
      
    ) {
        var viewResponse = reviewService.viewSentReviews(
                pageable,
                userId
        );

        return ResponseEntity.ok().body(ApiResponse.ok(viewResponse));
    }

    @GetMapping("/reviews/receive")
    public ResponseEntity<ApiResponse<Slice<ReceivedReviewResponse>>> viewReceivedReviews(
            @PageableDefault(size = 5) Pageable pageable,
            @AuthUser Long userId
    ) {
        var viewResponse = reviewService.viewReceivedReviews(
                pageable,
                userId
        );

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
    public ResponseEntity<ApiResponse<UserUpdateResponse>> turnHeroModeOn(
            @AuthUser Long userId
    ) {
        var userUpdateResponse = userService.turnOnHeroMode(userId);

        return ResponseEntity.ok(ApiResponse.ok(userUpdateResponse));
    }

    @PatchMapping("/change-citizen")
    public ResponseEntity<ApiResponse<UserUpdateResponse>> turnHeroModeOff(
            @AuthUser Long userId
    ) {
        var userUpdateResponse = userService.turnOffHeroMode(userId);

        return ResponseEntity.ok(ApiResponse.ok(userUpdateResponse));
    }
}
