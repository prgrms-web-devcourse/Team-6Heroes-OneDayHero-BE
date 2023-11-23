package com.sixheroes.onedayheroapi.main;

import com.sixheroes.onedayheroapi.global.argumentsresolver.authuser.AuthUser;
import com.sixheroes.onedayheroapi.global.response.ApiResponse;
import com.sixheroes.onedayheroapi.main.request.UserPositionRequest;
import com.sixheroes.onedayheroapplication.main.MainService;
import com.sixheroes.onedayheroapplication.main.response.MainResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/main")
public class MainController {

    private final MainService mainService;

    @GetMapping
    public ResponseEntity<ApiResponse<MainResponse>> callMainResponse(
            @AuthUser Long userId,
            @ModelAttribute UserPositionRequest request
    ) {
        var serverTime = LocalDateTime.now();
        var result = mainService.findMainResponse(userId, request.toService(), serverTime);

        return ResponseEntity.ok(ApiResponse.ok(result));
    }
}
