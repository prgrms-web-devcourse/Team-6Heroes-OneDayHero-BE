package com.sixheroes.onedayheroapplication.user.response;

import com.sixheroes.onedayherodomain.user.User;
import lombok.Builder;

@Builder
public record UserAuthResponse(
        Boolean signUp,
        Long userId,
        String userRole
) {

    public static UserAuthResponse loginResponse(User user) {
        return UserAuthResponse.builder()
                .signUp(false)
                .userId(user.getId())
                .userRole(user.getUserRole().name())
                .build();
    }

    public static UserAuthResponse signUpResponse(User user) {
        return UserAuthResponse.builder()
                .signUp(true)
                .userId(user.getId())
                .userRole(user.getUserRole().name())
                .build();
    }
}
