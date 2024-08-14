package com.ddd.sonnypolabobe.global.config

import com.ddd.sonnypolabobe.global.security.JwtAuthenticationFilter
import com.ddd.sonnypolabobe.global.security.JwtExceptionFilter
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.security.web.util.matcher.RequestMatcher
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
@EnableMethodSecurity
class SecurityConfig(
    private val jwtAuthenticationFilter: JwtAuthenticationFilter,
    private val jwtExceptionFilter: JwtExceptionFilter
) {
    companion object {
        val ALLOW_URLS = listOf<String>(
            "/api/v1/boards/{id}",
            "/api/v1/boards/create-available",
            "/api/v1/boards/total-count",
            "/api/v1/file/**",
            "/api/v1/oauth/sign-in",
            "/api/v1/oauth/re-issue",
            "/api/v1/user/check-exist",
            "/health",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/api/v1/polaroids/{id}",
            "/api/v1/boards/{boardId}/polaroids"
        )
    }
    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        return http
            .cors {}
            .csrf{
                it.disable()
            }
            .httpBasic {
                it.disable()
            }
            .formLogin { it.disable() }
            .sessionManagement { sessionManagementConfig ->
                sessionManagementConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
            .addFilterBefore(jwtExceptionFilter, JwtAuthenticationFilter::class.java)
            .authorizeHttpRequests {
                it.requestMatchers(RequestMatcher { request ->
                    ALLOW_URLS.any { url -> AntPathRequestMatcher(url).matches(request) }
                }).permitAll()
                it.anyRequest().authenticated()
            }
            .build()
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {

        val configuration = CorsConfiguration().apply {
            allowCredentials = true
            allowedOrigins = listOf("http://localhost:3000", "https://polabo.site",
                "http://polabo.site", "http://dev.polabo.site", "https://dev.polabo.site")
            allowedMethods = listOf(
                HttpMethod.POST.name(),
                HttpMethod.GET.name(),
                HttpMethod.PUT.name(),
                HttpMethod.DELETE.name(),
                HttpMethod.OPTIONS.name()
            )
            allowedHeaders = listOf("*")
        }

        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)

        return source

    }
}