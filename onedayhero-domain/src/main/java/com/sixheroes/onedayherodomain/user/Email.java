package com.sixheroes.onedayherodomain.user;

import com.sixheroes.onedayherocommon.error.ErrorCode;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class Email {

    private static final String EMAIL_REGEX = "\b[\\w\\.-]+@[\\w\\.-]+\\.\\w{2,4}\b";

    @Column(name = "email", length = 255, nullable = false)
    private String email;

    @Builder
    public Email(
        String email
    ) {
        validCreateEmail(email);

        this.email = email;
    }

    private void validCreateEmail(
        String email
    ) {
        validEmailLength(email);
        validEmailRegex(email);
    }
    private void validEmailLength(
        String email
    ) {
        if (email.length() > 255) {
            log.debug("email 길이가 255자를 초과했습니다. email.length() : {}", email.length());
            throw new IllegalArgumentException(ErrorCode.U_001.name());
        }
    }

    private void validEmailRegex(
        String email
    ) {
        if (!email.matches(EMAIL_REGEX)) {
            log.debug("email 형식이 올바르지 않습니다.");
            throw new IllegalArgumentException(ErrorCode.U_001.name());
        }
    }
}