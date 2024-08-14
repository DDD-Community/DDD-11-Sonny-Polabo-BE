package com.ddd.sonnypolabobe.domain.user.controller

import com.ddd.sonnypolabobe.domain.user.dto.UserDto
import com.ddd.sonnypolabobe.domain.user.service.UserService
import com.ddd.sonnypolabobe.global.response.ApplicationResponse
import com.ddd.sonnypolabobe.global.util.DateConverter
import io.swagger.v3.oas.annotations.Operation
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime

@RestController
@RequestMapping("/api/v1/user")
class UserController(
    private val userService: UserService
) {

    @Operation(summary = "프로필 변경", description = """
        프로필 사항을 변경합니다.
        
        유저가 가진 정보 중 변경한 값 + 변경하지 않은 값 모두 보내주세요.
        보내는 값을 그대로 디비에 저장합니다.
    """)
    @PutMapping("/nickname")
    fun updateNickname(@RequestBody request: UserDto.Companion.UpdateReq)
    : ApplicationResponse<Nothing> {
        val userInfoFromToken = SecurityContextHolder.getContext().authentication.principal as UserDto.Companion.Res
        this.userService.updateProfile(request, userInfoFromToken.id)
        return ApplicationResponse.ok()
    }

    @Operation(summary = "프로필 조회", description = """
        프로필을 조회합니다.
    """)
    @GetMapping("/profile")
    fun getProfile() : ApplicationResponse<UserDto.Companion.ProfileRes> {
        val userInfoFromToken = SecurityContextHolder.getContext().authentication.principal as UserDto.Companion.Res
        return ApplicationResponse.ok(this.userService.findById(userInfoFromToken.id))
    }

    @Operation(summary = "회원 탈퇴", description = """
        회원 탈퇴를 진행합니다.
        탈퇴 사유를 입력해주세요.
        사유가 '기타'인 경우에만 reason 필드를 채워주세요.
    """)
    @PutMapping("/withdraw")
    fun withdraw(@RequestBody request: UserDto.Companion.WithdrawReq) : ApplicationResponse<Nothing> {
        val userInfoFromToken = SecurityContextHolder.getContext().authentication.principal as UserDto.Companion.Res
        this.userService.withdraw(request, userInfoFromToken.id)
        return ApplicationResponse.ok()
    }

    @Operation(summary = "회원 계정 존재 여부 확인", description = """
        이메일로 계정 등록 여부를 확인합니다.
    """)
    @GetMapping("/check-exist")
    fun checkExist(
        @RequestParam("email") email: String
    ) = ApplicationResponse.ok(this.userService.checkExist(email))
}