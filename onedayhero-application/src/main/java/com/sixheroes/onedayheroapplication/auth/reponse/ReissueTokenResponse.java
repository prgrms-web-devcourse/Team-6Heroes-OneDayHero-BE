package com.sixheroes.onedayheroapplication.auth.reponse;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;

@Builder
public record ReissueTokenResponse(
        Boolean reissued,

        String accessToken,

        @JsonIgnore
        String refreshToken
) {

    public static ReissueTokenResponse success(
            String accessToken,
            String refreshToken
    ) {
        return ReissueTokenResponse.builder()
                .reissued(true)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public static ReissueTokenResponse fail() {
        return ReissueTokenResponse.builder()
                .reissued(false)
                .accessToken("")
                .refreshToken("")
                .build();
    }
}
