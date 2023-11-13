package com.sixheroes.onedayheroapi.user;

import com.sixheroes.onedayheroapi.global.response.ApiResponse;
import com.sixheroes.onedayheroapi.user.request.UserUpadateRequest;
import com.sixheroes.onedayheroapplication.mission.MissionBookmarkService;
import com.sixheroes.onedayheroapplication.mission.response.MissionBookmarkMeViewResponse;
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
    public ResponseEntity<ApiResponse<?>> findSentReviews() {
        return null;
    }

    @GetMapping("/reviews/receive")
    public ResponseEntity<ApiResponse<?>> findReceivedReviews() {
        return null;
    }

    @GetMapping("/bookmarks")
    public ResponseEntity<ApiResponse<MissionBookmarkMeViewResponse>> viewBookmarks(
            @PageableDefault(size = 3) Pageable pageable
            //TODO: @Auth Long userID
    ) {
        var viewResponse = missionBookmarkService.viewMyBookmarks(
                pageable,
                tempUserId()
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
  
    //TODO: 로그인 기능 추가 후 제거
    private Long tempUserId() {
        return 1L;
    }
}