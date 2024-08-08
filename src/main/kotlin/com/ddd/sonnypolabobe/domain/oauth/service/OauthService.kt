package com.ddd.sonnypolabobe.domain.oauth.service

import com.ddd.sonnypolabobe.domain.user.dto.UserDto
import com.ddd.sonnypolabobe.domain.user.repository.UserJooqRepository
import com.ddd.sonnypolabobe.domain.user.token.dto.UserTokenDto
import com.ddd.sonnypolabobe.domain.user.token.repository.UserTokenJooqRepository
import com.ddd.sonnypolabobe.global.security.JwtUtil
import com.ddd.sonnypolabobe.global.security.KakaoDto
import com.ddd.sonnypolabobe.global.util.DateConverter.dateToLocalDateTime
import com.ddd.sonnypolabobe.global.util.WebClientUtil
import com.ddd.sonnypolabobe.logger
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.reactive.function.BodyInserters


@Service
class OauthService(
    private val userRepository : UserJooqRepository,
    private val jwtUtil: JwtUtil,
    private val userTokenRepository: UserTokenJooqRepository
    ) {

    @Transactional
    fun signIn(request: UserDto.Companion.CreateReq): UserDto.Companion.TokenRes {
        this.userRepository.findByEmail(request.email)?.let {
            val tokenRequest = UserDto.Companion.CreateTokenReq(
                id = it.id,
                email = it.email,
                nickName = it.nickName
            )

            val tokenRes = this.jwtUtil.generateAccessToken(tokenRequest)

            val userToken = UserTokenDto(
                userId = it.id,
                accessToken = tokenRes.accessToken,
                expiredAt = dateToLocalDateTime(tokenRes.expiredDate)
            )

            this.userTokenRepository.updateByUserId(userToken)
            return tokenRes
        } ?: run {
            val userId = this.userRepository.insertOne(request)

            // 토큰 생성
            val tokenRequest = UserDto.Companion.CreateTokenReq(
                id = userId,
                email = request.email,
                nickName = request.nickName
            )

            val tokenRes = this.jwtUtil.generateAccessToken(tokenRequest)

            val userToken = UserTokenDto(
                userId = userId,
                accessToken = tokenRes.accessToken,
                expiredAt = dateToLocalDateTime(tokenRes.expiredDate)
            )

            this.userTokenRepository.insertOne(userToken)
            return tokenRes
        }
    }

    fun reIssue(token: String?): UserDto.Companion.TokenRes{
    val tokenFromDB = token?.let {
        val slicedToken = if(it.startsWith("Bearer ")) it.substring(7) else it
        this.userTokenRepository.findByAccessToken(slicedToken)
    } ?: throw RuntimeException("Token Not Found")
        val user = this.userRepository.findById(tokenFromDB.userId) ?: throw RuntimeException("User Not Found")

        // 토큰 생성
        val tokenRequest = UserDto.Companion.CreateTokenReq(
            id = user.id,
            email = user.email,
            nickName = user.nickName
        )

        val tokenRes = this.jwtUtil.generateAccessToken(tokenRequest)

        val userToken = UserTokenDto(
            userId = user.id,
            accessToken = tokenRes.accessToken,
            expiredAt = dateToLocalDateTime(tokenRes.expiredDate)
        )

        this.userTokenRepository.updateByUserId(userToken)
        return tokenRes
    }
}