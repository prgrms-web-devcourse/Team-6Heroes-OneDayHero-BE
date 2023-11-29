package com.sixheroes.onedayheroapi.mission;

import com.sixheroes.onedayheroapi.global.argumentsresolver.authuser.AuthUser;
import com.sixheroes.onedayheroapi.global.response.ApiResponse;
import com.sixheroes.onedayheroapplication.mission.MissionImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/mission-images")
@RestController
public class MissionImageController {

    private final MissionImageService missionImageService;

    @DeleteMapping("/{missionImageId}")
    public ResponseEntity<ApiResponse<Void>> deleteMissionImage(
            @PathVariable Long missionImageId,
            @AuthUser Long userId
    ) {
        missionImageService.deleteImage(missionImageId, userId);
        return new ResponseEntity<>(ApiResponse.noContent(), HttpStatus.NO_CONTENT);
    }
}
