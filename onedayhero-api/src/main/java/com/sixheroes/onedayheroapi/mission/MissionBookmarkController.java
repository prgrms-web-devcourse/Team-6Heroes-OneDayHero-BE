package com.sixheroes.onedayheroapi.mission;

import com.sixheroes.onedayheroapi.global.response.ApiResponse;
import com.sixheroes.onedayheroapi.mission.request.MissionBookmarkCancelRequest;
import com.sixheroes.onedayheroapi.mission.request.MissionBookmarkCreateRequest;
import com.sixheroes.onedayheroapplication.mission.MissionBookmarkService;
import com.sixheroes.onedayheroapplication.mission.response.MissionBookmarkCancelResponse;
import com.sixheroes.onedayheroapplication.mission.response.MissionBookmarkCreateResponse;
import com.sixheroes.onedayheroapplication.mission.response.MissionBookmarkMeViewResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RequiredArgsConstructor
@RequestMapping("/api/v1/bookmarks")
@RestController
public class MissionBookmarkController {

    private final MissionBookmarkService missionBookmarkService;

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<MissionBookmarkMeViewResponse>> me(
            @PageableDefault(size = 3) Pageable pageable,
            Long userId //TODO: @Auth Long userId
    ) {
        var viewResponse = missionBookmarkService.me(
                pageable,
                userId
        );
        return ResponseEntity.ok().body(ApiResponse.ok(viewResponse));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<MissionBookmarkCreateResponse>> createMissionBookmark(
            @Valid @RequestBody MissionBookmarkCreateRequest request
    ) {
        var response = missionBookmarkService.createMissionBookmark(request.toService());
        return ResponseEntity.created(URI.create("/api/v1/missions/" + response.missionId())).body(ApiResponse.created(response));
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<MissionBookmarkCancelResponse>> cancelMissionBookmark(
            @Valid @RequestBody MissionBookmarkCancelRequest request
    ) {
        var response = missionBookmarkService.cancelMissionBookmark(request.toService());
        return ResponseEntity.ok().body(ApiResponse.ok(response));
    }
}
