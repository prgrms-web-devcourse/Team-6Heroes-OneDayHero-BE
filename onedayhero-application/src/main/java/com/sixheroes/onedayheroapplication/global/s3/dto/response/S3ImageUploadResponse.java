package com.sixheroes.onedayheroapplication.global.s3.dto.response;

public record S3ImageUploadResponse(
        String originalName,
        String uniqueName,
        String path
) {
}
