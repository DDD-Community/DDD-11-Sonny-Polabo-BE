package com.ddd.sonnypolabobe.global.exception

import com.ddd.sonnypolabobe.global.response.ApplicationResponse
import com.ddd.sonnypolabobe.logger
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(ApplicationException::class)
    fun applicationException(ex: ApplicationException): ResponseEntity<ApplicationResponse<Error>> {
        logger().info("error : ${ex.error}")
        return ResponseEntity.status(ex.error.status).body(ApplicationResponse.error(ex.error))
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun validationException(ex: MethodArgumentNotValidException): ResponseEntity<ApplicationResponse<Error>> {
        logger().info("error : ${ex.bindingResult.allErrors[0].defaultMessage}")
        return ResponseEntity.status(CustomErrorCode.INVALID_VALUE_EXCEPTION.status)
            .body(ApplicationResponse.error(CustomErrorCode.INVALID_VALUE_EXCEPTION, ex.bindingResult.allErrors[0].defaultMessage!!))
    }

    @ExceptionHandler(RuntimeException::class)
    fun runtimeException(ex: RuntimeException): ResponseEntity<ApplicationResponse<Error>> {
        logger().info("error : ${ex.message}")
        return ResponseEntity.status(CustomErrorCode.INTERNAL_SERVER_EXCEPTION.status)
            .body(ApplicationResponse.error(CustomErrorCode.INTERNAL_SERVER_EXCEPTION))
    }
}