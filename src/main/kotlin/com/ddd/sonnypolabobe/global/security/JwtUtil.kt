package com.ddd.sonnypolabobe.global.security

import com.ddd.sonnypolabobe.domain.user.dto.UserDto
import com.ddd.sonnypolabobe.global.exception.ApplicationException
import com.ddd.sonnypolabobe.global.exception.CustomErrorCode
import com.ddd.sonnypolabobe.logger
import io.jsonwebtoken.*
import io.jsonwebtoken.security.Keys
import jakarta.xml.bind.DatatypeConverter
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.security.Key
import java.util.*

@Component
class JwtUtil(
    @Value("\${jwt.access-key}")
    private val accessSecretKey: String,
    @Value("\${jwt.validity.access-seconds}")
    private val accessTokenExpirationMs: Long,
) {

    fun generateAccessToken(request: UserDto.Companion.CreateTokenReq): UserDto.Companion.TokenRes {
        val now = Date()
        val expiredDate = Date(now.time + accessTokenExpirationMs)
        val claims: MutableMap<String, Any> = HashMap()
        claims["CLAIM_KEY_ID"] = request.id.toString()
        claims["CLAIM_EMAIL"] = request.email
        claims["CLAIM_NICKNAME"] = request.nickName
        val accessToken = Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(expiredDate)
            .signWith(getKey(accessSecretKey), SignatureAlgorithm.HS512)
            .compact()
        return UserDto.Companion.TokenRes(accessToken, expiredDate, true, request.nickName)
    }

    fun getAuthenticatedMemberFromToken(accessToken: String): AuthenticatedMember {
        val claims = getClaimsFromAccessToken(subPrefix(accessToken), accessSecretKey)
        val id = claims["CLAIM_KEY_ID"].toString()
        val email = claims["CLAIM_EMAIL"].toString()
        val nickname = claims["CLAIM_NICKNAME"].toString()
        return AuthenticatedMember(id, email, nickname, claims.expiration)
    }

    fun validateToken(accessToken: String): Boolean {
        try {
            val key = getKey(accessSecretKey)
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(subPrefix(accessToken))
            return true
        } catch (e: Exception) {
            logger().error("error : $e")
            throw ApplicationException(CustomErrorCode.JWT_INVALID)
        }
    }

    fun getClaimsFromAccessToken(token: String, secretKey: String): Claims {
        try {
            val key = getKey(secretKey)
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).body
        } catch (e: io.jsonwebtoken.security.SecurityException) {
            throw ApplicationException(CustomErrorCode.JWT_SIGNATURE)
        } catch (e: MalformedJwtException) {
            throw ApplicationException(CustomErrorCode.JWT_MALFORMED)
        } catch (e: ExpiredJwtException) {
            throw ApplicationException(CustomErrorCode.JWT_EXPIRED)
        } catch (e: UnsupportedJwtException) {
            throw ApplicationException(CustomErrorCode.JWT_UNSUPPORTED)
        } catch (e: IllegalArgumentException) {
            throw ApplicationException(CustomErrorCode.JWT_ILLEGAL_ARGUMENT)
        }
    }

    private fun subPrefix(token: String): String {
        return if (token.isNotEmpty() && token.startsWith("Bearer ")) {
            token.substring(7)
        } else {
            token
        }
    }

    private fun getKeyBytes(secretKey: String): ByteArray {
        return DatatypeConverter.parseBase64Binary((secretKey))
    }

    private fun getKey(secretKey: String): Key {
        val keyBytes = Base64.getDecoder().decode(secretKey)
        return Keys.hmacShaKeyFor(keyBytes)
    }

}