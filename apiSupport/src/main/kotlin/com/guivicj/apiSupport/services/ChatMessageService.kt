package com.guivicj.apiSupport.services

import com.fasterxml.jackson.databind.ObjectMapper
import com.guivicj.apiSupport.dtos.MessageDTO
import com.guivicj.apiSupport.dtos.responses.UserSessionInfoDTO
import com.guivicj.apiSupport.enums.ChatRole
import org.springframework.stereotype.Service

@Service
class ChatMessageService(
    private val openAiService: OpenAiService,
    private val ticketService: TicketService,
    private val broadcastService: ChatBroadcastService,
    private val techService: TechService
) {
    fun processIncomingMessage(ticketId: Long, userId: Long, message: MessageDTO, userSession: UserSessionInfoDTO) {
        val json = ObjectMapper().writeValueAsString(message)

        broadcastService.broadcast(ticketId, json)

        val ticket = ticketService.getTicketById(ticketId)
        val tech = techService.getTechById(ticket.technicianId) ?: return
        val userContent = message.content ?: return

        if (message.content.contains("real person", ignoreCase = true)) {
            if (tech.technicianType.name == "CHAT") {
                ticketService.assignToAvailableHuman(ticketId, userSession)
                println("Escalated ticket $ticketId to a human technician.")
            }
        }

        if (message.role == ChatRole.USER && ticket.technicianId == 0L && openAiService.shouldCallAI()) {
            val aiReplyText = openAiService.sendMessage(
                listOf(
                    mapOf(
                        "role" to "system",
                        "content" to "You are a technical support assistant. In your first message, you must introduce" +
                                " yourself and explain that you are an AI assistant helping with the issue. Let the user " +
                                "know that if they write \"real person\", you will escalate the ticket to a human technician."
                    ),
                    mapOf("role" to "user", "content" to userContent)
                )
            )

            val aiMessage = message.copy(
                content = aiReplyText,
                role = ChatRole.TECHNICIAN,
            )

            val aiJson = ObjectMapper().writeValueAsString(aiMessage)
            broadcastService.broadcast(ticketId, aiJson)
        }
    }
}
