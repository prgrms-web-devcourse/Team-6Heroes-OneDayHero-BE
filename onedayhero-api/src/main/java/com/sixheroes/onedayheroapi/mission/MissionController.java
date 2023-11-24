package com.sixheroes.onedayheroapi.mission;

import com.sixheroes.onedayheroapi.global.argumentsresolver.authuser.AuthUser;
import com.sixheroes.onedayheroapi.global.response.ApiResponse;
import com.sixheroes.onedayheroapi.mission.request.MissionCreateRequest;
import com.sixheroes.onedayheroapi.mission.request.MissionExtendRequest;
import com.sixheroes.onedayheroapi.mission.request.MissionFindFilterRequest;
import com.sixheroes.onedayheroapi.mission.request.MissionUpdateRequest;
import com.sixheroes.onedayheroapplication.mission.MissionService;
import com.sixheroes.onedayheroapplication.mission.repository.response.MissionMatchingResponses;
import com.sixheroes.onedayheroapplication.mission.response.MissionCompletedResponse;
import com.sixheroes.onedayheroapplication.mission.response.MissionIdResponse;
import com.sixheroes.onedayheroapplication.mission.response.MissionProgressResponse;
import com.sixheroes.onedayheroapplication.mission.response.MissionResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/missions")
@RestController
public class MissionController {

    private final MissionService missionService;

    @GetMapping("/{missionId}")
    public ResponseEntity<ApiResponse<MissionResponse>> findMission(
            @PathVariable Long missionId,
            @AuthUser Long userId
    ) {
        var result = missionService.findOne(userId, missionId);

        return ResponseEntity.ok(ApiResponse.ok(result));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Slice<MissionResponse>>> findMissions(
            @AuthUser Long userId,
            @PageableDefault(size = 5) Pageable pageable,
            @Valid @ModelAttribute MissionFindFilterRequest request
    ) {
        var result = missionService.findAllByDynamicCondition(pageable, request.toService());

        return ResponseEntity.ok(ApiResponse.ok(result));
    }

    @GetMapping("/matching")
    public ResponseEntity<ApiResponse<MissionMatchingResponses>> findProgressMission(
            @AuthUser Long userId
    ) {
        var result = missionService.findMatchingMissionByUserId(userId);

        return ResponseEntity.ok(ApiResponse.ok(result));
    }

    @GetMapping("/progress")
    public ResponseEntity<ApiResponse<Slice<MissionProgressResponse>>> findProgressMission(
            @PageableDefault(size = 5) Pageable pageable,
            @AuthUser Long userId
    ) {
        var result = missionService.findProgressMission(pageable, userId);

        return ResponseEntity.ok(ApiResponse.ok(result));
    }

    @GetMapping("/completed")
    public ResponseEntity<ApiResponse<Slice<MissionCompletedResponse>>> findCompletedMission(
            @PageableDefault(size = 5) Pageable pageable,
            @AuthUser Long userId
    ) {
        var result = missionService.findCompletedMissionByUserId(pageable, userId);

        return ResponseEntity.ok(ApiResponse.ok(result));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<MissionIdResponse>> createMission(
            @AuthUser Long userId,
            @Valid @RequestPart MissionCreateRequest missionCreateRequest,
            @RequestPart List<MultipartFile> multipartFiles
    ) {
        var registerDateTime = LocalDateTime.now();
        var result = missionService.createMission(missionCreateRequest.toService(multipartFiles, userId), registerDateTime);

        return ResponseEntity.created(URI.create("/api/v1/missions/" + result.id()))
                .body(ApiResponse.created(result));
    }

    @PostMapping("/{missionId}")
    public ResponseEntity<ApiResponse<MissionIdResponse>> updateMission(
            @PathVariable Long missionId,
            @AuthUser Long userId,
            @Valid @RequestPart MissionUpdateRequest missionUpdateRequest,
            @RequestPart List<MultipartFile> multipartFiles
    ) {
        var modifiedDateTime = LocalDateTime.now();
        var result = missionService.updateMission(missionId, missionUpdateRequest.toService(multipartFiles, userId), modifiedDateTime);

        return ResponseEntity.ok(ApiResponse.ok(result));
    }

    @PatchMapping("/{missionId}/extend")
    public ResponseEntity<ApiResponse<MissionIdResponse>> extendMission(
            @AuthUser Long userId,
            @PathVariable Long missionId,
            @Valid @RequestBody MissionExtendRequest request
    ) {
        var modifiedDateTime = LocalDateTime.now();
        var result = missionService.extendMission(missionId, request.toService(userId), modifiedDateTime);

        return ResponseEntity.ok(ApiResponse.ok(result));
    }

    @PatchMapping("/{missionId}/complete")
    public ResponseEntity<ApiResponse<MissionIdResponse>> completeMission(
            @AuthUser Long userId,
            @PathVariable Long missionId
    ) {
        var result = missionService.completeMission(missionId, userId);

        return ResponseEntity.ok(ApiResponse.ok(result));
    }

    @DeleteMapping("/{missionId}")
    public ResponseEntity<ApiResponse<Void>> deleteMission(
            @AuthUser Long userId,
            @PathVariable Long missionId
    ) {
        missionService.deleteMission(missionId, userId);
        return new ResponseEntity<>(ApiResponse.noContent(), HttpStatus.NO_CONTENT);
    }
}
