package com.ddd.sonnypolabobe.domain.user.dto

import com.ddd.sonnypolabobe.domain.user.withdraw.dto.WithdrawType
import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Email
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.Date
import java.util.stream.Collectors

class UserDto {
    companion object {
        data class CreateReq(
            @field:Email
            @field:Schema(description = "이메일", example = "test@polabo.com")
            val email: String,
            @field:Schema(description = "닉네임", example = "쏘니")
            val nickName: String,
        )

        data class UpdateReq(
            @JsonProperty("nickName")
            @field:Schema(description = "닉네임", example = "쏘니")
            val nickName: String,
            @JsonProperty("birthDt")
            @field:Schema(description = "생년월일", example = "1990-01-01")
            val birthDt: LocalDate?,
            @field:Schema(description = "성별", example = "NONE, F, M")
            val gender: GenderType?
        )

        data class CreateTokenReq(
            val id: Long,
            val email: String,
            val nickName: String
        )

        data class TokenRes(
            val accessToken: String,
            val refreshToken: String,
            val expiredDate: Date,
            var isNewUser: Boolean,
            val nickName: String,
            var birthDt: LocalDate?,
            var gender: GenderType
        ) {
            constructor(
                accessToken: String,
                refreshToken: String,
                expiredDate: Date,
                isNewUser: Boolean,
                nickName: String
            ) : this(
                accessToken,
                refreshToken,
                expiredDate,
                isNewUser,
                nickName,
                null,
                GenderType.NONE
            )
        }

        data class ProfileRes(
            @field:Schema(description = "아이디", example = "1")
            val id: Long,
            @field:Schema(description = "이메일", example = "test@polabo.com")
            val email: String,
            @field:Schema(description = "닉네임", example = "쏘니")
            val nickName: String,
            @field:Schema(description = "생년월일", example = "1990-01-01")
            val createdAt: LocalDateTime,
        )

        data class WithdrawReq(
            @field:Schema(description = "탈퇴 타입", example = """
                NOT_USE : 사용하지 않음
                WORRY_ABOUT_PERSONAL_INFO : 개인정보 우려
                DROP_MY_DATA : 내 데이터 삭제
                WANT_TO_NEW_ACCOUNT : 새로운 계정을 만들고 싶어요
                OTHER : 기타  
            """)
            val type: WithdrawType,
            @field:Schema(description = "탈퇴 사유", example = "탈퇴 사유입니다.")
            val reason: String?
        )

        data class Res(
            val id: Long,
            val email: String,
            val nickName: String,
            val yn: Boolean,
            val createdAt: LocalDateTime,
            val updatedAt: LocalDateTime?,
            val birthDt: LocalDate?,
            val gender : GenderType
        ) : UserDetails {
            override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
                val roles = mutableListOf("ROLE_USER")
                return roles.stream()
                    .map { role -> SimpleGrantedAuthority(role) }
                    .collect(Collectors.toList())
            }

            override fun getPassword(): String {
                return ""
            }

            override fun getUsername(): String {
                return id.toString()
            }
        }
    }
}