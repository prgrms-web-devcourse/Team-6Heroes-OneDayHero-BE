package com.sixheroes.onedayheroapi.user;

import com.sixheroes.onedayheroapi.global.argumentsresolver.authuser.AuthUser;
import com.sixheroes.onedayheroapi.global.response.ApiResponse;
import com.sixheroes.onedayheroapi.global.s3.MultipartFileMapper;
import com.sixheroes.onedayheroapi.user.request.UserUpdateRequest;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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

    @PostMapping
    public ResponseEntity<ApiResponse<UserUpdateResponse>> updateUser(
        @AuthUser Long userId,
        @Valid @RequestPart UserUpdateRequest userUpdateRequest,
        @RequestPart(required = false) List<MultipartFile> userImages
    ) {
        var userUpdateResponse = userService.updateUser(
            userId,
            userUpdateRequest.toService(),
            MultipartFileMapper.mapToServiceRequests(userImages)
        );

        return ResponseEntity.ok(ApiResponse.ok(userUpdateResponse));
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

    @DeleteMapping("/user-images/{userImageId}")
    public ResponseEntity<ApiResponse<Void>> deleteUserImage(
        @AuthUser Long userId,
        @PathVariable Long userImageId
    ) {
        userService.deleteUserImage(userId, userImageId);

        return new ResponseEntity<>(ApiResponse.noContent(), HttpStatus.NO_CONTENT);
    }
}
