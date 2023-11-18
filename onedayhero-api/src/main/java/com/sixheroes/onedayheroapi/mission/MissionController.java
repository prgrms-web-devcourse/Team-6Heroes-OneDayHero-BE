package com.sixheroes.onedayheroapi.mission;

import com.sixheroes.onedayheroapi.global.response.ApiResponse;
import com.sixheroes.onedayheroapi.mission.request.*;
import com.sixheroes.onedayheroapplication.mission.MissionService;
import com.sixheroes.onedayheroapplication.mission.response.MissionProgressResponses;
import com.sixheroes.onedayheroapplication.mission.response.MissionResponse;
import com.sixheroes.onedayheroapplication.mission.response.MissionResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@RequestMapping("/api/v1/missions")
@RestController
public class MissionController {

    private final MissionService missionService;

    @GetMapping("/{missionId}")
    public ResponseEntity<ApiResponse<MissionResponse>> findMission(
            @PathVariable Long missionId
    ) {
        var result = missionService.findOne(missionId);

        return ResponseEntity.ok(ApiResponse.ok(result));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<MissionResponses>> findMissions(
            @PageableDefault(size = 5) Pageable pageable,
            MissionFindFilterRequest request
    ) {
        var serviceRequest = request.toService();
        var result = missionService.findAllByDynamicCondition(pageable, serviceRequest);

        return ResponseEntity.ok(ApiResponse.ok(result));
    }

    @GetMapping("/progress/{userId}")
    public ResponseEntity<ApiResponse<MissionProgressResponses>> findProgressMission(
            @PageableDefault(size = 5) Pageable pageable,
            @PathVariable Long userId
    ) {
        var result = missionService.findProgressMission(pageable, userId);

        return ResponseEntity.ok(ApiResponse.ok(result));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<MissionResponse>> createMission(
            @Valid @RequestBody MissionCreateRequest request
    ) {
        var registerDateTime = LocalDateTime.now();
        var result = missionService.createMission(request.toService(), registerDateTime);

        return ResponseEntity.created(URI.create("/api/v1/missions/" + result.id()))
                .body(ApiResponse.created(result));
    }

    @PatchMapping("/{missionId}")
    public ResponseEntity<ApiResponse<MissionResponse>> updateMission(
            @PathVariable Long missionId,
            @Valid @RequestBody MissionUpdateRequest request
    ) {
        var modifiedDateTime = LocalDateTime.now();
        var result = missionService.updateMission(missionId, request.toService(), modifiedDateTime);

        return ResponseEntity.ok(ApiResponse.ok(result));
    }

    @PatchMapping("/{missionId}/extend")
    public ResponseEntity<ApiResponse<MissionResponse>> extendMission(
            @PathVariable Long missionId,
            @Valid @RequestBody MissionUpdateRequest request
    ) {
        var modifiedDateTime = LocalDateTime.now();
        var result = missionService.extendMission(missionId, request.toService(), modifiedDateTime);

        return ResponseEntity.ok(ApiResponse.ok(result));
    }

    @PatchMapping("/{missionId}/complete")
    public ResponseEntity<ApiResponse<MissionResponse>> completeMission(
            @PathVariable Long missionId,
            @Valid @RequestBody MissionCompleteRequest request
    ) {
        var result = missionService.completeMission(missionId, request.userId());

        return ResponseEntity.ok(ApiResponse.ok(result));
    }

    @DeleteMapping("/{missionId}")
    public ResponseEntity<ApiResponse<Void>> deleteMission(
            @PathVariable Long missionId,
            @Valid @RequestBody MissionDeleteRequest request
    ) {
        missionService.deleteMission(missionId, request.citizenId());
        return new ResponseEntity<>(ApiResponse.noContent(), HttpStatus.NO_CONTENT);
    }
}
