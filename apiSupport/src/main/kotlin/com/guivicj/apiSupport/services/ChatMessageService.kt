package com.guivicj.apiSupport.services

import com.fasterxml.jackson.databind.ObjectMapper
import com.guivicj.apiSupport.dtos.MessageDTO
import com.guivicj.apiSupport.dtos.responses.UserSessionInfoDTO
import com.guivicj.apiSupport.enums.ChatRole
import com.guivicj.apiSupport.models.TicketMessage
import com.guivicj.apiSupport.repositories.TicketMessageRepository
import com.guivicj.apiSupport.repositories.TicketRepository
import org.springframework.stereotype.Service

@Service
class ChatMessageService(
    private val openAiService: OpenAiService,
    private val ticketService: TicketService,
    private val broadcastService: ChatBroadcastService,
    private val techService: TechService,
    private val ticketMessageRepository: TicketMessageRepository,
    private val ticketRepository: TicketRepository
) {
    fun processIncomingMessage(ticketId: Long, message: MessageDTO, userSession: UserSessionInfoDTO) {
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
                        "content" to "You are a technical support assistant. In your first message, " +
                                "introduce yourself as an AI assistant assigned to help with the user's issue. " +
                                "Respond respectfully and concisely. Let the user know that if they write " +
                                "\"real person\", their ticket will be escalated to a human technician."
                    ),
                    mapOf("role" to "user", "content" to userContent)
                )
            )
            val ticket = ticketRepository.findById(ticketId)
                .orElseThrow { RuntimeException("Ticket not found") }

            val aiMessageEntity = TicketMessage(
                ticket = ticket,
                role = ChatRole.TECHNICIAN,
                content = aiReplyText
            )
            ticketMessageRepository.save(aiMessageEntity)

            val aiDto = MessageDTO(
                ticketId = ticket.id,
                role = ChatRole.TECHNICIAN,
                content = aiReplyText
            )

            val aiJson = ObjectMapper().writeValueAsString(aiDto)
            broadcastService.broadcast(ticketId, aiJson)

        }
    }
}
