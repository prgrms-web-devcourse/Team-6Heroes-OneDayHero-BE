package com.sixheroes.onedayheroapi.user.request;

import jakarta.validation.constraints.NotBlank;

public record HeroRankRequest(
    @NotBlank(message = "지역은 필수입니다.")
    String region,

    String missionCategoryCode
) {
}
