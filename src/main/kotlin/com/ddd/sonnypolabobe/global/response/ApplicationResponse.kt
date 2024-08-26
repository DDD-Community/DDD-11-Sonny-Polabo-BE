package com.ddd.sonnypolabobe.global.response

import com.ddd.sonnypolabobe.global.exception.CustomErrorCode
import com.fasterxml.jackson.annotation.JsonInclude
import jakarta.annotation.Generated
import java.time.LocalDateTime

data class ApplicationResponse<T>(
    val localDateTime: LocalDateTime,
    val code: String,
    val message: String,
    @JsonInclude(JsonInclude.Include.NON_NULL)
    val data: T?
) {
    @Generated
    companion object {
        fun ok() = ApplicationResponse(
            localDateTime = LocalDateTime.now(),
            code = "SUCCESS",
            message = "성공",
            data = null
        )

        fun <T> ok(data: T): ApplicationResponse<T> = ApplicationResponse(
            localDateTime = LocalDateTime.now(),
            code = "SUCCESS",
            message = "성공",
            data = data
        )

        fun error(errorCode: CustomErrorCode) = ApplicationResponse<Error>(
            localDateTime = LocalDateTime.now(),
            code = errorCode.code,
            message = errorCode.message,
            data = null
        )

        fun error(errorCode: CustomErrorCode, message: String) = ApplicationResponse<Error>(
            localDateTime = LocalDateTime.now(),
            code = errorCode.code,
            message = message,
            data = null
        )
    }
}
