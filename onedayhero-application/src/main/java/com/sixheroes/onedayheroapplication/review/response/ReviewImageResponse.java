package com.sixheroes.onedayheroapplication.review.response;

import com.sixheroes.onedayherodomain.review.ReviewImage;
import lombok.Builder;

@Builder
public record ReviewImageResponse(
        Long id,
        String originalName,
        String uniqueName,
        String path

) {

    public static ReviewImageResponse from(
            ReviewImage reviewImage
    ) {
        return ReviewImageResponse.builder()
                .id(reviewImage.getId())
                .originalName(reviewImage.getOriginalName())
                .uniqueName(reviewImage.getUniqueName())
                .path(reviewImage.getPath())
                .build();
    }
}
