package com.guivicj.apiSupport.dtos

import com.guivicj.apiSupport.enums.ChatRole

data class MessageDTO(
    val ticketId: Long,
    val role: ChatRole,
    val content: String
)
