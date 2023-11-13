package com.sixheroes.onedayheroapplication.user.response;

import com.sixheroes.onedayherodomain.user.repository.dto.UserQueryDto;
import lombok.Builder;

@Builder
public record UserImageResponse(
    String originalName,
    String uniqueName,
    String path
) {

    public static UserImageResponse from(
        UserQueryDto userQueryDto
    ) {
        return UserImageResponse.builder()
            .originalName(userQueryDto.originalName())
            .uniqueName(userQueryDto.uniqueName())
            .path(userQueryDto.path())
            .build();
    }
}