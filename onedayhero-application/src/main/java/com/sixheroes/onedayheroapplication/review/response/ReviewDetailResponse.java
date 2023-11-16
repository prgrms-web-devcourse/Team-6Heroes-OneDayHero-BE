package com.sixheroes.onedayheroapplication.review.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mysql.cj.x.protobuf.MysqlxExpr;
import com.sixheroes.onedayheroapplication.mission.response.MissionCategoryResponse;
import com.sixheroes.onedayheroapplication.review.repository.response.ReviewDetailQueryResponse;
import com.sixheroes.onedayherodomain.mission.MissionCategoryCode;
import com.sixheroes.onedayherodomain.review.ReviewImage;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Builder
public record ReviewDetailResponse(
        Long id,

        Long senderId,

        String senderNickname,

        Long receiverId,

        Long categoryId,

        String categoryCode,

        String categoryName,

        String missionTitle,

        String content,

        Integer starScore,

        List<ReviewImageResponse> reviewImageResponses,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
        LocalDateTime createdAt
) {
        private static Function<Optional<List<ReviewImage>>, List<ReviewImageResponse>> responseMapper = reviewImages ->
             reviewImages.map(images ->
                             images
                                     .stream()
                                     .map(ReviewImageResponse::from)
                                     .toList()
                     ).orElse(Collections.emptyList()
             );

        public static ReviewDetailResponse of(
                ReviewDetailQueryResponse queryResponse,
                Optional<List<ReviewImage>> reviewImages
        ) {
                return ReviewDetailResponse.builder()
                        .id(queryResponse.id())
                        .senderId(queryResponse.senderId())
                        .senderNickname(queryResponse.senderNickname())
                        .receiverId(queryResponse.receiverId())
                        .categoryId(queryResponse.categoryId())
                        .categoryCode(queryResponse.categoryCode().name())
                        .categoryName(queryResponse.categoryName())
                        .missionTitle(queryResponse.missionTitle())
                        .content(queryResponse.content())
                        .starScore(queryResponse.starScore())
                        .createdAt(queryResponse.createdAt())
                        .reviewImageResponses(responseMapper.apply(reviewImages))
                        .build();
        }

}
