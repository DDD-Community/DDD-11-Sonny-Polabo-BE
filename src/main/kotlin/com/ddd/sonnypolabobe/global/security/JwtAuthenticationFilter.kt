package com.ddd.sonnypolabobe.global.security

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter(
    private val jwtUtil: JwtUtil,
    private val customUserDetailsService: CustomUserDetailsService
) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authorizationHeader = request.getHeader("Authorization")
        if(request.requestURI.contains("/api/v1/oauth/re-issue")) {
            filterChain.doFilter(request, response)
            return
        }

        //JWT가 헤더에 있는 경우
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {

            //JWT 유효성 검증
            if (jwtUtil.validateToken(authorizationHeader)) {
                val userId = jwtUtil.getAuthenticatedMemberFromToken(authorizationHeader).id

                //유저와 토큰 일치 시 userDetails 생성
                val userDetails = customUserDetailsService.loadUserByUsername(userId)

                //UserDetsils, Password, Role -> 접근권한 인증 Token 생성
                val usernamePasswordAuthenticationToken =
                    UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)

                //현재 Request의 Security Context에 접근권한 설정
                SecurityContextHolder.getContext().authentication =
                    usernamePasswordAuthenticationToken
            }
        }
        filterChain.doFilter(request, response)
    }
}