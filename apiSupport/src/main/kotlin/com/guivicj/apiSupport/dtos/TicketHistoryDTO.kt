package com.guivicj.apiSupport.dtos

import java.time.LocalDateTime

data class TicketHistoryDTO(
    val ticketId: Long,
    val fromTechnicianId: Long?,
    val toTechnicianId: Long,
    val changedAt: LocalDateTime
)