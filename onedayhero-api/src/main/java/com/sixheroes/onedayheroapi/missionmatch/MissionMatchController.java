package com.sixheroes.onedayheroapi.missionmatch;

import com.sixheroes.onedayheroapi.global.response.ApiResponse;
import com.sixheroes.onedayheroapi.missionmatch.request.MissionMatchCreateRequest;
import com.sixheroes.onedayheroapi.missionmatch.request.MissionMatchGiveUpRequest;
import com.sixheroes.onedayheroapi.missionmatch.request.MissionMatchWithdrawRequest;
import com.sixheroes.onedayheroapplication.missionmatch.MissionMatchService;
import com.sixheroes.onedayheroapplication.missionmatch.response.MissionMatchCreateResponse;
import com.sixheroes.onedayheroapplication.missionmatch.response.MissionMatchGiveUpResponse;
import com.sixheroes.onedayheroapplication.missionmatch.response.MissionMatchWithdrawResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RequiredArgsConstructor
@RequestMapping("/api/v1/mission-matches")
@RestController
public class MissionMatchController {

    private final MissionMatchService missionMatchService;

    @PostMapping
    public ResponseEntity<ApiResponse<MissionMatchCreateResponse>> createMissionMatch(
        @Valid @RequestBody MissionMatchCreateRequest request
    ) {
        var response = missionMatchService.createMissionMatch(request.toService());

        //TODO : URI 주소 미정
        return ResponseEntity.created(URI.create("/api/v1/me"))
                .body(ApiResponse.created(response));
    }

    @PutMapping("/withdraw")
    public ResponseEntity<ApiResponse<MissionMatchWithdrawResponse>> withdrawMissionMatch(
            @Valid @RequestBody MissionMatchWithdrawRequest request
    ) {
        var response = missionMatchService.withdrawMissionMatch(request.toService());
        return ResponseEntity.ok()
                .body(ApiResponse.ok(response));
    }

    @PutMapping("/give-up")
    public ResponseEntity<ApiResponse<MissionMatchGiveUpResponse>> giveUpMissionMatch(
            @Valid @RequestBody MissionMatchGiveUpRequest request
    ) {
        var response = missionMatchService.giveUpMissionMatch(request.toService());
        return ResponseEntity.ok()
                .body(ApiResponse.ok(response));
    }
}
