package com.guivicj.apiSupport.services

import com.guivicj.apiSupport.dtos.responses.OpenAiResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient

@Service
class OpenAiService(
    @Value("\${openai.api.key}") private val apiKey: String,
    @Value("\${openai.api.url}") private val apiUrl: String,
    @Value("\${openai.model}") private val model: String
) {

    private val webClient = WebClient.builder()
        .baseUrl(apiUrl)
        .defaultHeader("Authorization", "Bearer $apiKey")
        .defaultHeader("Content-Type", "application/json")
        .build()

    fun sendMessage(messages: List<Map<String, String>>): String {
        val requestBody = mapOf(
            "model" to model,
            "messages" to messages
        )

        val response = webClient.post()
            .bodyValue(requestBody)
            .retrieve()
            .bodyToMono(OpenAiResponse::class.java)
            .block()

        return response?.choices?.firstOrNull()?.message?.get("content")?.trim()
            ?: "Sorry, I couldn't generate any answers"
    }
}
