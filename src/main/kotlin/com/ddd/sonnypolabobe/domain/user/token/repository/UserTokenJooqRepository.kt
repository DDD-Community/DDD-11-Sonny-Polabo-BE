package com.ddd.sonnypolabobe.domain.user.token.repository

import com.ddd.sonnypolabobe.domain.user.token.dto.UserTokenDto

interface UserTokenJooqRepository {
    fun insertOne(userToken: UserTokenDto)
    fun findByAccessToken(token: String): UserTokenDto?
    fun updateByUserId(userToken: UserTokenDto)
}