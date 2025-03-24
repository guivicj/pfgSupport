package com.guivicj.apiSupport.dtos

import com.guivicj.apiSupport.enums.StateType
import java.time.LocalDateTime

data class TicketDTO(
    var userId: Long,
    val technicianId: Long,
    val productId: Long,
    val description: String,
    val state: StateType,
    val openedAt: LocalDateTime? = null,
    val inProgressAt: LocalDateTime? = null,
    val closedAt: LocalDateTime? = null
    )
