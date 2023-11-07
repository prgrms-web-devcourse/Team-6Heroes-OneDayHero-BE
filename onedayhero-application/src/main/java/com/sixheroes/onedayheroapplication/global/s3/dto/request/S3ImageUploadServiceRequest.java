package com.sixheroes.onedayheroapplication.global.s3.dto.request;


import java.io.InputStream;

public record S3ImageUploadServiceRequest(
        InputStream inputStream,
        String originalName,
        String contentType,
        Long contentSize
) {
}
