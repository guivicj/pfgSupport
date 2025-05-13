package org.guivicj.support.domain.model

import kotlinx.serialization.Serializable
import org.guivicj.support.data.model.ChatRole

@Serializable
data class MessageDTO(
    val ticketId: Long,
    val role: ChatRole,
    val content: String
)
