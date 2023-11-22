package com.sixheroes.onedayheroapplication.main;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MainService {

    public MainResponse findMainResponse(
            Long userId
    ) {
        // 손님 인지 여부에 따라서 다른 값을 반환해야 함.
        throw new UnsupportedOperationException();
    }
}
