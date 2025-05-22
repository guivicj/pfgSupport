package com.guivicj.apiSupport.services

import com.guivicj.apiSupport.dtos.responses.OpenAiResponse
import org.apache.http.HttpHeaders
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import java.time.Duration
import java.time.Instant

@Service
class OpenAiService(
    @Value("\${openai.api.key}") private val apiKey: String,
    @Value("\${openai.api.url}") private val apiUrl: String,
    @Value("\${openai.model}") private val model: String
) {
    private var lastAiCallTime = Instant.MIN

    private val webClient = WebClient.builder()
        .baseUrl(apiUrl)
        .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer $apiKey")
        .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json")
        .build()


    fun shouldCallAI(): Boolean {
        val now = Instant.now()
        if (Duration.between(lastAiCallTime, now).seconds < 1) return false
        lastAiCallTime = now
        return true
    }

    fun sendMessage(messages: List<Map<String, String>>): String {
        val requestBody = mapOf(
            "model" to model,
            "messages" to messages
        )

        val response = webClient.post()
            .uri("/v1/chat/completions")
            .bodyValue(requestBody)
            .retrieve()
            .bodyToMono(OpenAiResponse::class.java)
            .block()

        return response?.choices?.firstOrNull()?.message?.content?.trim()
            ?: "Failed to generate response from OpenAI. Try again later."
    }
}
