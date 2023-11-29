package com.sixheroes.onedayherodomain.user;

import com.sixheroes.onedayherocommon.exception.BusinessException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserBasicInfoTest {

    @DisplayName("닉네임 길이가 30자 이하면 유저 기본 정보가 생성된다.")
    @Test
    void validNicknameLength() {
        // given
        var random = new Random();
        String nickname = random.ints('a', 'z' + 1)
                .limit(30)
                .collect(
                        StringBuilder::new,
                        StringBuilder::appendCodePoint,
                        StringBuilder::append
                ).toString();

        // when
        var userBasicInfo = createUserBasicInfoWithNickname(nickname);

        // then
        assertThat(userBasicInfo.getNickname()).isEqualTo(nickname);
    }

    @DisplayName("닉네임 길이가 30자를 초과하면 예외가 발생한다.")
    @Test
    void invalidOutofRangeNicknameLength() {
        // given
        var random = new Random();
        var nickname = random.ints('a', 'z' + 1)
                .limit(31)
                .collect(
                        StringBuilder::new,
                        StringBuilder::appendCodePoint,
                        StringBuilder::append
                ).toString();

        // when & then
        assertThatThrownBy(() -> createUserBasicInfoWithNickname(nickname))
                .isInstanceOf(BusinessException.class);
    }

    @DisplayName("자기소개 길이가 200자 이하면 유저 기본 정보가 생성된다.")
    @Test
    void validIntroduceLength() {
        // given
        var random = new Random();
        var introduce = random.ints('a', 'z' + 1)
                .limit(200)
                .collect(
                        StringBuilder::new,
                        StringBuilder::appendCodePoint,
                        StringBuilder::append
                ).toString();

        // when
        var userBasicInfo = createUserBasicInfoWithIntroduce(introduce);

        // then
        assertThat(userBasicInfo.getIntroduce()).isEqualTo(introduce);
    }

    @DisplayName("자기소개 길이가 200자 초과하면 예외가 발생한다.")
    @Test
    void invalidOutOfRangeIntroduceLength() {
        // given
        var random = new Random();
        var introduce = random.ints('a', 'z' + 1)
                .limit(201)
                .collect(
                        StringBuilder::new,
                        StringBuilder::appendCodePoint,
                        StringBuilder::append
                ).toString();

        // when & then
        assertThatThrownBy(() -> createUserBasicInfoWithIntroduce(introduce))
                .isInstanceOf(BusinessException.class);
    }

    @DisplayName("태어난 날짜가 오늘보다 과거면 유저 기본 정보가 생성된다.")
    @Test
    void validBirthBeforeToday() {
        // given
        var birth = LocalDate.now().minusDays(1);

        // when
        var userBasicInfo = createUserBasicInfoWithBirth(birth);

        // then
        assertThat(userBasicInfo.getBirth()).isEqualTo(birth);
    }

    @DisplayName("태어난 날짜가 오늘이거나 오늘보다 미래라면 예외가 발생한다.")
    @Test
    void invalidBirthSameAndAfterToday() {
        // given
        var birth = LocalDate.now();

        // when & then
        assertThatThrownBy(() -> createUserBasicInfoWithBirth(birth))
                .isInstanceOf(BusinessException.class);
    }

    private UserBasicInfo createUserBasicInfoWithNickname(
            String nickname
    ) {
        return UserBasicInfo.builder()
                .nickname(nickname)
                .birth(LocalDate.of(1990, 1, 1))
                .gender(UserGender.MALE)
                .introduce("자기소개")
                .build();
    }

    private UserBasicInfo createUserBasicInfoWithIntroduce(
            String introduce
    ) {
        return UserBasicInfo.builder()
                .nickname("이름")
                .birth(LocalDate.of(1990, 1, 1))
                .gender(UserGender.MALE)
                .introduce(introduce)
                .build();
    }

    private UserBasicInfo createUserBasicInfoWithBirth(
            LocalDate birth
    ) {
        return UserBasicInfo.builder()
                .nickname("이름")
                .birth(birth)
                .gender(UserGender.MALE)
                .introduce("자기소개")
                .build();
    }
}