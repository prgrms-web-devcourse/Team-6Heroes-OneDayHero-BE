package com.sixheroes.onedayheroapi.user;

import com.sixheroes.onedayheroapi.global.response.ApiResponse;
import com.sixheroes.onedayheroapplication.user.UserService;
import com.sixheroes.onedayheroapplication.user.response.ProfileCitizenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@RestController
public class ProfileController {

    private final UserService userService;

    @GetMapping("/{userId}/citizen")
    public ResponseEntity<ApiResponse<ProfileCitizenResponse>> findProfileCitizen(
        @PathVariable Long userId
    ) {
        var citizenProfile = userService.findCitizenProfile(userId);

        return ResponseEntity.ok(ApiResponse.ok(citizenProfile));
    }
}