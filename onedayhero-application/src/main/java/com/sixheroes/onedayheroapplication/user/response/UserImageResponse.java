package com.sixheroes.onedayheroapplication.user.response;

import com.sixheroes.onedayherodomain.user.UserImage;
import lombok.Builder;

import java.util.Objects;

@Builder
public record UserImageResponse(
    String originalName,
    String uniqueName,
    String path
) {
    static final UserImageResponse EMPTY = UserImageResponse.builder()
        .build();

    public static UserImageResponse from(
        UserImage userImage
    ) {
        if (Objects.isNull(userImage)) {
            return EMPTY;
        }

        return UserImageResponse.builder()
            .originalName(userImage.getOriginalName())
            .uniqueName(userImage.getUniqueName())
            .path(userImage.getPath())
            .build();
    }

    public static UserImageResponse empty() {
        return EMPTY;
    }
}