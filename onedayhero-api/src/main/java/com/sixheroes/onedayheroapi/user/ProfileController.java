package com.sixheroes.onedayheroapi.user;

import com.sixheroes.onedayheroapi.global.argumentsresolver.authuser.AuthUser;
import com.sixheroes.onedayheroapi.global.response.ApiResponse;
import com.sixheroes.onedayheroapplication.review.ReviewService;
import com.sixheroes.onedayheroapplication.review.response.ReceivedReviewViewResponse;
import com.sixheroes.onedayheroapplication.user.UserService;
import com.sixheroes.onedayheroapplication.user.response.ProfileCitizenResponse;
import com.sixheroes.onedayheroapplication.user.response.ProfileHeroResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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

    @GetMapping("/{userId}/hero")
    public ResponseEntity<ApiResponse<ProfileHeroResponse>> findProfileHero(
        @PathVariable Long userId
    ) {
        var heroProfile = userService.findHeroProfile(userId);

        return ResponseEntity.ok(ApiResponse.ok(heroProfile));
    }
}
