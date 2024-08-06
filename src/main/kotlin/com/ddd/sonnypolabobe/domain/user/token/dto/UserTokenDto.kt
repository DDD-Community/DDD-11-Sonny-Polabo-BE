package com.ddd.sonnypolabobe.domain.user.token.dto

import java.time.LocalDateTime

data class UserTokenDto(
    val userId: Long,
    val accessToken: String,
    val expiredAt: LocalDateTime
)
