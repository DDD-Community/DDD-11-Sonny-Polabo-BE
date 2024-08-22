package com.ddd.sonnypolabobe.global.util

import com.sun.jndi.ldap.LdapPoolManager.trace
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
        val embedData: MutableMap<String, Any> = HashMap()

        embedData["title"] = "서버 에러 발생"

        val field1: MutableMap<String, String> = HashMap()
        field1["name"] = "요청"
        field1["value"] = req

        embedData["fields"] = listOf<Map<String, String>>(field1)

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