package com.guivicj.apiSupport.services

import org.springframework.stereotype.Service
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.TextMessage
import java.util.concurrent.ConcurrentHashMap

@Service
class ChatBroadcastService {
    private val sessionsByTicket = ConcurrentHashMap<Long, MutableSet<WebSocketSession>>()

    fun register(ticketId: Long, session: WebSocketSession) {
        sessionsByTicket.computeIfAbsent(ticketId) { mutableSetOf() }.add(session)
    }

    fun remove(ticketId: Long, session: WebSocketSession) {
        sessionsByTicket[ticketId]?.remove(session)
    }

    fun broadcast(ticketId: Long, messageJson: String) {
        sessionsByTicket[ticketId]?.forEach { session ->
            if (session.isOpen) {
                session.sendMessage(TextMessage(messageJson))
            }
        }
    }
}
