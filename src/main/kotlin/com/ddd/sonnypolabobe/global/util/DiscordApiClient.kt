package com.ddd.sonnypolabobe.global.util

import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.netty.http.client.HttpClient
import java.time.Duration


@Component
class DiscordApiClient(
    @Value("\${logging.discord.webhook-uri}")
    private val discordWebhookUri: String
) {
    fun sendDiscordComm(): WebClient = WebClient.builder().baseUrl(discordWebhookUri)
        .clientConnector(
            ReactorClientHttpConnector(
                HttpClient.create().responseTimeout(Duration.ofMillis(2500))
            )
        )
        .build()

    fun sendErrorLog(req: String) {
        val payload = """
            {
              "content": "서버 알림",
              "tts": false,
              "embeds": [{
                "title": "에러 발생",
                "description": "$req"
              }]
            }
        """.trimIndent()

        sendDiscordComm()
            .post()
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(payload)
            .retrieve()
            .bodyToMono(Void::class.java)
            .subscribe()
    }

    fun sendErrorTrace(errorCode: String, message: String?, trace: String) {
        val payload = """
            {
              "content": "서버 알림",
              "tts": false,
              "embeds": [{
                "title": "서버 에러 발생",
                "description": "에러 코드 : ${errorCode} \n  트레이스 : $trace \n 메세지 : ${message ?: "메시지 없음"}"
              }]
            }
        """.trimIndent()

        sendDiscordComm()
            .post()
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(payload)
            .retrieve()
            .bodyToMono(Void::class.java)
            .subscribe()
    }

}