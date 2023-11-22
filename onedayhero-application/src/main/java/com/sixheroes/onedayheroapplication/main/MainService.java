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
        throw new UnsupportedOperationException();
    }
}
