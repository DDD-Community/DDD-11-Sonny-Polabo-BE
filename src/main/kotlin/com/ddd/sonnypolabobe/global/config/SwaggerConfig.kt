package com.ddd.sonnypolabobe.global.config

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class SwaggerConfig {

    @Bean
    fun openAPI(): OpenAPI {
        val securityScheme: SecurityScheme = getSecurityScheme()
        val securityRequirement: SecurityRequirement = getSecurityRequireMent()

        return OpenAPI()
            .components(Components().addSecuritySchemes("bearerAuth", securityScheme))
            .security(listOf(securityRequirement))
    }

    private fun getSecurityScheme(): SecurityScheme {
        return SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("Bearer").bearerFormat("JWT")
            .`in`(SecurityScheme.In.HEADER).name("Authorization")
    }

    private fun getSecurityRequireMent(): SecurityRequirement {
        return SecurityRequirement().addList("bearerAuth")
    }
}