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

    EU_001(400, "이메일 형식이 올바르지 않습니다."),
    EU_002(400, "이메일의 길이가 255자를 초과했습니다."),
    EU_003(400, "닉네임 길이가 30자를 초과했습니다."),
    EU_004(400, "자기소개 길이가 200자를 초과했습니다."),
    EU_005(400, "희망 근무 시간에서 시작 시간이 종료 시간보다 미래입니다."),

    EI_001(400, "이미지 원본 이름 길이가 260자를 초과했습니다"),
    EI_002(400, "이미지 고유 이름 길이기 100자를 초과했습니다."),
    EI_003(400, "이미지 경로 길이가 250자를 초과했습니다.");

    private final int status;
    private final String message;
}
