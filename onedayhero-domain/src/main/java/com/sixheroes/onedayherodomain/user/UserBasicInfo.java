package com.sixheroes.onedayherodomain.user;

import com.sixheroes.onedayherocommon.error.ErrorCode;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;

@Slf4j
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class UserBasicInfo {

    @Column(name = "nickname", length = 30, nullable = false)
    private String nickname;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", length = 10, nullable = false)
    private UserGender gender;

    @Column(name = "birth", nullable = false)
    private LocalDate birth;

    @Column(name = "introduce", length = 200, nullable = true)
    private String introduce;

    @Builder
    private UserBasicInfo(
        String nickname,
        UserGender gender,
        LocalDate birth,
        String introduce
    ) {
        validCreateUserBasicInfo(nickname, introduce);

        this.nickname = nickname;
        this.gender = gender;
        this.birth = birth;
        this.introduce = introduce;
    }

    private void validCreateUserBasicInfo(
        String nickname,
        String introduce
    ) {
        validNicknameLength(nickname);
        validIntroduceLength(introduce);
    }

    private void validNicknameLength(String nickname) {
        if (nickname.length() > 30) {
            log.debug("nickname 길이가 30을 초과했습니다. nickname.length() : {}", nickname.length());
            throw new IllegalArgumentException(ErrorCode.EU_003.name());
        }
    }

    private void validIntroduceLength(String introduce) {
        if (introduce.length() > 200) {
            log.debug("introduce 길이가 200을 초과했습니다. introduce.length() : {}", introduce.length());
            throw new IllegalArgumentException(ErrorCode.EU_004.name());
        }
    }
}
