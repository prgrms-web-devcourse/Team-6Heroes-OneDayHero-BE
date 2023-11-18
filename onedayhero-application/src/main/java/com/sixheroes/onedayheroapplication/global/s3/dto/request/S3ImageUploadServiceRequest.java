package com.sixheroes.onedayheroapplication.global.s3.dto.request;


import lombok.Builder;

import java.io.InputStream;

@Builder
public record S3ImageUploadServiceRequest(
        InputStream inputStream,
        String originalName,
        String contentType,
        Long contentSize
) {
}
