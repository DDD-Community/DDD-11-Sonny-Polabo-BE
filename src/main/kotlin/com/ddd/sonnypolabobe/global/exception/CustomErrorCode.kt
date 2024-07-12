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
    POLAROID_COUNT_EXCEEDED(HttpStatus.BAD_REQUEST, "POL002", "보드당 폴라로이드는 50개까지만 생성 가능합니다.")

}