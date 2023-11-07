package com.sixheroes.onedayheroapi.user;

import com.sixheroes.onedayheroapi.global.response.ApiResponse;
import com.sixheroes.onedayheroapi.user.request.UserUpadateRequest;
import com.sixheroes.onedayheroapplication.mission.MissionBookmarkService;
import com.sixheroes.onedayheroapplication.mission.response.MissionBookmarkMeViewResponse;
import com.sixheroes.onedayheroapplication.user.UserService;
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

    @PatchMapping
    public ResponseEntity<ApiResponse<UserUpdateResponse>> updateUser(
        @Valid @RequestBody UserUpadateRequest userUpadateRequest
    ) {
        var userUpdateResponse = userService.updateUser(userUpadateRequest.toService());

        return ResponseEntity.ok(ApiResponse.ok(userUpdateResponse));
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

    //TODO: 로그인 기능 추가 후 제거
    private Long tempUserId() {
        return 1L;
    }
}
