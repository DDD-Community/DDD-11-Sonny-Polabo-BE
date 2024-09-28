package com.ddd.sonnypolabobe.domain.user.withdraw.repository

import com.ddd.sonnypolabobe.domain.user.dto.UserDto

interface WithdrawJooqRepository {
    fun insertOne(request: UserDto.Companion.WithdrawReq, userId: Long)
}