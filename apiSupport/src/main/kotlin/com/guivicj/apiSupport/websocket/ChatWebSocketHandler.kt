package com.guivicj.apiSupport.websocket

import com.fasterxml.jackson.databind.ObjectMapper
import com.guivicj.apiSupport.dtos.MessageDTO
import com.guivicj.apiSupport.dtos.responses.UserSessionInfoDTO
import com.guivicj.apiSupport.mappers.UserMapper
import com.guivicj.apiSupport.repositories.UserRepository
import com.guivicj.apiSupport.services.ChatBroadcastService
import com.guivicj.apiSupport.services.ChatMessageService
import org.springframework.stereotype.Component
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler
import java.time.LocalDateTime
import java.util.concurrent.ConcurrentHashMap

@Component
class ChatWebSocketHandler(
    private val chatMessageService: ChatMessageService,
    private val userRepository: UserRepository,
    private val userMapper: UserMapper,
    private val chatBroadcastService: ChatBroadcastService
) : TextWebSocketHandler() {

    private val connectedUsers = ConcurrentHashMap<Long, WebSocketSession>()
    private val objectMapper = ObjectMapper()

    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        val (ticketId, userId) = extractParams(session)
        if (ticketId == null || userId == null) return

        try {
            val dto = objectMapper.readValue(message.payload, MessageDTO::class.java)

            val user = userRepository.findById(userId)
                .orElseThrow { RuntimeException("User not found") }

            val userSession = UserSessionInfoDTO(
                user = userMapper.toDTO(user),
                authenticatedSince = LocalDateTime.now()
            )

            chatMessageService.processIncomingMessage(ticketId, dto, userSession)

        } catch (e: Exception) {
            println("Failed to handle message: ${e.message}")
        }
    }


    private fun extractParams(session: WebSocketSession): Pair<Long?, Long?> {
        val query = session.uri?.query ?: return null to null
        val params = query.split("&").associate {
            val (k, v) = it.split("=")
            k to v
        }
        val ticketId = params["ticketId"]?.toLongOrNull()
        val userId = params["userId"]?.toLongOrNull()
        return ticketId to userId
    }

    fun isUserConnected(userId: Long): Boolean = connectedUsers.containsKey(userId)

    override fun afterConnectionEstablished(session: WebSocketSession) {
        val (ticketId, userId) = extractParams(session)
        if (ticketId != null && userId != null) {
            chatBroadcastService.register(ticketId, session)
        }
    }

    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        val (ticketId, _) = extractParams(session)
        if (ticketId != null) {
            chatBroadcastService.remove(ticketId, session)
        }
    }
}
