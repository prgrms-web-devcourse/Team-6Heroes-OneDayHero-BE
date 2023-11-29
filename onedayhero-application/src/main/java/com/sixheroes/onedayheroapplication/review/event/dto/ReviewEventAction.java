package com.sixheroes.onedayheroapplication.review.event.dto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ReviewEventAction {
    REVIEW_CREATE("리뷰 생성");

    private final String description;
}
