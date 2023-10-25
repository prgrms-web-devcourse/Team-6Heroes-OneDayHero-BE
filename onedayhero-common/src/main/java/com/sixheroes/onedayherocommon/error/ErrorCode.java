package com.sixheroes.onedayherocommon.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * <pre><b>code</b>는 프론트와 정한 에러 코드를 넣어준다.</pre>
 * <pre><b>status</b>는 값에 따라 백엔드에서 넣어준다.</pre>
 * <pre><b>message</b>는 프론트와 협의해서 정한 에러 메시지를 넣어준다.</pre>
 */
@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    T_001(400, "입력 값에서 오류가 발생했습니다."),

    S_001(500, "서버에서 알 수 없는 오류가 발생했습니다."),

    U_001(400, "유저에서 필드 값에 대한 오류가 발생했습니다.");

    private final int status;
    private final String message;
}
