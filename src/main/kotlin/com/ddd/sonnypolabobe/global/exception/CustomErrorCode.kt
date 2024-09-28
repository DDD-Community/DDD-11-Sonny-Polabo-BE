package com.ddd.sonnypolabobe.global.exception

import org.springframework.http.HttpStatus

enum class CustomErrorCode(
    val status: HttpStatus,
    val code: String,
    val message: String
) {
    INVALID_VALUE_EXCEPTION(HttpStatus.BAD_REQUEST, "COM001", "잘못된 값입니다."),
    INTERNAL_SERVER_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "COM002", "서버 내부 오류입니다."),


    POLAROID_NOT_FOUND(HttpStatus.NOT_FOUND, "POL001", "폴라로이드를 찾을 수 없습니다."),
    POLAROID_COUNT_EXCEEDED(HttpStatus.BAD_REQUEST, "POL002", "보드당 폴라로이드는 50개까지만 생성 가능합니다."),

    // jwt
    JWT_INVALID(HttpStatus.UNAUTHORIZED, "JWT001", "유효하지 않은 토큰입니다."),
    JWT_EXPIRED(HttpStatus.UNAUTHORIZED, "JWT002", "만료된 토큰입니다."),
    JWT_MALFORMED(HttpStatus.UNAUTHORIZED, "JWT003", "잘못된 토큰입니다."),
    JWT_UNSUPPORTED(HttpStatus.UNAUTHORIZED, "JWT004", "지원되지 않는 토큰입니다."),
    JWT_ILLEGAL_ARGUMENT(HttpStatus.UNAUTHORIZED, "JWT005", "잘못된 인자입니다."),
    JWT_SIGNATURE(HttpStatus.UNAUTHORIZED, "JWT006", "잘못된 서명입니다."),

    // board
    BOARD_CREATED_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "BOARD001", "보드 생성에 실패했습니다."),
    BOARD_NOT_FOUND(HttpStatus.NOT_FOUND, "BOARD002", "보드를 찾을 수 없습니다."),

    POLAROID_DELETE_FAILED(HttpStatus.UNAUTHORIZED, "POL003", "폴라로이드 삭제할 권한이 없습니다."),

}