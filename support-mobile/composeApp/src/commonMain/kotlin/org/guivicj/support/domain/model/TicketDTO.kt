package org.guivicj.support.domain.model

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
    val openedAt: String? = null,
    val inProgressAt: String? = null,
    val closedAt: String? = null
)
