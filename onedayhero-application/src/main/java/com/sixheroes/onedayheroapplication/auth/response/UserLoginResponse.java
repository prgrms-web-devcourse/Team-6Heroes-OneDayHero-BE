package com.sixheroes.onedayheroapplication.auth.response;

import com.sixheroes.onedayherodomain.user.User;
import lombok.Builder;

@Builder
public record UserLoginResponse(
        Long userId,
        String userRole
) {

    public static UserLoginResponse from(User user) {
        return UserLoginResponse.builder()
                .userId(user.getId())
                .userRole(user.getUserRole().name())
                .build();
    }
}
