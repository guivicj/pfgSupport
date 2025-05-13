package com.guivicj.apiSupport.dtos

import com.guivicj.apiSupport.enums.ChatRole
import java.time.LocalDateTime

data class MessageDTO(
    val ticketId: Long,
    val role: ChatRole,
    val content: String,
    val timestamp: LocalDateTime = LocalDateTime.now()
)
