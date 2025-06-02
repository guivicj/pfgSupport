package org.guivicj.support.data.repository

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.*
import io.ktor.http.ContentType
import io.ktor.http.contentType
import org.guivicj.support.data.model.ChatRole
import org.guivicj.support.data.model.TechnicianType
import org.guivicj.support.data.model.request.ChangeStateRequest
import org.guivicj.support.data.model.request.EscalateByTypeRequest
import org.guivicj.support.domain.model.MessageDTO
import org.guivicj.support.domain.model.TicketDTO
import org.guivicj.support.domain.repository.TicketRepository

class TicketRepositoryImpl(
    private val client: HttpClient
) : TicketRepository {
    private val baseUrl = "http://10.0.2.2:8080/api/tickets"

    override suspend fun getAll(): List<TicketDTO> {
        return client.get(baseUrl).body()
    }

    override suspend fun getById(id: Long): TicketDTO {
        return client.get("$baseUrl/id/$id").body()
    }

    override suspend fun getByUser(userId: Long): List<TicketDTO> {
        return client.get("$baseUrl/by-user/$userId").body()
    }

    override suspend fun getByTechnician(techId: Long): List<TicketDTO> {
        return client.get("$baseUrl/by-technician/$techId").body()
    }

    override suspend fun create(ticket: TicketDTO, idToken: String): TicketDTO {
        return client.post(baseUrl) {
            header("Authorization", "Bearer $idToken")
            contentType(ContentType.Application.Json)
            setBody(ticket)
        }.body()
    }

    override suspend fun assignToHuman(ticketId: Long, idToken: String): TicketDTO {
        return client.put("$baseUrl/$ticketId/assign-human") {
            header("Authorization", "Bearer $idToken")
        }.body()
    }

    override suspend fun changeState(
        ticketId: Long,
        newState: String,
        technicianId: Long,
        idToken: String
    ): TicketDTO {
        return client.put("$baseUrl/$ticketId/change-state") {
            header("Authorization", "Bearer $idToken")
            contentType(ContentType.Application.Json)
            setBody(ChangeStateRequest(state = newState, technicianId = technicianId))
        }.body()
    }

    override suspend fun sendMessage(
        ticketId: Long,
        role: ChatRole,
        content: String,
        idToken: String
    ): MessageDTO {
        return client.post("$baseUrl/$ticketId/send") {
            header("Authorization", "Bearer $idToken")
            contentType(ContentType.Application.Json)
            setBody(MessageDTO(ticketId = ticketId, role = role, content = content))
        }.body()
    }

    override suspend fun getMessages(ticketId: Long, idToken: String): List<MessageDTO> {
        return client.get("$baseUrl/$ticketId/messages") {
            header("Authorization", "Bearer $idToken")
        }.body()
    }

    override suspend fun changeTicketState(
        ticketId: Long,
        newState: String,
        technicianId: Long,
        idToken: String
    ): TicketDTO {
        return client.put("$baseUrl/$ticketId/change-state") {
            header("Authorization", "Bearer $idToken")
            contentType(ContentType.Application.Json)
            setBody(ChangeStateRequest(state = newState, technicianId = technicianId))
        }.body()
    }

    override suspend fun escalateByType(
        ticketId: Long,
        technicianType: TechnicianType,
        token: String
    ) {
        client.put("$baseUrl/$ticketId/escalate-by-type") {
            header("Authorization", "Bearer $token")
            contentType(ContentType.Application.Json)
            setBody(EscalateByTypeRequest(technicianType))
        }
    }
}