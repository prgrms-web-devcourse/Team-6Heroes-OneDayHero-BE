package com.sixheroes.onedayheroapi.missionproposal;

import com.sixheroes.onedayheroapi.global.response.ApiResponse;
import com.sixheroes.onedayheroapi.missionproposal.request.MissionProposalApproveRequest;
import com.sixheroes.onedayheroapi.missionproposal.request.MissionProposalCreateRequest;
import com.sixheroes.onedayheroapi.missionproposal.request.MissionProposalRejectRequest;
import com.sixheroes.onedayheroapplication.missionproposal.MissionProposalService;
import com.sixheroes.onedayheroapplication.missionproposal.response.MissionProposalApproveResponse;
import com.sixheroes.onedayheroapplication.missionproposal.response.MissionProposalCreateResponse;
import com.sixheroes.onedayheroapplication.missionproposal.response.MissionProposalRejectResponse;
import com.sixheroes.onedayheroapplication.missionproposal.response.MissionProposalResponse;
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
        @Valid @RequestBody MissionProposalCreateRequest missionRequestCreateRequest
    ) {
        var missionProposal = missionProposalService.createMissionProposal(missionRequestCreateRequest.toService());

        return ResponseEntity.created(URI.create("/api/v1/mission-proposals/" + missionProposal.missionProposalId()))
            .body(ApiResponse.created(missionProposal));
    }

    @PatchMapping("/{missionProposalId}/approve")
    public ResponseEntity<ApiResponse<MissionProposalApproveResponse>> approveMissionRequest(
        @PathVariable Long missionProposalId,
        @Valid @RequestBody MissionProposalApproveRequest missionProposalApproveRequest) {
        var missionProposalApproveResponse = missionProposalService.approveMissionProposal(
            missionProposalId,
            missionProposalApproveRequest.toService()
        );

        return ResponseEntity.ok(ApiResponse.ok(missionProposalApproveResponse));
    }

    @PatchMapping("/{missionRequestId}/reject")
    public ResponseEntity<ApiResponse<MissionProposalRejectResponse>> rejectMissionRequest(
        @PathVariable Long missionRequestId,
        @Valid @RequestBody MissionProposalRejectRequest missionRequestRejectRequest
    ) {
        var missionRequestRejectResponse = missionProposalService.rejectMissionProposal(
            missionRequestId,
            missionRequestRejectRequest.toService()
        );

        return ResponseEntity.ok(ApiResponse.ok(missionRequestRejectResponse));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<MissionProposalResponse>> findMissionRequest(
        @RequestParam Long heroId,
        Pageable pageable
    ) {
        var missionRequest = missionProposalService.findMissionProposal(heroId, pageable);

        return ResponseEntity.ok(ApiResponse.ok(missionRequest));
    }
}