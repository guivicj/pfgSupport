package org.guivicj.support.domain.model

import org.guivicj.support.data.model.ChatRole

data class MessageDTO(
    val ticketId: Long,
    val role: ChatRole,
    val content: String
)
