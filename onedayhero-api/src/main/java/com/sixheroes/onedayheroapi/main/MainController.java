package com.sixheroes.onedayheroapi.main;

import com.sixheroes.onedayheroapi.global.argumentsresolver.authuser.AuthUser;
import com.sixheroes.onedayheroapi.global.response.ApiResponse;
import com.sixheroes.onedayheroapplication.main.MainResponse;
import com.sixheroes.onedayheroapplication.main.MainService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/main")
public class MainController {

    private final MainService mainService;

    @GetMapping
    public ResponseEntity<ApiResponse<MainResponse>> callMainResponse(
            @AuthUser Long userId
    ) {
        var result = mainService.findMainResponse(userId);

        return ResponseEntity.ok(ApiResponse.ok(result));
    }
}
