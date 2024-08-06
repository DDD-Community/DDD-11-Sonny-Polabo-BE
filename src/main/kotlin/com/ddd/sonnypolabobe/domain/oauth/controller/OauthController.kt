package com.ddd.sonnypolabobe.domain.oauth.controller

import com.ddd.sonnypolabobe.domain.oauth.service.OauthService
import com.ddd.sonnypolabobe.global.response.ApplicationResponse
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.HttpHeaders
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/oauth")
class OauthController(private val oauthService: OauthService) {

    @Operation(summary = "카카오 소셜 로그인", description = """
    카카오 소셜 로그인을 진행합니다.
    인가 코드를 파라미터로 넘겨주세요.
    """)
    @GetMapping("/sign-in")
    fun login(@RequestParam(name = "code") code : String) = ApplicationResponse.ok(this.oauthService.signIn(code))

    @PutMapping("/re-issue")
    fun reIssue(
        @RequestHeader(name = "Authorization", required = true) header: String
    ) = run {
        ApplicationResponse.ok(this.oauthService.reIssue(header))
    }
}