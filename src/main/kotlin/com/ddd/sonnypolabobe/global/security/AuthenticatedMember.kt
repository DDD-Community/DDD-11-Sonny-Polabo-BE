package com.ddd.sonnypolabobe.global.security

import java.util.*

data class AuthenticatedMember(
    val id: String,
    val email: String,
    val nickname: String,
    val expiredAt: Date
)
