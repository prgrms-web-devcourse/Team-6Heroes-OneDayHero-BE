package com.sixheroes.onedayheroapi.user;

import com.sixheroes.onedayheroapi.global.response.ApiResponse;
import com.sixheroes.onedayheroapi.user.request.UserUpadateRequest;
import com.sixheroes.onedayheroapplication.user.UserService;
import com.sixheroes.onedayheroapplication.user.response.UserUpdateResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/v1/me")
@RestController
public class UserController {

    private final UserService userService;

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
}
