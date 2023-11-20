package com.sixheroes.onedayheroapplication.user;

import com.sixheroes.onedayherocommon.error.ErrorCode;
import com.sixheroes.onedayherodomain.user.UserImage;
import com.sixheroes.onedayherodomain.user.repository.UserImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Component
public class UserImageReader {

    private final UserImageRepository userImageRepository;

    public UserImage findOne(
        Long userImageId
    ) {
        return userImageRepository.findById(userImageId)
            .orElseThrow(() -> new NoSuchElementException(ErrorCode.T_001.name()));
    }
}
