package com.ddd.sonnypolabobe.global.config

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import io.swagger.v3.oas.models.servers.Server
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class SwaggerConfig {

    @Bean
    fun openAPI(): OpenAPI {
        val securityScheme: SecurityScheme = getSecurityScheme()
        val securityRequirement: SecurityRequirement = getSecurityRequirement()

        return OpenAPI()
            .addServersItem(Server().url("/"))
            .components(Components().addSecuritySchemes("bearerAuth", securityScheme))
            .security(listOf(securityRequirement))
    }

    private fun getSecurityScheme(): SecurityScheme =
        SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("Bearer").bearerFormat("JWT")
            .`in`(SecurityScheme.In.HEADER).name("Authorization")


    private fun getSecurityRequirement(): SecurityRequirement =
        SecurityRequirement().addList("bearerAuth")
}