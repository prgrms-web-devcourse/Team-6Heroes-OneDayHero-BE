package com.sixheroes.onedayherodomain.user;

import com.sixheroes.onedayherocommon.error.ErrorCode;
import com.sixheroes.onedayherocommon.exception.BusinessException;
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

    @Column(name = "nickname", length = 30, nullable = true)
    private String nickname;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", length = 10, nullable = false)
    private UserGender gender;

    @Column(name = "birth", nullable = true)
    private LocalDate birth;

    @Column(name = "introduce", length = 200, nullable = true)
    private String introduce;

    public static UserBasicInfo initStatus(
            String defaultNickname
    ) {
        return UserBasicInfo.builder()
                .nickname(defaultNickname)
                .gender(UserGender.OTHER)
                .introduce("-")
                .birth(LocalDate.of(2023, 1, 1))
                .build();
    }

    @Builder
    private UserBasicInfo(
            String nickname,
            UserGender gender,
            LocalDate birth,
            String introduce
    ) {
        validCreateUserBasicInfo(nickname, introduce, birth);

        this.nickname = nickname;
        this.gender = gender;
        this.birth = birth;
        this.introduce = introduce;
    }

    private void validCreateUserBasicInfo(
            String nickname,
            String introduce,
            LocalDate birth
    ) {
        validNicknameLength(nickname);
        validIntroduceLength(introduce);
        validBirthBeforeToday(birth);
    }

    private void validNicknameLength(String nickname) {
        if (nickname.length() > 30) {
            log.warn("nickname 길이가 30을 초과했습니다. nickname.length() : {}", nickname.length());
            throw new BusinessException(ErrorCode.INVALID_USER_NICKNAME_LENGTH);
        }
    }

    private void validIntroduceLength(String introduce) {
        if (introduce.length() > 200) {
            log.warn("introduce 길이가 200을 초과했습니다. introduce.length() : {}", introduce.length());
            throw new BusinessException(ErrorCode.INVALID_USER_INTRODUCE_LENGTH);
        }
    }

    private void validBirthBeforeToday(LocalDate birth) {
        if (!birth.isBefore(LocalDate.now())) {
            log.warn("태어난 날짜는 오늘보다 과거여야 합니다. birth : {}", birth);
            throw new BusinessException(ErrorCode.INVALID_BIRTH);
        }
    }
}
