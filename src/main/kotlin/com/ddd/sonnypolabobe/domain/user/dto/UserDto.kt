package com.ddd.sonnypolabobe.domain.user.dto

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.Email
import org.intellij.lang.annotations.RegExp
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
            val email: String,
            val nickName: String,
//            val birthDt : LocalDate?,
//            val gender : GenderType?
        )

        data class UpdateReq(
            @JsonProperty("nickName")
            val nickName: String,
            @JsonProperty("birthDt")
            val birthDt: LocalDate?,
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
            val id: Long,
            val email: String,
            val nickName: String,
            val createdAt: LocalDateTime,
        )

        data class WithdrawReq(
            val type: WithdrawType,
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