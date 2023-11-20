package com.sixheroes.onedayheroapi.missionproposal;

import com.sixheroes.onedayheroapi.global.argumentsresolver.authuser.AuthUser;
import com.sixheroes.onedayheroapi.global.response.ApiResponse;
import com.sixheroes.onedayheroapi.missionproposal.request.MissionProposalCreateRequest;
import com.sixheroes.onedayheroapplication.missionproposal.MissionProposalService;
import com.sixheroes.onedayheroapplication.missionproposal.response.MissionProposalApproveResponse;
import com.sixheroes.onedayheroapplication.missionproposal.response.MissionProposalCreateResponse;
import com.sixheroes.onedayheroapplication.missionproposal.response.MissionProposalRejectResponse;
import com.sixheroes.onedayheroapplication.missionproposal.response.MissionProposalResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RequiredArgsConstructor
@RequestMapping("/api/v1/mission-proposals")
@RestController
public class MissionProposalController {

    private final MissionProposalService missionProposalService;

    @PostMapping
    public ResponseEntity<ApiResponse<MissionProposalCreateResponse>> createMissionProposal(
        @AuthUser Long userId,
        @Valid @RequestBody MissionProposalCreateRequest missionRequestCreateRequest
    ) {
        var missionProposal = missionProposalService.createMissionProposal(userId, missionRequestCreateRequest.toService());

        return ResponseEntity.created(URI.create("/api/v1/mission-proposals/" + missionProposal.id()))
            .body(ApiResponse.created(missionProposal));
    }

    @PatchMapping("/{missionProposalId}/approve")
    public ResponseEntity<ApiResponse<MissionProposalApproveResponse>> approveMissionRequest(
            @AuthUser Long userId,
            @PathVariable Long missionProposalId
    ) {
        var missionProposalApproveResponse = missionProposalService.approveMissionProposal(
                userId,
                missionProposalId
        );

        return ResponseEntity.ok(ApiResponse.ok(missionProposalApproveResponse));
    }

    @PatchMapping("/{missionRequestId}/reject")
    public ResponseEntity<ApiResponse<MissionProposalRejectResponse>> rejectMissionRequest(
            @AuthUser Long userId,
            @PathVariable Long missionRequestId
    ) {
        var missionRequestRejectResponse = missionProposalService.rejectMissionProposal(
                userId,
                missionRequestId
        );

        return ResponseEntity.ok(ApiResponse.ok(missionRequestRejectResponse));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<MissionProposalResponses>> findMissionRequest(
        @RequestParam Long heroId,
        Pageable pageable
    ) {
        var missionRequest = missionProposalService.findMissionProposal(heroId, pageable);

        return ResponseEntity.ok(ApiResponse.ok(missionRequest));
    }
}
