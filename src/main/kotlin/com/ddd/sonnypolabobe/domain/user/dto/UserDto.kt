package com.ddd.sonnypolabobe.domain.user.dto

import com.fasterxml.jackson.annotation.JsonProperty
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
            val email: String,
            val nickName: String,
            val birthDt : LocalDate,
            val gender : GenderType
            )

        data class UpdateReq(
            @JsonProperty("nickName")
            val nickName : String
        )

        data class CreateTokenReq(
            val id : Long,
            val email: String,
            val nickName: String
        )

        data class TokenRes(
            val accessToken: String,
            val expiredDate: Date,
            val isNewUser : Boolean,
            val nickName: String
        )

        data class ProfileRes(
            val id: Long,
            val email: String,
            val nickName: String,
            val createdAt: LocalDateTime,
        )

        data class WithdrawReq(
            val type: WithdrawType,
            val reason : String?
        )

        data class Res(
            val id: Long,
            val email: String,
            val nickName: String,
            val yn: Boolean,
            val createdAt: LocalDateTime,
            val updatedAt: LocalDateTime?
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