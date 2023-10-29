package com.sixheroes.onedayheroapi.missionmatch;

import com.sixheroes.onedayheroapi.global.response.ApiResponse;
import com.sixheroes.onedayheroapi.missionmatch.request.MissionMatchCreateRequest;
import com.sixheroes.onedayheroapplication.missionmatch.MissionMatchService;
import com.sixheroes.onedayheroapplication.missionmatch.response.MissionMatchCreateResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
