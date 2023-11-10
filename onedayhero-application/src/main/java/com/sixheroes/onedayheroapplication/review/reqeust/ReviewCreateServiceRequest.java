package com.sixheroes.onedayheroapplication.review.reqeust;

import com.sixheroes.onedayheroapplication.global.s3.dto.request.S3ImageUploadServiceRequest;
import lombok.Builder;

import java.util.List;
import java.util.Optional;

@Builder
public record ReviewCreateServiceRequest(
        Long senderId,
        Long receiverId,
        Long missionCategoryId,
        String missionTitle,
        String content,
        Integer starScore
) {
}
