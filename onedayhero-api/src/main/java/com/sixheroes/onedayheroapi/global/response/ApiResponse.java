package com.sixheroes.onedayheroapi.global.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public record ApiResponse<T>(
        int status,

        T data,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
        LocalDateTime serverDateTime
) {

    public ApiResponse<T> ok(T data) {
        return new ApiResponse<>(HttpStatus.OK.value(), data, LocalDateTime.now());
    }

    public ApiResponse<T> created(T data) {
        return new ApiResponse<>(HttpStatus.CREATED.value(), data, LocalDateTime.now());
    }

    public ApiResponse<T> noContent(T data) {
        return new ApiResponse<>(HttpStatus.NO_CONTENT.value(), data, LocalDateTime.now());
    }
}