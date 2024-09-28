package com.ddd.sonnypolabobe.global.security

import com.ddd.sonnypolabobe.domain.user.repository.UserJooqRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class CustomUserDetailsService(
    private val userJooqRepository: UserJooqRepository
) : UserDetailsService {
    override fun loadUserByUsername(id: String): UserDetails =
        this.userJooqRepository.findById(id.toLong())
            ?: throw IllegalArgumentException("해당하는 사용자를 찾을 수 없습니다.")
}