package com.ddd.sonnypolabobe.domain.user.service

import com.ddd.sonnypolabobe.domain.user.dto.UserDto
import com.ddd.sonnypolabobe.domain.user.repository.UserJooqRepository
import com.ddd.sonnypolabobe.domain.user.withdraw.repository.WithdrawJooqRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    private val userJooqRepository: UserJooqRepository,
    private val withdrawJooqRepository: WithdrawJooqRepository
) {
    @Transactional
    fun updateProfile(request: UserDto.Companion.UpdateReq, userId: Long) {
        this.userJooqRepository.updateProfile(request, userId)
    }

    @Transactional(readOnly = true)
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

    @Transactional
    fun withdraw(request: UserDto.Companion.WithdrawReq, id: Long) {
        this.withdrawJooqRepository.insertOne(request, id)
        this.userJooqRepository.deleteById(id)
    }

    @Transactional(readOnly = true)
    fun checkExist(email: String): Boolean {
        return this.userJooqRepository.findByEmail(email) != null
    }


}