package com.sixheroes.onedayheroapi.review.request;

import com.sixheroes.onedayheroapplication.review.reqeust.ReviewUpdateServiceRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record ReviewUpdateRequest(
        @NotBlank(message = "리뷰 내용은 필수 값이며 공백 일 수 없습니다.")
        String content,

        @NotNull(message = "별점은 필수 값 입니다.")
        Integer starScore
) {

        public ReviewUpdateServiceRequest toService() {
                return ReviewUpdateServiceRequest.builder()
                        .content(content)
                        .starScore(starScore)
                        .build();
        }
}
