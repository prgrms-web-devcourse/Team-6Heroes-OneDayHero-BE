package com.sixheroes.onedayheroapi.user.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record UserBasicInfoDto(
    @NotBlank(message = "유저 이름이 빈 문자열입니다.")
    String nickname,

    @NotBlank(message = "유저 성별이 빈 문자열 입니다.")
    String gender,

    @NotNull(message = "태어난 날짜가 null입니다.")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    LocalDate birth,

    String introduce
) {
}