package com.sixheroes.onedayheroapplication.global.s3.dto.request;


import java.io.InputStream;

public record S3ImageUploadRequest(
        InputStream inputStream,
        String originalFilename,
        String contentType,
        Long contentSize
) {
}
