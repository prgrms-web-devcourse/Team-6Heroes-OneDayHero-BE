package com.sixheroes.onedayheroapi.missionmatch;

import com.sixheroes.onedayheroapi.global.argumentsresolver.authuser.AuthUser;
import com.sixheroes.onedayheroapi.global.response.ApiResponse;
import com.sixheroes.onedayheroapi.missionmatch.request.MissionMatchCreateRequest;
import com.sixheroes.onedayheroapi.missionmatch.request.MissionMatchCancelRequest;
import com.sixheroes.onedayheroapplication.missionmatch.MissionMatchService;
import com.sixheroes.onedayheroapplication.missionmatch.response.MissionMatchCreateResponse;
import com.sixheroes.onedayheroapplication.missionmatch.response.MissionMatchCancelResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RequestMapping("/api/v1/mission-matches")
@RestController
public class MissionMatchController {

    private final MissionMatchService missionMatchService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<MissionMatchCreateResponse>> createMissionMatch(
            @AuthUser Long userId,
            @Valid @RequestBody MissionMatchCreateRequest request
    ) {
        var response = missionMatchService.createMissionMatch(userId, request.toService());

        return ResponseEntity.ok().body(ApiResponse.created(response));
    }

    @PutMapping("/cancel")
    public ResponseEntity<ApiResponse<MissionMatchCancelResponse>> cancelMissionMatch(
            @AuthUser Long userId,
            @Valid @RequestBody MissionMatchCancelRequest request
    ) {
        var response = missionMatchService.cancelMissionMatch(userId, request.toService());

        return ResponseEntity.ok().body(ApiResponse.ok(response));
    }
}
