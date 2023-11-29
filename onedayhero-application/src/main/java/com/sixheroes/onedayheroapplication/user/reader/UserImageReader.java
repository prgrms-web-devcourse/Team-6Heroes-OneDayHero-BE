package com.sixheroes.onedayheroapplication.user.reader;

import com.sixheroes.onedayherocommon.error.ErrorCode;
import com.sixheroes.onedayherocommon.exception.EntityNotFoundException;
import com.sixheroes.onedayherodomain.user.UserImage;
import com.sixheroes.onedayherodomain.user.repository.UserImageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class UserImageReader {

    private final UserImageRepository userImageRepository;

    public UserImage findOne(
            Long userImageId
    ) {
        return userImageRepository.findById(userImageId)
                .orElseThrow(() -> {
                    log.warn("유저 이미지를 찾지 못하였습니다. userImageId : {}", userImageId);
                    return new EntityNotFoundException(ErrorCode.NOT_FOUND_IMAGE);
                });
    }
}
