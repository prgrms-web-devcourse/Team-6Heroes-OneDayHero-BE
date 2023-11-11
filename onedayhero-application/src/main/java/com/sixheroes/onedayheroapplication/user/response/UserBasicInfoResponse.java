package com.sixheroes.onedayheroapplication.user.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sixheroes.onedayherodomain.user.UserBasicInfo;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record UserBasicInfoResponse(
    String nickname,

    String gender,

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    LocalDate birth,

    String introduce
) {

    public static UserBasicInfoResponse from(
        UserBasicInfo userBasicInfo
    ) {
        return UserBasicInfoResponse.builder()
            .nickname(userBasicInfo.getNickname())
            .gender(userBasicInfo.getGender().name())
            .birth(userBasicInfo.getBirth())
            .introduce(userBasicInfo.getIntroduce())
            .build();
    }
}