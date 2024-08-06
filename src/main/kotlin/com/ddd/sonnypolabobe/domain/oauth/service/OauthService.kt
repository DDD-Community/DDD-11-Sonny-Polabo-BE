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
    private val webClient : WebClientUtil,
    private val objectMapper: ObjectMapper,
    @Value("\${spring.security.oauth2.client.registration.kakao.client-id}") private val clientId : String,
    @Value("\${spring.security.oauth2.client.registration.kakao.redirect-uri}") private val redirectUri : String,
    @Value("\${spring.security.oauth2.client.registration.kakao.client-secret}") private val clientSecret : String,
    private val userRepository : UserJooqRepository,
    private val jwtUtil: JwtUtil,
    private val userTokenRepository: UserTokenJooqRepository
    ) {

    fun getAccessToken(code: String) : KakaoDto.Companion.Token {

        //요청 본문
        val params: MultiValueMap<String, String> = LinkedMultiValueMap()
        params.add("grant_type", "authorization_code")
        params.add("client_id", clientId)
        params.add("redirect_uri", redirectUri)
        params.add("code", code)
        params.add("client_secret", clientSecret)

        logger().error("params : $params")
        // 요청 보내기 및 응답 수신
        val response = webClient.create("https://kauth.kakao.com")
            .post()
            .uri("/oauth/token")
            .header("Content-type", "application/x-www-form-urlencoded")
            .body(BodyInserters.fromFormData(params))
            .retrieve()
            .bodyToMono(String::class.java)
            .block()


        return try {
            this.objectMapper.readValue(response, KakaoDto.Companion.Token::class.java)
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

    fun getKakaoInfo(accessToken: String): KakaoDto.Companion.UserInfo {

        // 요청 보내기 및 응답 수신
        val response = webClient.create("https://kapi.kakao.com")
            .post()
            .uri("/v2/user/me")
            .header("Content-type", "application/x-www-form-urlencoded")
            .header("Authorization", "Bearer $accessToken")
            .retrieve()
            .bodyToMono(String::class.java)
            .block()

        return try {
            this.objectMapper.readValue(response, KakaoDto.Companion.UserInfo::class.java)
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

    @Transactional
    fun signIn(code: String): UserDto.Companion.TokenRes {
        val token = getAccessToken(code)
        val userInfo = getKakaoInfo(token.access_token)

        // DB에 저장
        val request = UserDto.Companion.CreateReq(
            nickName = userInfo.kakao_account.profile.nickname,
            email = userInfo.kakao_account.email
        )

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
                email = userInfo.kakao_account.email,
                nickName = userInfo.kakao_account.profile.nickname
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