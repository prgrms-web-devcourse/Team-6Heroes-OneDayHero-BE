package com.sixheroes.onedayheroapplication.user.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sixheroes.onedayherodomain.user.UserBasicInfo;
import com.sixheroes.onedayherodomain.user.UserGender;
import lombok.Builder;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

@Builder
public record UserBasicInfoResponse(
    String nickname,

    String gender,

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    LocalDate birth,

    String introduce
) {
    static final UserBasicInfoResponse EMPTY = UserBasicInfoResponse.builder()
                                                                .build();

    public static UserBasicInfoResponse from(
        UserBasicInfo basicInfo
    ) {
        if (Objects.isNull(basicInfo)) {
            return EMPTY;
        }

        return UserBasicInfoResponse.builder()
            .nickname(basicInfo.getNickname())
            .gender(Optional.of(basicInfo.getGender()).map(UserGender::name).orElse(null))
            .birth(basicInfo.getBirth())
            .introduce(basicInfo.getIntroduce())
            .build();
    }
}