package com.sixheroes.onedayheroapi.main.request;

import com.sixheroes.onedayheroapplication.main.request.UserPositionServiceRequest;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record UserPositionRequest(
        @NotNull(message = "경도는 필수 값 입니다.")
        double longitude,

        @NotNull(message = "위도는 필수 값 입니다.")
        double latitude
) {

    public UserPositionServiceRequest toService() {
        return UserPositionServiceRequest.builder()
                .longitude(longitude)
                .latitude(latitude)
                .build();
    }
}
