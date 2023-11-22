package com.sixheroes.onedayheroapplication.user.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sixheroes.onedayherodomain.user.User;
import com.sixheroes.onedayherodomain.user.UserBasicInfo;
import com.sixheroes.onedayherodomain.user.UserGender;
import lombok.Builder;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

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
        static final UserBasicInfoForProfileCitizenResponse EMPTY = UserBasicInfoForProfileCitizenResponse.builder()
            .build();

        public static UserBasicInfoForProfileCitizenResponse from(
            UserBasicInfo userBasicInfo
        ) {
            if (Objects.isNull(userBasicInfo)) {
                return EMPTY;
            }

            var userGender = Optional.of(userBasicInfo.getGender())
                .map(UserGender::name)
                .orElse(null);

            return UserBasicInfoForProfileCitizenResponse.builder()
                .nickname(userBasicInfo.getNickname())
                .gender(userGender)
                .birth(userBasicInfo.getBirth())
                .build();
        }
    }
}
