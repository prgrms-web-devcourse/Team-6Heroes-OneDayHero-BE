package com.sixheroes.onedayheroapplication.review.reqeust;

import lombok.Builder;

@Builder
public record ReviewUpdateServiceRequest(
        String content,
        Integer starScore
){
}
