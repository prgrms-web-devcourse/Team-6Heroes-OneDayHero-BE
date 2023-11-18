package com.sixheroes.onedayheroapplication.global.s3.dto.request;

import lombok.Builder;

@Builder
public record S3ImageDeleteServiceRequest(
        Long imageId,
        String uniqueName
) {
}
