package com.ddd.sonnypolabobe.global.util

import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.netty.http.client.HttpClient
import java.time.Duration


@Component
class WebClientUtil() {

    fun create(baseUrl: String): WebClient {
        return WebClient.builder()
            .baseUrl(baseUrl)
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .codecs { it.defaultCodecs().enableLoggingRequestDetails(true) }
            .clientConnector(
                ReactorClientHttpConnector(
                    HttpClient.create().responseTimeout(Duration.ofMillis(2500))
                )
            )
            .build()
    }

}