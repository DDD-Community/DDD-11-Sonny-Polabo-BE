package com.ddd.sonnypolabobe.global.exception

import com.ddd.sonnypolabobe.global.response.ApplicationResponse
import com.ddd.sonnypolabobe.global.util.DiscordApiClient
import com.ddd.sonnypolabobe.logger
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler(
    private val discordApiClient: DiscordApiClient
) {
    @ExceptionHandler(ApplicationException::class)
    fun applicationException(ex: ApplicationException): ResponseEntity<ApplicationResponse<Error>> {
        logger().error("error : ${ex.error}")
        if (ex.error.status.is5xxServerError) {
            this.discordApiClient.sendErrorTrace(
                ex.error.status.toString(), ex.error.message,
                ex.stackTrace.contentToString()
            )
        }
        return ResponseEntity.status(ex.error.status).body(ApplicationResponse.error(ex.error))
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun validationException(ex: MethodArgumentNotValidException): ResponseEntity<ApplicationResponse<Error>> {
        logger().error("error : ${ex.bindingResult.allErrors[0].defaultMessage}")
        return ResponseEntity.status(CustomErrorCode.INVALID_VALUE_EXCEPTION.status)
            .body(
                ApplicationResponse.error(
                    CustomErrorCode.INVALID_VALUE_EXCEPTION,
                    ex.bindingResult.allErrors[0].defaultMessage!!
                )
            )
    }

    @ExceptionHandler(RuntimeException::class)
    fun runtimeException(ex: RuntimeException): ResponseEntity<ApplicationResponse<Error>> {
        logger().error("error : ${ex.message}")
        this.discordApiClient.sendErrorTrace(
            "500", ex.message,
            ex.stackTrace.contentToString()
        )
        return ResponseEntity.status(CustomErrorCode.INTERNAL_SERVER_EXCEPTION.status)
            .body(ApplicationResponse.error(CustomErrorCode.INTERNAL_SERVER_EXCEPTION))
    }
}