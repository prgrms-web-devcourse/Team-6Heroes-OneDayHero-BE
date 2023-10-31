package com.sixheroes.onedayherocommon.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

/**
 * <pre><b>code</b>는 프론트와 정한 에러 코드를 넣어준다.</pre>
 * <pre><b>status</b>는 값에 따라 백엔드에서 넣어준다.</pre>
 * <pre><b>message</b>는 프론트와 협의해서 정한 에러 메시지를 넣어준다.</pre>
 */
@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    // User Service
    EUC_000(400, "존재하지 않는 유저입니다."),

    // Mission 도메인
    EM_001(400, "미션 내용의 내용은 필수 값이며 공백 일 수 없습니다."),
    EM_002(400, "미션 내용의 길이는 1000자 이하로만 가능합니다."),
    EM_003(400, "미션의 수행일은 등록일로부터 이전 날짜 일 수 없습니다."),
    EM_004(400, "미션의 종료 시간이 시작시간보다 이전 일 수 없습니다."),
    EM_005(400, "미션의 마감 시간이 시작시간 이후 일 수 없습니다."),
    EM_006(400, "미션의 포상금은 0 이상이어야 합니다."),

    // Mission Service
    EMC_000(400, "존재하지 않는 미션입니다."),
    EMC_001(400, "존재하지 않는 미션 카테고리 아이디를 요청하였습니다."),
    EMC_002(400, "매칭중인 미션만 찜 할 수 있습니다."),

    // User 도메인
    EU_001(400, "이메일 형식이 올바르지 않습니다."),
    EU_002(400, "이메일의 길이가 255자를 초과했습니다."),
    EU_003(400, "닉네임 길이가 30자를 초과했습니다."),
    EU_004(400, "자기소개 길이가 200자를 초과했습니다."),
    EU_005(400, "희망 근무 시간에서 시작 시간이 종료 시간보다 미래입니다."),
    EU_006(400, "태어난 날짜가 오늘보다 미래입니다."),
    EU_007(400, "올바른 성별 값이 아닙니다."),
    EU_008(400, "올바른 요일 값이 아닙니다."),

    // UserImage 도메인
    EI_001(400, "이미지 원본 이름 길이가 260자를 초과했습니다"),
    EI_002(400, "이미지 고유 이름 길이기 100자를 초과했습니다."),
    EI_003(400, "이미지 경로 길이가 250자를 초과했습니다."),

    // User Service
    EUC_001(400, "존재하지 않는 유저 아이디입니다."),


    // COMMON
    // TYPE_001
    T_001(400, "입력 값에서 오류가 발생했습니다."),
    // SERVER_001
    S_001(500, "서버에서 알 수 없는 오류가 발생했습니다."),
    // Authorization_001
    A_001(403, "권한이 없는 요청이 들어왔습니다.");

    private final int status;
    private final String message;

    public static Optional<ErrorCode> findByName(String name) {
        return Optional.of(ErrorCode.valueOf(name));
    }
}
