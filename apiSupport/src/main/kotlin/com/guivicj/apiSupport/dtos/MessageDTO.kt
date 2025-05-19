package com.guivicj.apiSupport.dtos

import com.guivicj.apiSupport.enums.ChatRole

data class MessageDTO(
    val ticketId: Long? = 0,
    val role: ChatRole? = ChatRole.USER,
    val content: String? = "",
)
