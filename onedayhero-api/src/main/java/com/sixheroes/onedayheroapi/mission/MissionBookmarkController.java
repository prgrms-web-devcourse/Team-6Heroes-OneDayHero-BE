package com.sixheroes.onedayheroapi.mission;

import com.sixheroes.onedayheroapi.global.argumentsresolver.authuser.AuthUser;
import com.sixheroes.onedayheroapi.global.response.ApiResponse;
import com.sixheroes.onedayheroapi.mission.request.MissionBookmarkCancelRequest;
import com.sixheroes.onedayheroapi.mission.request.MissionBookmarkCreateRequest;
import com.sixheroes.onedayheroapplication.mission.MissionBookmarkService;
import com.sixheroes.onedayheroapplication.mission.response.MissionBookmarkCancelResponse;
import com.sixheroes.onedayheroapplication.mission.response.MissionBookmarkCreateResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RequiredArgsConstructor
@RequestMapping("/api/v1/bookmarks")
@RestController
public class MissionBookmarkController {

    private final MissionBookmarkService missionBookmarkService;

    @PostMapping
    public ResponseEntity<ApiResponse<MissionBookmarkCreateResponse>> createMissionBookmark(
            @AuthUser Long userId,
            @Valid @RequestBody MissionBookmarkCreateRequest request
    ) {
        var response = missionBookmarkService.createMissionBookmark(userId, request.toService());
        return ResponseEntity.created(URI.create("/api/v1/missions/" + response.missionId())).body(ApiResponse.created(response));
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<MissionBookmarkCancelResponse>> cancelMissionBookmark(
            @AuthUser Long userId,
            @Valid @RequestBody MissionBookmarkCancelRequest request
    ) {
        var response = missionBookmarkService.cancelMissionBookmark(userId, request.toService());
        return ResponseEntity.ok().body(ApiResponse.ok(response));
    }
}
