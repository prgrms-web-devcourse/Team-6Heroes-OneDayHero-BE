package com.sixheroes.onedayheroapi.user;

import com.sixheroes.onedayheroapi.global.response.ApiResponse;
import com.sixheroes.onedayheroapplication.user.ProfileService;
import com.sixheroes.onedayheroapplication.user.response.HeroSearchResponse;
import com.sixheroes.onedayheroapplication.user.response.ProfileCitizenResponse;
import com.sixheroes.onedayheroapplication.user.response.ProfileHeroResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@RestController
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping("/{userId}/citizen-profile")
    public ResponseEntity<ApiResponse<ProfileCitizenResponse>> findProfileCitizen(
        @PathVariable Long userId
    ) {
        var citizenProfile = profileService.findCitizenProfile(userId);

        return ResponseEntity.ok(ApiResponse.ok(citizenProfile));
    }

    @GetMapping("/{userId}/hero-profile")
    public ResponseEntity<ApiResponse<ProfileHeroResponse>> findProfileHero(
        @PathVariable Long userId
    ) {
        var heroProfile = profileService.findHeroProfile(userId);

        return ResponseEntity.ok(ApiResponse.ok(heroProfile));
    }

    @GetMapping("/hero-search")
    public ResponseEntity<ApiResponse<Slice<HeroSearchResponse>>> searchHeroes(
        @RequestParam String nickname,
        @PageableDefault Pageable pageable
    ) {
        var heroSearchResponses = profileService.searchHeroes(nickname, pageable);

        return ResponseEntity.ok(ApiResponse.ok(heroSearchResponses));
    }
}
