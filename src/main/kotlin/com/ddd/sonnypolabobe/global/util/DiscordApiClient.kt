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

    fun sendErrorLog(req: HttpLog) {
        val embedData: MutableMap<String, Any> = HashMap()

        embedData["title"] = "서버 에러 발생"

        val field1: MutableMap<String, String> = HashMap()
        field1["name"] = "요청 정보"
        field1["value"] = req.requestMethod + " " + req.requestURI + " " + req.elapsedTime + "ms"

        val field2: MutableMap<String, String> = HashMap()
        field2["name"] = "응답 코드"
        field2["value"] = req.responseStatus.toString()

        val field3: MutableMap<String, String> = HashMap()
        field3["name"] = "요청 헤더"
        field3["value"] = req.headers.map { it.key + " : " + it.value }.joinToString("\n")

        val field4: MutableMap<String, String> = HashMap()
        field4["name"] = "요청 본문"
        field4["value"] = req.requestBody

        val field5: MutableMap<String, String> = HashMap()
        field5["name"] = "요청 파람"
        field5["value"] = req.parameters.map { it.key + " : " + it.value }.joinToString("\n")

        val field6: MutableMap<String, String> = HashMap()
        field6["name"] = "응답 본문"
        field6["value"] = req.responseBody

        embedData["fields"] = listOf<Map<String, String>>(field1, field2, field3, field4, field5, field6)

        val payload: MutableMap<String, Any> = HashMap()
        payload["embeds"] = arrayOf<Any>(embedData)

        sendDiscordComm()
            .post()
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(payload)
            .retrieve()
            .bodyToMono(Void::class.java)
            .subscribe()
    }

    fun sendErrorTrace(errorCode: String, message: String?, trace: String) {
        val embedData: MutableMap<String, Any> = HashMap()

        embedData["title"] = "서버 에러 발생"

        val field1: MutableMap<String, String> = HashMap()
        field1["name"] = "트레이스"
        field1["value"] = trace

        val field2: MutableMap<String, String> = HashMap()
        field2["name"] = "에러 코드"
        field2["value"] = errorCode

        val field3: MutableMap<String, String> = HashMap()
        field3["name"] = "메시지"
        field3["value"] = message ?: "메시지 없음"

        embedData["fields"] = listOf<Map<String, String>>(field1, field2, field3)

        val payload: MutableMap<String, Any> = HashMap()
        payload["embeds"] = arrayOf<Any>(embedData)

        sendDiscordComm()
            .post()
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(payload)
            .retrieve()
            .bodyToMono(Void::class.java)
            .subscribe()
    }

}