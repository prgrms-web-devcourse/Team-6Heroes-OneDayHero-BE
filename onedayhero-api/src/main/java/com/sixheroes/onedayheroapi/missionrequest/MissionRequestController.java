package com.sixheroes.onedayheroapi.missionrequest;

import com.sixheroes.onedayheroapi.global.response.ApiResponse;
import com.sixheroes.onedayheroapi.missionrequest.request.MissionRequestApproveRequest;
import com.sixheroes.onedayheroapi.missionrequest.request.MissionRequestCreateRequest;
import com.sixheroes.onedayheroapi.missionrequest.request.MissionRequestRejectRequest;
import com.sixheroes.onedayheroapplication.missionrequest.MissionRequestService;
import com.sixheroes.onedayheroapplication.missionrequest.response.MissionRequestApproveResponse;
import com.sixheroes.onedayheroapplication.missionrequest.response.MissionRequestCreateResponse;
import com.sixheroes.onedayheroapplication.missionrequest.response.MissionRequestRejectResponse;
import com.sixheroes.onedayheroapplication.missionrequest.response.MissionRequestResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PatchMapping("/{missionRequestId}/approve")
    public ResponseEntity<ApiResponse<MissionRequestApproveResponse>> approveMissionRequest(
        @PathVariable Long missionRequestId,
        @Valid @RequestBody MissionRequestApproveRequest missionRequestApproveRequest) {
        var missionRequestApproveResponse = missionRequestService.approveMissionRequest(
            missionRequestId,
            missionRequestApproveRequest.toService()
        );

        return ResponseEntity.ok(ApiResponse.ok(missionRequestApproveResponse));
    }

    @PatchMapping("/{missionRequestId}/reject")
    public ResponseEntity<ApiResponse<MissionRequestRejectResponse>> rejectMissionRequest(
        @PathVariable Long missionRequestId,
        @Valid @RequestBody MissionRequestRejectRequest missionRequestRejectRequest
    ) {
        var missionRequestRejectResponse = missionRequestService.rejectMissionRequest(
            missionRequestId,
            missionRequestRejectRequest.toService()
        );

        return ResponseEntity.ok(ApiResponse.ok(missionRequestRejectResponse));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<MissionRequestResponse>> findMissionRequest(
        @RequestParam Long heroId,
        Pageable pageable
    ) {
        var missionRequest = missionRequestService.findMissionRequest(heroId, pageable);

        return ResponseEntity.ok(ApiResponse.ok(missionRequest));
    }
}