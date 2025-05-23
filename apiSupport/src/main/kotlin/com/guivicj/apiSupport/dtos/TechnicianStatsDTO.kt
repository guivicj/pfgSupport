package com.guivicj.apiSupport.dtos

data class TechnicianStatsDTO(
    val technicianId: Long,
    val totalTickets: Int,
    val totalResolved: Int,
    val totalInProgress: Int,
    val avgResolutionTime: String
)
