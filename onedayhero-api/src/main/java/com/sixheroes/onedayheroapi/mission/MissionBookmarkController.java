package com.sixheroes.onedayheroapi.mission;

import com.sixheroes.onedayheroapi.global.response.ApiResponse;
import com.sixheroes.onedayheroapi.mission.request.MissionBookmarkCreateControllerRequest;
import com.sixheroes.onedayheroapplication.mission.MissionBookmarkService;
import com.sixheroes.onedayheroapplication.mission.response.MissionBookmarkCreateResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RequiredArgsConstructor
@RequestMapping("/api/v1/bookmarks")
@RestController
public class MissionBookmarkController {

    private final MissionBookmarkService missionBookmarkService;

    @PostMapping
    public ResponseEntity<ApiResponse<MissionBookmarkCreateResponse>> createMissionBookmark(
            @Valid @RequestBody MissionBookmarkCreateControllerRequest request
    ) {
        var response = missionBookmarkService.createMissionBookmark(request.mapToApplicationRequest());

        return ResponseEntity.created(URI.create("/api/v1/missions/" + response.missionId())).body(ApiResponse.created(response));
    }
}
