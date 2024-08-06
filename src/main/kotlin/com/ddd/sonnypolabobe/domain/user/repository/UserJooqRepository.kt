package com.ddd.sonnypolabobe.domain.user.repository

import com.ddd.sonnypolabobe.domain.user.dto.UserDto

interface UserJooqRepository {
    fun insertOne(request: UserDto.Companion.CreateReq): Long

    fun findById(id: Long): UserDto.Companion.Res?
    fun findByEmail(email: String): UserDto.Companion.Res?
    fun updateProfile(request: UserDto.Companion.UpdateReq, userId: Long)
}