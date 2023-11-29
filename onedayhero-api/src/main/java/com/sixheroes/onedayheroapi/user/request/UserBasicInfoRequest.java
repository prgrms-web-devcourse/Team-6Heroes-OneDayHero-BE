package com.sixheroes.onedayheroapi.user.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record UserBasicInfoRequest(
    @NotBlank(message = "유저 이름은 필수이고 빈 문자여서는 안됩니다.")
    String nickname,

    @NotBlank(message = "유저 성별은 필수이고 빈 문자여서는 안됩니다.")
    String gender,

    @NotNull(message = "태어난 날짜는 필수값입니다.")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    LocalDate birth,

    String introduce
) {
}