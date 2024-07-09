package com.ddd.sonnypolabobe.global.security

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor

@Component
class RateLimitingInterceptor(private val rateLimitingService: RateLimitingService) :
    HandlerInterceptor {

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        // 특정 URL 패턴을 필터링합니다.
        if (request.requestURI == "/api/v1/boards" && request.method == "POST") {
            if (!rateLimitingService.incrementRequestCount()) {
                response.status = HttpStatus.TOO_MANY_REQUESTS.value()
                response.writer.write("Daily request limit exceeded")
                return false
            }
        }
        return true
    }
}