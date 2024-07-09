package com.ddd.sonnypolabobe.global.security

import com.ddd.sonnypolabobe.global.util.DiscordApiClient
import com.ddd.sonnypolabobe.global.util.HttpLog
import com.ddd.sonnypolabobe.logger
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import org.springframework.web.filter.GenericFilterBean
import org.springframework.web.util.ContentCachingRequestWrapper
import org.springframework.web.util.ContentCachingResponseWrapper
import org.springframework.web.util.WebUtils
import java.io.UnsupportedEncodingException
import java.util.*

@Component
class LoggingFilter(
    private val discordApiClient: DiscordApiClient
) : GenericFilterBean() {
    private val excludedUrls = setOf("/actuator", "/swagger-ui")

    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        val requestWrapper: ContentCachingRequestWrapper =
            ContentCachingRequestWrapper(request as HttpServletRequest)
        val responseWrapper: ContentCachingResponseWrapper =
            ContentCachingResponseWrapper(response as HttpServletResponse)
        if (excludeLogging(request.requestURI)) {
            chain.doFilter(request, response)
        } else {
            val startedAt = System.currentTimeMillis()
            chain.doFilter(requestWrapper, responseWrapper)
            val endedAt = System.currentTimeMillis()

            logger().info(
                "\n" +
                        "[REQUEST] ${request.method} - ${request.requestURI} ${responseWrapper.status} - ${(endedAt - startedAt) / 10000.0} \n" +
                        "Headers : ${getHeaders(request)} \n" +
                        "Parameters : ${getRequestParams(request)} \n" +
                        "Request body : ${getRequestBody(requestWrapper)} \n" +
                        "Response body : ${getResponseBody(responseWrapper)}"
            )

            if(responseWrapper.status >= 400) {
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
    }

    private fun excludeLogging(requestURI: String): Boolean {
        return excludedUrls.contains(requestURI)
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