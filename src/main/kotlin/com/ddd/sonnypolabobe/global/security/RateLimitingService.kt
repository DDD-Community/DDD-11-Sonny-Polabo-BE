package com.ddd.sonnypolabobe.global.security

import com.ddd.sonnypolabobe.logger
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.util.concurrent.ConcurrentHashMap

@Service
class RateLimitingService(
    @Value("\${limit.count}")
    private val limit: Int
) {

    private val requestCounts = ConcurrentHashMap<String, Int>()
    private val LIMIT = limit
    private val REQUEST_KEY = "api_request_count"

    fun incrementRequestCount(): Boolean {
        val currentCount = requestCounts.getOrDefault(REQUEST_KEY, 0)

        if (currentCount >= LIMIT) {
            return false
        }

        requestCounts[REQUEST_KEY] = currentCount + 1
        logger().info("Request count: ${requestCounts[REQUEST_KEY]}")
        return true
    }

    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul")
    fun resetRequestCount() = requestCounts.clear()
}