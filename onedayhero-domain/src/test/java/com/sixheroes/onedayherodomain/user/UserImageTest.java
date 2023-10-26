package com.sixheroes.onedayherodomain.user;

import com.sixheroes.onedayherocommon.error.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.assertj.core.api.Assertions.*;

class UserImageTest {

    @DisplayName("원본 이름의 길이가 260자 이하면 유저 이미지가 생성된다.")
    @Test
    void validOriginalNameLengthTest() {
        // given
        var random = new Random();
        var originalName = random.ints('a', 'z' + 1)
            .limit(260)
            .collect(
                StringBuilder::new,
                StringBuilder::appendCodePoint,
                StringBuilder::append
            ).toString();

        // when
        var userImage = createUserImageWithOrignalName(originalName);

        // then
        assertThat(userImage.getOriginalName()).isEqualTo(originalName);
    }

    @DisplayName("이미지 원본 이름의 길이가 260자 초과하면 예외가 발생한다.")
    @Test
    void validOriginalNameLengthExTest() {
        // given
        var random = new Random();
        var originalName = random.ints('a', 'z' + 1)
            .limit(261)
            .collect(
                StringBuilder::new,
                StringBuilder::appendCodePoint,
                StringBuilder::append
            ).toString();

        // when & then
        assertThatThrownBy(() -> createUserImageWithOrignalName(originalName))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(ErrorCode.EI_001.name());
    }

    @DisplayName("고유 이름의 길이가 100자 이하면 유저 이미지가 생성된다.")
    @Test
    void validUniqueNameLengthTest() {
        // given
        var random = new Random();
        var uniqueName = random.ints('a', 'z' + 1)
            .limit(100)
            .collect(
                StringBuilder::new,
                StringBuilder::appendCodePoint,
                StringBuilder::append
            ).toString();

        // when
        var userImage = createUserImageWithUniqueName(uniqueName);

        // then
        assertThat(userImage.getUniqueName()).isEqualTo(uniqueName);
    }

    @DisplayName("고유 이름의 길이가 100자 초과하면 예외가 발생한다.")
    @Test
    void validUniqueNameLengthExTest() {
        // given
        var random = new Random();
        var uniqueName = random.ints('a', 'z' + 1)
            .limit(101)
            .collect(
                StringBuilder::new,
                StringBuilder::appendCodePoint,
                StringBuilder::append
            ).toString();

        // when & then
        assertThatThrownBy(() -> createUserImageWithUniqueName(uniqueName))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(ErrorCode.EI_002.name());
    }

    @DisplayName("경로 이름의 길이가 250자 이하면 유저 이미지가 생성된다.")
    @Test
    void validPathLengthTest() {
        // given
        var random = new Random();
        var path = random.ints('a', 'z' + 1)
            .limit(250)
            .collect(
                StringBuilder::new,
                StringBuilder::appendCodePoint,
                StringBuilder::append
            ).toString();

        // when
        var userImage = createUserImageWithPath(path);

        // then
        assertThat(userImage.getPath()).isEqualTo(path);
    }

    @DisplayName("경로 이름의 길이가 250자 초과하면 예외가 발생한다.")
    @Test
    void validPathLengthExTest() {
        // given
        var random = new Random();
        var path = random.ints('a', 'z' + 1)
            .limit(251)
            .collect(
                StringBuilder::new,
                StringBuilder::appendCodePoint,
                StringBuilder::append
            ).toString();

        // when & then
        assertThatThrownBy(() -> createUserImageWithPath(path))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(ErrorCode.EI_003.name());
    }

    private UserImage createUserImageWithOrignalName(
        String originalName
    ) {
        return UserImage.builder()
            .originalName(originalName)
            .uniqueName("고유 이름")
            .path("경로")
            .build();
    }

    private UserImage createUserImageWithUniqueName(
        String uniqueName
    ) {
        return UserImage.builder()
            .originalName("원본 이름")
            .uniqueName(uniqueName)
            .path("경로")
            .build();
    }

    private UserImage createUserImageWithPath(
        String path
    ) {
        return UserImage.builder()
            .originalName("원본 이름")
            .uniqueName("고유 이름")
            .path(path)
            .build();
    }
}