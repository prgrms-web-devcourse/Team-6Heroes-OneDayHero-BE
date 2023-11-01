package com.sixheroes.onedayherodomain.user;

import com.sixheroes.onedayherocommon.error.ErrorCode;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.regex.Pattern;

@Slf4j
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class Email {

    private static final Pattern EMAIL_REGEX = Pattern.compile("^[a-zA-Z0-9+-_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$");

    @Column(name = "email", length = 255, nullable = false)
    private String email;

    @Builder
    private Email(
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
            throw new IllegalArgumentException(ErrorCode.EU_002.name());
        }
    }

    private void validEmailRegex(
        String email
    ) {
        var matcher = EMAIL_REGEX.matcher(email);
        if (!matcher.matches()) {
            log.debug("email 형식이 올바르지 않습니다.");
            throw new IllegalArgumentException(ErrorCode.EU_001.name());
        }
    }
}