package com.sixheroes.onedayheroapi.mission;

import com.sixheroes.onedayheroapi.global.argumentsresolver.authuser.AuthUser;
import com.sixheroes.onedayheroapi.global.response.ApiResponse;
import com.sixheroes.onedayheroapi.mission.request.MissionBookmarkCancelRequest;
import com.sixheroes.onedayheroapi.mission.request.MissionBookmarkCreateRequest;
import com.sixheroes.onedayheroapplication.mission.MissionBookmarkService;
import com.sixheroes.onedayheroapplication.mission.response.MissionBookmarkResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RequiredArgsConstructor
@RequestMapping("/api/v1/bookmarks")
@RestController
public class MissionBookmarkController {

    private final MissionBookmarkService missionBookmarkService;

    @PostMapping
    public ResponseEntity<ApiResponse<MissionBookmarkResponse>> createMissionBookmark(
            @AuthUser Long userId,
            @Valid @RequestBody MissionBookmarkCreateRequest request
    ) {
        var response = missionBookmarkService.createMissionBookmark(userId, request.toService());

        return ResponseEntity.ok().body(ApiResponse.created(response));
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> cancelMissionBookmark(
            @AuthUser Long userId,
            @Valid @RequestBody MissionBookmarkCancelRequest request
    ) {
        missionBookmarkService.cancelMissionBookmark(userId, request.toService());

        return new ResponseEntity<>(ApiResponse.noContent(), HttpStatus.NO_CONTENT);
    }
}
