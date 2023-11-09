package com.sixheroes.onedayheroapplication.user.response;

import com.sixheroes.onedayherodomain.user.User;
import lombok.Builder;

@Builder
public record UserAuthResponse(
        Long userId,
        String userRole
) {

    public static UserAuthResponse from(User user) {
        return UserAuthResponse.builder()
                .userId(user.getId())
                .userRole(user.getUserRole().name())
                .build();
    }
}
