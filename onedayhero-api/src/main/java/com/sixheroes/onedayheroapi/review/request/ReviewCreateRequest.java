package com.sixheroes.onedayheroapi.review.request;

import com.sixheroes.onedayheroapplication.review.reqeust.ReviewCreateServiceRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;


@Builder
public record ReviewCreateRequest(
        @NotNull(message = "리뷰 작성자의 아이디는 필수 값 입니다.")
        Long senderId,

        @NotNull(message = "리뷰 수신자의 아이디는 필수 값 입니다.")
        Long receiverId,

        @NotNull(message = "미션의 카테고리 아이디는 필수 값 입니다.")
        Long missionCategoryId,

        @NotBlank(message = "미션의 제목은 필수 값이며 공백 일 수 없습니다.")
        String missionTitle,

        @NotBlank(message = "리뷰 내용은 필수 값이며 공백 일 수 없습니다.")
        String content,

        @NotNull(message = "별점은 필수 값 입니다.")
        Integer starScore
) {

        public ReviewCreateServiceRequest toService() {
                return ReviewCreateServiceRequest.builder()
                        .senderId(senderId)
                        .receiverId(receiverId)
                        .missionCategoryId(missionCategoryId)
                        .missionTitle(missionTitle)
                        .content(content)
                        .starScore(starScore)
                        .build();
        }
}
