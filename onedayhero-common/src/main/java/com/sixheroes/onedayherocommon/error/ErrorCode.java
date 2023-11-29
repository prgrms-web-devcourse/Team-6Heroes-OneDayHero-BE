package com.sixheroes.onedayherocommon.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

/**
 * <pre><b>code</b>는 프론트와 정한 에러 코드를 넣어준다.</pre>
 * <pre><b>message</b>는 프론트와 협의해서 정한 에러 메시지를 넣어준다.</pre>
 */
@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    // SERVER
    INTERNAL_SERVER_ERROR("S_001", "서버에서 예기치 못한 에러가 발생했습니다."),

    // COMMON
    INVALID_REQUEST_VALUE("C_001", "적절하지 못한 입력값이 들어왔습니다."),
    UNAUTHORIZED_REQUEST("C_002", "권한이 없는 요청을 수행했습니다."),

    // AUTH
    NOT_FOUNT_SOCIAL_TYPE("A_000", "존재하지 않는 소셜 로그인 타입입니다."),
    UNAUTHORIZED_TOKEN_REQUEST("A_001", "권한이 없는 요청이 들어왔습니다."),
    EXPIRED_TOKEN("A_002", "액세스 토큰이 만료되었습니다. 리프레시 토큰을 전송하여 액세스 토큰을 재발급받아야합니다."),

    // USER
    NOT_FOUND_USER("U_000", "존재하지 않는 유저입니다."),
    INVALID_USER_EMAIL("U_001", "이메일 형식이 올바르지 않습니다."),
    INVALID_USER_EMAIL_LENGTH("U_002", "이메일의 길이가 255자를 초과했습니다."),
    INVALID_USER_NICKNAME_LENGTH("U_003", "닉네임 길이가 30자를 초과했습니다."),
    INVALID_USER_INTRODUCE_LENGTH("U_004", "자기소개 길이가 200자를 초과했습니다."),
    INVALID_WORKING_TIME("U_005", "희망 근무 시간에서 시작 시간이 종료 시간보다 미래입니다."),
    INVALID_BIRTH("U_006", "태어난 날짜가 오늘보다 미래입니다."),
    INVALID_GENDER("U_007", "올바른 성별 값이 아닙니다."),
    INVALID_WEEK("U_008", "올바른 요일 값이 아닙니다."),
    HERO_MODE_OFF("U_009", "히어로 모드가 비활성화 상태입니다"),
    HERO_MODE_ON("U_010", "히어로 모드가 활성 상태입니다."),

    // IMAGE
    NOT_FOUND_IMAGE("I_000", "이미지가 존재하지 않습니다."),
    INVALID_ORIGINAL_LENGTH("I_001", "이미지 원본 이름 길이가 260자를 초과했습니다"),
    INVALID_UNIQUE_LENGTH("I_002", "이미지 고유 이름 길이가 100자를 초과했습니다."),
    INVALID_PATH_LENGTH("I_003", "이미지 경로 길이가 250자를 초과했습니다."),
    INVALID_IMAGE_FORMAT("I_004", "이미지 이름 포맷이 잘못되었습니다."),


    // MISSION
    NOT_FOUND_MISSION("M_000", "존재하지 않는 미션입니다."),
    INVALID_MISSION_CONTENT("M_001", "미션 내용의 내용은 필수 값이며 공백 일 수 없습니다."),
    INVALID_MISSION_CONTENT_LENGTH("M_002", "미션 내용의 길이는 1000자 이하로만 가능합니다."),
    INVALID_MISSION_DATE("M_003", "미션의 수행일은 등록일로부터 이전 날짜 일 수 없습니다."),
    INVALID_MISSION_END_TIME("M_004", "미션의 종료 시간이 시작 시간보다 이전 일 수 없습니다."),
    INVALID_MISSION_DEADLINE_TIME("M_005", "미션의 마감 시간이 시작시간 이후 일 수 없습니다."),
    INVALID_MISSION_PRICE("M_006", "미션의 포상금은 0 이상이어야 합니다."),
    INVALID_MISSION_OWNER("M_007", "미션의 소유자가 아닙니다."),
    ABORT_MISSION_COMPLETE("M_008", "매칭 중 상태인 미션에 대해서만 매칭완료 상태로 변경 할 수 있습니다."),
    ABORT_MISSION_CANCEL("M_009", "매칭 완료인 상태의 미션만 취소 상태로 변경 할 수 있습니다"),
    ABORT_MISSION_EXTEND("M_010", "만료된 미션만 연장이 가능합니다."),
    ABORT_MISSION_DELETE("M_011", "매칭중인 상태의 미션은 삭제가 불가능합니다."),
    INVALID_MISSION_TITLE("M_012", "미션 제목은 필수 값이며 공백 일 수 없습니다."),
    INVALID_MISSION_TITLE_LENGTH("M_013", "미션 제목의 길이는 100자 이하여야 합니다."),
    ABORT_MISSION_UPDATE("M_014", "매칭중인 상태에서만 미션의 수정이 가능합니다."),

    // MISSION MATCH
    INVALID_MATCHING_STATUS("MM_001", "매칭 설정/취소를 할 수 없는 상태입니다."),
    INVALID_MISSION_DELETE_REQUEST("MM_002", "미션의 매칭이 완료된 상태에서 미션을 바로 삭제 할 수 없습니다."),
    INVALID_MISSION_UPDATE_REQUEST("MM_003", "미션이 매칭중인 상태일 경우에만 미션을 수정 할 수 있습니다."),

    // MISSION CATEGORY
    NOT_FOUND_MISSION_CATEGORY("MC_001", "미션 카테고리가 존재하지 않습니다."),

    // MISSION BOOKMARK
    DUPLICATE_MISSION_BOOKMARK_REQUEST("MC_001", "이미 해당 미션에 찜을 한 상태입니다."),
    INVALID_STATUS_MISSION_BOOKMARK_REQUEST("MC_002", "매칭중인 미션만 찜 할 수 있습니다."),

    // MISSION PROPOSAL
    INVALID_MISSION_PROPOSAL_HERO("MP_001", "미션을 제안받은 히어로가 아닙니다."),
    INVALID_MISSION_PROPOSAL_STATUS("MP_002", "미션 제안 중인 상태가 아니라면 승낙 혹은 거절할 수 없습니다."),

    // REGION
    NOT_FOUND_REGION("R_000", "존재하지 않는 지역입니다."),

    // LOGIN
    LOGIN_FAIL("L_001", "로그인 과정에서 에러가 발생했습니다. 다시 시도해주세요"),

    // ALARM
    NOT_FOUND_ALARM("AA_000", "존재하지 않는 알람입니다."),
    INVALID_ALARM_OWNER("AA_001", "해당 알람에 접근 할 수 있는 권한이 없습니다."),
    NOT_FOUND_ALARM_TEMPLATE("AT_000", "존재하지 않는 알람 템플릿입니다."),

    // MISSION_CHATROOM
    NOT_FOUND_MISSION_CHATROOM("MCR_000", "존재하지 않는 채팅방입니다."),
    ABORT_CHATROOM_EXIT("MCR_001", "채팅방에 입장한 상태에서만 채팅방 나가기가 가능합니다."),
    INVALID_CREATE_CHAT("MCR_002", "채팅방을 생성하기 위한 적절한 인원이 들어오지 않았습니다."),
    NOT_FOUND_CHAT_TOPIC("MCR_003", "저장되어있는 레디스 채팅 토픽을 찾지 못했습니다."),

    // USER_MISSION_CHATROOM

    // CHAT
    NOT_FOUND_CHAT_TYPE("CC_000", "존재하지 않는 채팅타입입니다."),
    INVALID_MESSAGE("CC_001", "적절하지 않은 메시지가 도착하였습니다."),

    // REVIEW
    NOT_FOUND_REVIEW("R_000", "존재하지 않는 리뷰입니다."),
    INVALID_REVIEW_OWNER("R_001", "리뷰의 소유자가 아닙니다.");

    private final String code;
    private final String message;

    public static ErrorCode findByName(String name) {
        return Arrays.stream(ErrorCode.values())
                .filter((errorCode) -> errorCode.name().equals(name))
                .findAny()
                .orElse(ErrorCode.INTERNAL_SERVER_ERROR);
    }
}
