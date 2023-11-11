package com.sixheroes.onedayheroapplication.user.response;

import com.sixheroes.onedayherodomain.user.UserImage;
import lombok.Builder;

@Builder
public record UserImageResponse(
    String originalName,
    String uniqueName,
    String path
) {

    public static UserImageResponse from(
        UserImage userImage
    ) {
        return UserImageResponse.builder()
            .originalName(userImage.getOriginalName())
            .uniqueName(userImage.getUniqueName())
            .path(userImage.getPath())
            .build();
    }
}
