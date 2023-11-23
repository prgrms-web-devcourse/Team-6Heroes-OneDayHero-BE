package com.sixheroes.onedayheroapplication.user.response;

import com.sixheroes.onedayherodomain.mission.repository.dto.MissionCategoryDto;
import com.sixheroes.onedayherodomain.user.User;
import com.sixheroes.onedayherodomain.user.UserBasicInfo;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Builder
public record HeroSearchResponse(
    Long id,
    String nickname,
    UserImageResponse image,
    List<MissionCategoryResponse> favoriteMissionCategories,
    Integer heroScore
){

    public static HeroSearchResponse of(
        User user,
        List<MissionCategoryDto> missionCategories
    ) {
        var missionCategoryResponses = Optional.ofNullable(missionCategories)
            .orElseGet(ArrayList::new)
            .stream()
            .map(MissionCategoryResponse::from)
            .toList();

        var userImageResponse = user.getUserImages().stream()
            .map(UserImageResponse::from)
            .findFirst()
            .orElseGet(UserImageResponse::empty);

        var nickname = Optional.of(user.getUserBasicInfo())
            .map(UserBasicInfo::getNickname)
            .orElse(null);

        return HeroSearchResponse.builder()
            .id(user.getId())
            .nickname(nickname)
            .image(userImageResponse)
            .heroScore(user.getHeroScore())
            .favoriteMissionCategories(missionCategoryResponses)
            .build();
    }

    public record MissionCategoryResponse(
        String code,
        String name
    ) {

        static MissionCategoryResponse from(
            MissionCategoryDto missionCategoryDto
        ) {
            return new MissionCategoryResponse(
                missionCategoryDto.missionCategoryCode().name(),
                missionCategoryDto.name()
            );
        }
    }
}
