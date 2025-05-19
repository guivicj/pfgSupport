package com.guivicj.apiSupport.websocket

import com.fasterxml.jackson.databind.ObjectMapper
import com.guivicj.apiSupport.dtos.MessageDTO
import com.guivicj.apiSupport.models.TicketMessage
import com.guivicj.apiSupport.repositories.TicketMessageRepository
import com.guivicj.apiSupport.repositories.TicketRepository
import org.springframework.stereotype.Component
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler
import java.util.concurrent.ConcurrentHashMap

@Component
class ChatWebSocketHandler(
    private val ticketRepository: TicketRepository,
    private val ticketMessageRepository: TicketMessageRepository
) : TextWebSocketHandler() {

    private val sessionsByTicket = ConcurrentHashMap<Long, MutableSet<WebSocketSession>>()
    private val connectedUsers = ConcurrentHashMap<Long, WebSocketSession>()
    private val objectMapper = ObjectMapper()

    override fun afterConnectionEstablished(session: WebSocketSession) {
        val (ticketId, userId) = extractParams(session)
        if (ticketId != null && userId != null) {
            sessionsByTicket.computeIfAbsent(ticketId) { mutableSetOf() }.add(session)
            connectedUsers[userId] = session
            println("Connected user $userId to ticket $ticketId")
        }
    }

    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        val (ticketId, userId) = extractParams(session)
        if (ticketId == null || userId == null) return

        try {
            val dto = objectMapper.readValue(message.payload, MessageDTO::class.java)
            val ticket = ticketRepository.findById(ticketId).orElseThrow()

            val ticketMessage = TicketMessage(
                ticket = ticket,
                role = dto.role!!,
                content = dto.content!!,
            )
            ticketMessageRepository.save(ticketMessage)

            val jsonBroadcast = objectMapper.writeValueAsString(dto)
            sessionsByTicket[ticketId]?.forEach {
                if (it.isOpen) it.sendMessage(TextMessage(jsonBroadcast))
            }
        } catch (e: Exception) {
            println("Failed to handle message: ${e.message}")
        }
    }

    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        val (ticketId, userId) = extractParams(session)
        if (ticketId != null) {
            sessionsByTicket[ticketId]?.remove(session)
        }
        if (userId != null) {
            connectedUsers.remove(userId)
        }
        println("Disconnected user $userId from ticket $ticketId")
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
}
