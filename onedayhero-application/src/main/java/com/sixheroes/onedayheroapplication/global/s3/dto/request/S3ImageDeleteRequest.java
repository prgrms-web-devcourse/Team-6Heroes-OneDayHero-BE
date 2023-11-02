package com.sixheroes.onedayheroapplication.global.s3.dto.request;

public record S3ImageDeleteRequest(
        Long imageId,
        String uniqueName
) {
}
