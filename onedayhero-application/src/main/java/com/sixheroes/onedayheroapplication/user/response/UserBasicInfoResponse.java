package com.sixheroes.onedayheroapplication.user.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sixheroes.onedayherodomain.user.UserBasicInfo;
import com.sixheroes.onedayherodomain.user.repository.dto.UserQueryDto;
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
        UserQueryDto userQueryDto
    ) {
        return UserBasicInfoResponse.builder()
            .nickname(userQueryDto.nickname())
            .gender(userQueryDto.gender().name())
            .birth(userQueryDto.birth())
            .introduce(userQueryDto.introduce())
            .build();
    }

    public static UserBasicInfoResponse from(
        UserBasicInfo basicInfo
    ) {
        return UserBasicInfoResponse.builder()
            .nickname(basicInfo.getNickname())
            .gender(basicInfo.getGender().name())
            .birth(basicInfo.getBirth())
            .introduce(basicInfo.getIntroduce())
            .build();
    }
}