package com.ddd.sonnypolabobe.global.security

import com.ddd.sonnypolabobe.global.exception.CustomErrorCode
import com.ddd.sonnypolabobe.global.util.DiscordApiClient
import com.ddd.sonnypolabobe.global.util.HttpLog
import com.ddd.sonnypolabobe.logger
import com.fasterxml.jackson.databind.ObjectMapper
import io.jsonwebtoken.JwtException
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.util.ContentCachingRequestWrapper
import org.springframework.web.util.ContentCachingResponseWrapper
import org.springframework.web.util.WebUtils
import java.io.UnsupportedEncodingException
import java.time.LocalDateTime
import java.util.*


@Component
class JwtExceptionFilter(
    private val discordApiClient: DiscordApiClient
) : OncePerRequestFilter() {
    private val excludedUrls = setOf("/actuator", "/swagger-ui", "/v3/api-docs")

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            val requestWrapper: ContentCachingRequestWrapper =
                ContentCachingRequestWrapper(request as HttpServletRequest)
            val responseWrapper: ContentCachingResponseWrapper =
                ContentCachingResponseWrapper(response as HttpServletResponse)
            if (excludeLogging(request.requestURI)) {
                filterChain.doFilter(request, response)
            } else {
                val startedAt = System.currentTimeMillis()
                filterChain.doFilter(requestWrapper, responseWrapper)
                val endedAt = System.currentTimeMillis()
                logger().info(
                    "\n" +
                            "[REQUEST] ${request.method} - ${request.requestURI} ${responseWrapper.status} - ${(endedAt - startedAt) / 10000.0} \n" +
                            "Headers : ${getHeaders(request)} \n" +
                            "Parameters : ${getRequestParams(request)} \n" +
                            "Request body : ${getRequestBody(requestWrapper)} \n" +
                            "Response body : ${getResponseBody(responseWrapper)}"
                )
                if (responseWrapper.status >= 400 && getResponseBody(responseWrapper).contains(
                        CustomErrorCode.INTERNAL_SERVER_EXCEPTION.message
                    )
                ) {
                    this.discordApiClient.sendErrorLog(
                        HttpLog(
                            request.method,
                            request.requestURI,
                            responseWrapper.status,
                            (endedAt - startedAt) / 10000.0,
                            getHeaders(request),
                            getRequestParams(request),
                            getRequestBody(requestWrapper),
                            getResponseBody(responseWrapper)
                        )
                    )
                }
            }
        } catch (e: JwtException) {
            response.contentType = "application/json;charset=UTF-8"
            response.status = HttpStatus.UNAUTHORIZED.value()
            response.characterEncoding = "utf-8"

            val mapper = ObjectMapper()
            val errorJson = mapper.createObjectNode()
            errorJson.put("message", e.message)
            response.writer.write(mapper.writeValueAsString(errorJson))
        }
    }

    private fun excludeLogging(requestURI: String): Boolean {
        return excludedUrls.any { requestURI.startsWith(it) }
    }

    private fun getResponseBody(response: ContentCachingResponseWrapper): String {
        var payload: String? = null
        response.characterEncoding = "utf-8"
        val wrapper =
            WebUtils.getNativeResponse(response, ContentCachingResponseWrapper::class.java)
        if (wrapper != null) {
            val buf = wrapper.contentAsByteArray
            if (buf.isNotEmpty()) {
                payload = String(buf, 0, buf.size, charset(wrapper.characterEncoding))
                wrapper.copyBodyToResponse()
            }
        }
        return payload ?: " - "
    }

    private fun getRequestBody(request: ContentCachingRequestWrapper): String {
        request.characterEncoding = "utf-8"
        val wrapper = WebUtils.getNativeRequest<ContentCachingRequestWrapper>(
            request,
            ContentCachingRequestWrapper::class.java
        )
        if (wrapper != null) {
            val buf = wrapper.contentAsByteArray
            if (buf.isNotEmpty()) {
                return try {
                    String(buf, 0, buf.size, charset(wrapper.characterEncoding))
                } catch (e: UnsupportedEncodingException) {
                    " - "
                }
            }
        }
        return " - "
    }

    private fun getRequestParams(request: HttpServletRequest): Map<String, String> {
        val parameterMap: MutableMap<String, String> = HashMap()
        request.characterEncoding = "utf-8"
        val parameterArray: Enumeration<*> = request.parameterNames

        while (parameterArray.hasMoreElements()) {
            val parameterName = parameterArray.nextElement() as String
            parameterMap[parameterName] = request.getParameter(parameterName)
        }

        return parameterMap
    }

    private fun getHeaders(request: HttpServletRequest): Map<String, String> {
        val headerMap: MutableMap<String, String> = HashMap()

        val headerArray: Enumeration<*> = request.headerNames
        while (headerArray.hasMoreElements()) {
            val headerName = headerArray.nextElement() as String
            headerMap[headerName] = request.getHeader(headerName)
        }
        return headerMap
    }
}