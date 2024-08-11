package com.ddd.sonnypolabobe.domain.oauth.controller

import com.ddd.sonnypolabobe.domain.oauth.service.OauthService
import com.ddd.sonnypolabobe.domain.user.dto.UserDto
import com.ddd.sonnypolabobe.global.response.ApplicationResponse
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.HttpHeaders
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/oauth")
class OauthController(private val oauthService: OauthService) {

    @Operation(summary = "회원가입/로그인", description = """
        회원가입/로그인을 진행합니다.
        이미 가입된 회원이라면 로그인을 진행하고, 가입되지 않은 회원이라면 회원가입을 진행합니다.
        
        요청 바디의 값이 변경되었습니다. - 2024.08.11
    """)
    @PostMapping("/sign-in")
    fun signIn(@RequestBody request: UserDto.Companion.CreateReq)
    = ApplicationResponse.ok(this.oauthService.signIn(request))

    @PutMapping("/re-issue")
    fun reIssue(
        @RequestHeader(name = "Authorization", required = true) header: String
    ) = ApplicationResponse.ok(this.oauthService.reIssue(header))
}