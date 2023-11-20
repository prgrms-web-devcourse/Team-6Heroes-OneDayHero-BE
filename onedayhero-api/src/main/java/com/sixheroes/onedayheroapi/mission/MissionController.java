package com.sixheroes.onedayheroapi.mission;

import com.sixheroes.onedayheroapi.global.response.ApiResponse;
import com.sixheroes.onedayheroapi.mission.request.*;
import com.sixheroes.onedayheroapplication.mission.MissionService;
import com.sixheroes.onedayheroapplication.mission.response.MissionCompletedResponse;
import com.sixheroes.onedayheroapplication.mission.response.MissionIdResponse;
import com.sixheroes.onedayheroapplication.mission.response.MissionProgressResponse;
import com.sixheroes.onedayheroapplication.mission.response.MissionResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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

@RequiredArgsConstructor
@RequestMapping("/api/v1/missions")
@RestController
public class MissionController {

    private final MissionService missionService;

    @GetMapping("/{missionId}")
    public ResponseEntity<ApiResponse<MissionResponse>> findMission(
            @PathVariable Long missionId,
            @RequestParam Long userId
    ) {
        var result = missionService.findOne(userId, missionId);

        return ResponseEntity.ok(ApiResponse.ok(result));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Slice<MissionResponse>>> findMissions(
            @PageableDefault(size = 5) Pageable pageable,
            MissionFindFilterRequest request
    ) {
        var serviceRequest = request.toService();
        var result = missionService.findAllByDynamicCondition(pageable, serviceRequest);

        return ResponseEntity.ok(ApiResponse.ok(result));
    }

    @GetMapping("/progress/{userId}")
    public ResponseEntity<ApiResponse<Slice<MissionProgressResponse>>> findProgressMission(
            @PageableDefault(size = 5) Pageable pageable,
            @PathVariable Long userId
    ) {
        var result = missionService.findProgressMission(pageable, userId);

        return ResponseEntity.ok(ApiResponse.ok(result));
    }

    @GetMapping("/completed/{userId}")
    public ResponseEntity<ApiResponse<Slice<MissionCompletedResponse>>> findCompletedMission(
            @PageableDefault(size = 5) Pageable pageable,
            @PathVariable Long userId
    ) {
        var result = missionService.findCompletedMissionByUserId(pageable, userId);

        return ResponseEntity.ok(ApiResponse.ok(result));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<MissionIdResponse>> createMission(
            @Valid @RequestPart MissionCreateRequest missionCreateRequest,
            @RequestPart List<MultipartFile> multipartFiles
    ) {
        var registerDateTime = LocalDateTime.now();
        var result = missionService.createMission(missionCreateRequest.toService(multipartFiles), registerDateTime);

        return ResponseEntity.created(URI.create("/api/v1/missions/" + result.id()))
                .body(ApiResponse.created(result));
    }

    @PostMapping("/{missionId}")
    public ResponseEntity<ApiResponse<MissionIdResponse>> updateMission(
            @PathVariable Long missionId,
            @Valid @RequestPart MissionUpdateRequest missionUpdateRequest,
            @RequestPart List<MultipartFile> multipartFiles
    ) {
        var modifiedDateTime = LocalDateTime.now();
        var result = missionService.updateMission(missionId, missionUpdateRequest.toService(multipartFiles), modifiedDateTime);

        return ResponseEntity.ok(ApiResponse.ok(result));
    }

    @PatchMapping("/{missionId}/extend")
    public ResponseEntity<ApiResponse<MissionIdResponse>> extendMission(
            @PathVariable Long missionId,
            @Valid @RequestBody MissionExtendRequest request
    ) {
        var modifiedDateTime = LocalDateTime.now();
        var result = missionService.extendMission(missionId, request.toService(), modifiedDateTime);

        return ResponseEntity.ok(ApiResponse.ok(result));
    }

    @PatchMapping("/{missionId}/complete")
    public ResponseEntity<ApiResponse<MissionIdResponse>> completeMission(
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
