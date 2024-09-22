package com.ddd.sonnypolabobe.domain.oauth.service

import com.ddd.sonnypolabobe.domain.user.dto.GenderType
import com.ddd.sonnypolabobe.domain.user.dto.UserDto
import com.ddd.sonnypolabobe.domain.user.repository.UserJooqRepository
import com.ddd.sonnypolabobe.domain.user.token.dto.UserTokenDto
import com.ddd.sonnypolabobe.domain.user.token.repository.UserTokenJooqRepository
import com.ddd.sonnypolabobe.global.security.JwtUtil
import com.ddd.sonnypolabobe.global.util.DateConverter.dateToLocalDateTime
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
class OauthService(
    private val userRepository: UserJooqRepository,
    private val userTokenRepository: UserTokenJooqRepository,
    private val jwtUtil: JwtUtil,
) {

    @Transactional
    fun signIn(request: UserDto.Companion.CreateReq): UserDto.Companion.TokenRes {
        this.userRepository.findByEmail(request.email)?.let {
            val tokenRes = generateAccessToken(it)
            val userToken = userTokenDto(it.id, tokenRes)

            this.userTokenRepository.updateByUserId(userToken)
            return tokenRes.also { _ ->
                tokenRes.isNewUser = false
                tokenRes.birthDt = it.birthDt
                tokenRes.gender = it.gender
            }
        } ?: run {
            val userId = this.userRepository.insertOne(request)
            val tokenRes = generateAccessToken(userId, request)
            val userToken = userTokenDto(userId, tokenRes)

            this.userTokenRepository.insertOne(userToken)
            return tokenRes.also { _ ->
                tokenRes.isNewUser = true
                tokenRes.birthDt = null
                tokenRes.gender = GenderType.NONE
            }
        }
    }

    @Transactional
    fun reIssue(token: String?): UserDto.Companion.TokenRes {
        val tokenFromDB = token?.let {
            val slicedToken = if (it.startsWith("Bearer ")) it.substring(7) else it
            this.jwtUtil.getAuthenticatedMemberFromRefreshToken(slicedToken)
        } ?: throw RuntimeException("Token Not Found")

        val user = this.userRepository.findById(tokenFromDB.id.toLong())
            ?: throw RuntimeException("User Not Found")

        val tokenRes = generateAccessToken(user)
        val userToken = userTokenDto(user.id, tokenRes)

        this.userTokenRepository.updateByUserId(userToken)
        return tokenRes
    }

    @Transactional
    fun signOut(id: Long) {
        this.userTokenRepository.deleteByUserId(id)
    }

    private fun userTokenDto(
        userId: Long,
        tokenRes: UserDto.Companion.TokenRes
    ): UserTokenDto = UserTokenDto(
        userId = userId,
        accessToken = tokenRes.accessToken,
        expiredAt = dateToLocalDateTime(tokenRes.expiredDate),
        refreshToken = tokenRes.refreshToken
    )

    private fun OauthService.generateAccessToken(
        userId: Long, request: UserDto.Companion.CreateReq
    ): UserDto.Companion.TokenRes = this.jwtUtil.generateAccessToken(
        UserDto.Companion.CreateTokenReq(
            id = userId, email = request.email, nickName = request.nickName
        )
    )

    private fun OauthService.generateAccessToken(it: UserDto.Companion.Res): UserDto.Companion.TokenRes =
        this.jwtUtil.generateAccessToken(
            UserDto.Companion.CreateTokenReq(
                id = it.id, email = it.email, nickName = it.nickName
            )
        )
}