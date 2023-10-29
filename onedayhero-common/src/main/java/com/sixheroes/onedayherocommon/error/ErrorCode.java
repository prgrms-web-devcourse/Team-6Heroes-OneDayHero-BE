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
//    EM_007(400, "매칭 중 상태인 미션에 대해서만 매칭완료 설정을 할 수 있습니다."),
//    EM_008(400, "자신이 작성한 미션에 대해서만 매칭완료 설정을 할 수 있습니다."),
//    EM_009(400, "매칭 완료인 상태의 미션만 포기/철회 상태로 설정할 수 있습니다"),

    // Mission Match
    EMM_001(400, "매칭된 상태의 미션만 취소할 수 있습니다."),
    EMM_002(400, "히어로는 본인의 미션매칭에 대해서만 포기할 수 있습니다."),
    EM_007(400, "미션의 매칭이 완료된 상태에서 미션을 바로 삭제 할 수 없습니다."),
    EM_008(400, "존재하지 않는 미션 아이디를 입력받았습니다."),
    EM_009(400, "미션이 매칭중인 상태일 경우에만 미션을 수정 할 수 있습니다."),
    EM_100(403, "미션을 수정 할 권한이 없습니다."),

    // Mission Category
    EMC_000(400, "존재하지 않는 미션입니다."),
    EMC_001(400, "존재하지 않는 미션 카테고리 아이디를 요청하였습니다."),
    EMC_002(400, "매칭중인 미션만 찜 할 수 있습니다."),

    // Mission Match Service
    EMMC_000(400, "존재하지 않는 미션매칭 입니다."),

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
