package com.sixheroes.onedayheroapplication.user.response;

import com.sixheroes.onedayherodomain.user.User;

public record UserUpdateResponse(
    Long id
) {

    public static UserUpdateResponse from(
        User user
    ) {
        return new UserUpdateResponse(user.getId());
    }
}
