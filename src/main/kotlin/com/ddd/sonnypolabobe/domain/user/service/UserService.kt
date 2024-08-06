package com.ddd.sonnypolabobe.domain.user.service

import com.ddd.sonnypolabobe.domain.user.dto.UserDto
import com.ddd.sonnypolabobe.domain.user.repository.UserJooqRepository
import com.ddd.sonnypolabobe.domain.user.repository.WithdrawJooqRepository
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userJooqRepository: UserJooqRepository,
    private val withdrawJooqRepository: WithdrawJooqRepository
) {
    fun updateProfile(request: UserDto.Companion.UpdateReq, userId: Long) {
        this.userJooqRepository.updateProfile(request, userId)
    }

    fun findById(id: Long): UserDto.Companion.ProfileRes {
        return this.userJooqRepository.findById(id).let {
            UserDto.Companion.ProfileRes(
                id = it!!.id,
                nickName = it.nickName,
                email = it.email,
                createdAt = it.createdAt
            )
        }
    }

    fun withdraw(request: UserDto.Companion.WithdrawReq, id: Long) {
        this.withdrawJooqRepository.insertOne(request, id)
    }


}