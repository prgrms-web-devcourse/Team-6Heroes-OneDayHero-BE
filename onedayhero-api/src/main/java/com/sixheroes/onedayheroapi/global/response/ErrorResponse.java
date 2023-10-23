package com.sixheroes.onedayheroapi.global.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;

import java.util.List;

/**
 * @param code    는 프론트와 정한 ErrorCode를 의미한다.
 * @param status  는 HttpStatusCode의 value를 의미한다. <b>EX) 200</b>
 * @param message 는 Error에 대한 메시지를 의미한다.
 * @param errors  는 BindException에서 발생 할 수 있는 입력 에러에 해당한다.
 */
@Builder
public record ErrorResponse(
        String code,

        int status,

        String message,

        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        List<ValidationError> errors
) {

    public static ErrorResponse from(final ErrorCode errorCode) {
        return ErrorResponse.builder()
                .code(errorCode.name())
                .status(errorCode.getStatus())
                .message(errorCode.getMessage())
                .build();
    }

    public static ErrorResponse from(
            final ErrorCode errorCode,
            final BindException e
    ) {
        return ErrorResponse.builder()
                .code(errorCode.name())
                .status(errorCode.getStatus())
                .message(errorCode.getMessage())
                .errors(ValidationError.of(e.getBindingResult().getFieldErrors()))
                .build();
    }

    @Builder
    public record ValidationError(
            String field,
            String message
    ) {
        private static ValidationError from(final FieldError fieldError) {
            return ValidationError.builder()
                    .field(fieldError.getField())
                    .message(fieldError.getDefaultMessage())
                    .build();
        }

        private static List<ValidationError> of(final List<FieldError> fieldErrors) {
            return fieldErrors.stream()
                    .map(ValidationError::from)
                    .toList();
        }
    }
}
