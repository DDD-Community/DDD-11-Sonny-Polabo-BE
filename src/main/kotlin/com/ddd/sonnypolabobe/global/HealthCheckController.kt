package com.ddd.sonnypolabobe.global

import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "1.0.0")
@RestController
class HealthCheckController {

    @GetMapping("/health")
    fun healthCheck(): String = "OK"
}