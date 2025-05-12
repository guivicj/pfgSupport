package org.guivicj.support.domain.repository

import org.guivicj.support.data.model.ChatRole
import org.guivicj.support.domain.model.MessageDTO
import org.guivicj.support.domain.model.TicketDTO

interface TicketRepository {
    suspend fun getAll(): List<TicketDTO>
    suspend fun getById(id: Long): TicketDTO?
    suspend fun getByUser(userId: Long): List<TicketDTO>
    suspend fun getByTechnician(techId: Long): List<TicketDTO>
    suspend fun create(ticket: TicketDTO, idToken: String): TicketDTO
    suspend fun assignToHuman(ticketId: Long, idToken: String): TicketDTO
    suspend fun changeState(ticketId: Long, newState: String, technicianId: Long, idToken: String): TicketDTO
    suspend fun sendMessage(ticketId: Long, role: ChatRole, content: String, idToken: String): List<MessageDTO>
    suspend fun getMessages(ticketId: Long, idToken: String): List<MessageDTO>
}