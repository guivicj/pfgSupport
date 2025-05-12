package org.guivicj.support.data.repository

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import org.guivicj.support.data.model.ChatRole
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

    override suspend fun getById(id: Long): TicketDTO? {
        val response = client.get("$baseUrl/id/$id")
        println(response.status)
        println(response.bodyAsText())
        return response.body()
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
        return client.put("$baseUrl/tickets/$ticketId/assign-human") {
            header("Authorization", "Bearer $idToken")
        }.body()
    }

    override suspend fun changeState(
        ticketId: Long,
        newState: String,
        technicianId: Long,
        idToken: String
    ): TicketDTO {
        return client.put("$baseUrl/tickets/$ticketId/change-state") {
            header("Authorization", "Bearer $idToken")
            contentType(ContentType.Application.Json)
            setBody(
                mapOf(
                    "state" to newState,
                    "technicianId" to technicianId
                )
            )
        }.body()
    }

    override suspend fun sendMessage(
        ticketId: Long,
        role: ChatRole,
        content: String,
        idToken: String
    ): List<MessageDTO> {
        return client.post("$baseUrl/tickets/$ticketId/messages") {
            contentType(ContentType.Application.Json)
            setBody(
                mapOf(
                    "ticketId" to ticketId,
                    "role" to role,
                    "content" to content
                )
            )
        }.body()
    }

    override suspend fun getMessages(ticketId: Long, idToken: String): List<MessageDTO> {
        return client.get("$baseUrl/tickets/$ticketId/messages") {
            header("Authorization", "Bearer $idToken")
        }.body()
    }

}