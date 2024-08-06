package com.ddd.sonnypolabobe.domain.user.controller

import com.ddd.sonnypolabobe.domain.user.dto.UserDto
import com.ddd.sonnypolabobe.domain.user.service.UserService
import com.ddd.sonnypolabobe.global.response.ApplicationResponse
import com.ddd.sonnypolabobe.global.util.DateConverter
import io.swagger.v3.oas.annotations.Operation
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@RestController
@RequestMapping("/api/v1/user")
class UserController(
    private val userService: UserService
) {

    @Operation(summary = "닉네임 변경", description = """
        닉네임을 변경합니다.
    """)
    @PutMapping("/nickname")
    fun updateNickname(@RequestBody request: UserDto.Companion.UpdateReq)
    = run {
        val userInfoFromToken = SecurityContextHolder.getContext().authentication.principal as UserDto.Companion.Res
        this.userService.updateProfile(request, userInfoFromToken.id)
        ApplicationResponse.ok()
    }

    @Operation(summary = "프로필 조회", description = """
        프로필을 조회합니다.
    """)
    @GetMapping("/profile")
    fun getProfile() = run {
        val userInfoFromToken = SecurityContextHolder.getContext().authentication.principal as UserDto.Companion.Res
        ApplicationResponse.ok(this.userService.findById(userInfoFromToken.id))
    }

    @Operation(summary = "회원 탈퇴", description = """
        회원 탈퇴를 진행합니다.
        탈퇴 사유를 입력해주세요.
        사유가 '기타'인 경우에만 reason 필드를 채워주세요.
    """)
    @PutMapping("/withdraw")
    fun withdraw(@RequestBody request: UserDto.Companion.WithdrawReq) = run {
        val userInfoFromToken = SecurityContextHolder.getContext().authentication.principal as UserDto.Companion.Res
        this.userService.withdraw(request, userInfoFromToken.id)
        ApplicationResponse.ok()
    }
}