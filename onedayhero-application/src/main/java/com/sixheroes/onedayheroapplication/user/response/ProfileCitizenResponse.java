package com.sixheroes.onedayheroapplication.user.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sixheroes.onedayherodomain.user.User;
import com.sixheroes.onedayherodomain.user.UserBasicInfo;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record ProfileCitizenResponse(
    UserBasicInfoForProfileCitizenResponse basicInfo,
    UserImageResponse image,
    Integer heroScore
) {

    public static ProfileCitizenResponse from(
        User user
    ) {
        var userBasicInfoResponse = UserBasicInfoForProfileCitizenResponse.from(user.getUserBasicInfo());
        var userImageResponse = user.getUserImages().stream()
            .map(UserImageResponse::from)
            .findFirst()
            .orElseGet(UserImageResponse::empty);

        return ProfileCitizenResponse.builder()
            .basicInfo(userBasicInfoResponse)
            .image(userImageResponse)
            .heroScore(user.getHeroScore())
            .build();
    }

    @Builder
    public
    record UserBasicInfoForProfileCitizenResponse(
        String nickname,

        String gender,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
        LocalDate birth
    ) {

        public static UserBasicInfoForProfileCitizenResponse from(
            UserBasicInfo userBasicInfo
        ) {
            return UserBasicInfoForProfileCitizenResponse.builder()
                .nickname(userBasicInfo.getNickname())
                .gender(userBasicInfo.getGender().name())
                .birth(userBasicInfo.getBirth())
                .build();
        }
    }
}
