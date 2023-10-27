package com.sixheroes.onedayheroapi.missionrequest;

import com.sixheroes.onedayheroapi.global.response.ApiResponse;
import com.sixheroes.onedayheroapi.missionrequest.request.MissionRequestCreateRequest;
import com.sixheroes.onedayheroapplication.missionrequest.MissionRequestService;
import com.sixheroes.onedayheroapplication.missionrequest.response.MissionRequestCreateResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RequiredArgsConstructor
@RequestMapping("/api/v1/mission-requests")
@RestController
public class MissionRequestController {

    private final MissionRequestService missionRequestService;

    @PostMapping
    public ResponseEntity<ApiResponse<MissionRequestCreateResponse>> createMissionRequest(
        @Valid @RequestBody MissionRequestCreateRequest missionRequestCreateRequest
    ) {
        var missionRequest = missionRequestService.createMissionRequest(missionRequestCreateRequest.toService());

        return ResponseEntity.created(URI.create("/api/v1/mission-requests/" + missionRequest.missionRequestId()))
            .body(ApiResponse.created(missionRequest));
    }
}