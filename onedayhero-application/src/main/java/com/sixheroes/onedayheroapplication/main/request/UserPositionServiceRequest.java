package com.sixheroes.onedayheroapplication.main.request;

import lombok.Builder;

@Builder
public record UserPositionServiceRequest(
        double longitude,
        double latitude
) {

}
