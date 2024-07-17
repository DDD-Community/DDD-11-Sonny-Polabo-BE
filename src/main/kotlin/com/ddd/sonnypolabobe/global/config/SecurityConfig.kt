package com.ddd.sonnypolabobe.global.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.security.web.util.matcher.RequestMatcher
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
@EnableMethodSecurity
class SecurityConfig() {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        return http
            .cors {
                it.configurationSource(corsConfigurationSource())
            }
            .csrf{
                it.disable()
            }
            .httpBasic {
                it.disable()
            }
            .formLogin { it.disable() }
            .authorizeHttpRequests {
                it.anyRequest().permitAll()
            }
            .build()
    }

    fun corsConfigurationSource(): UrlBasedCorsConfigurationSource {
        val configuration = CorsConfiguration()
        configuration.allowedOrigins = listOf("http://localhost:3000", "https://polabo.site",
            "http://polabo.site", "http://dev.polabo.site", "https://dev.polabo.site") // Allow all origins
        configuration.allowedMethods =
            listOf("GET", "POST", "PUT", "DELETE", "OPTIONS") // Allow common methods
        configuration.allowedHeaders = listOf("*") // Allow all headers
        configuration.allowCredentials = true // Allow credentials
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration(
            "/**",
            configuration
        ) // Apply configuration to all endpoints
        return source
    }
}