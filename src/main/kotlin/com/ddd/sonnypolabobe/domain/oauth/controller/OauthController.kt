package com.ddd.sonnypolabobe.domain.oauth.controller

import com.ddd.sonnypolabobe.domain.oauth.service.OauthService
import com.ddd.sonnypolabobe.domain.user.dto.UserDto
import com.ddd.sonnypolabobe.global.response.ApplicationResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*

@Tag(name = "1.0.0")
@RestController
@RequestMapping("/api/v1/oauth")
class OauthController(private val oauthService: OauthService) {

    @Operation(summary = "회원가입/로그인", description = """
        회원가입/로그인을 진행합니다.
        이미 가입된 회원이라면 로그인을 진행하고, 가입되지 않은 회원이라면 회원가입을 진행합니다.
        
        요청 바디의 값이 변경되었습니다. - 2024.08.11
        
        - birthDt, gender 필드는 프로필 수정에서 업데이트 가능합니다.
            응답 DTO에 기본값 바인딩으로 채웠습니다.
    """)
    @PostMapping("/sign-in")
    fun signIn(@RequestBody @Valid request: UserDto.Companion.CreateReq)
    = ApplicationResponse.ok(this.oauthService.signIn(request))

    @PutMapping("/re-issue")
    fun reIssue(
        @RequestHeader(name = "Authorization", required = true) header: String
    ) = ApplicationResponse.ok(this.oauthService.reIssue(header))

    @Operation(summary = "로그아웃", description = """
        로그아웃을 진행합니다.
        액세스 토큰을 헤더에 담아주세요.
    """)
    @PostMapping("/sign-out")
    fun signOut() : ApplicationResponse<Nothing> {
        val userId = SecurityContextHolder.getContext().authentication.principal as UserDto.Companion.Res
        this.oauthService.signOut(userId.id)
        return ApplicationResponse.ok()
    }
}