package org.guivicj.support.domain.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import org.guivicj.support.data.model.StateType

@Serializable
data class TicketDTO(
    val ticketId: Long,
    val userId: Long,
    val technicianId: Long,
    val productId: Long,
    val description: String,
    val state: StateType,
    val openedAt: LocalDateTime? = null,
    val inProgressAt: LocalDateTime? = null,
    val closedAt: LocalDateTime? = null
)
