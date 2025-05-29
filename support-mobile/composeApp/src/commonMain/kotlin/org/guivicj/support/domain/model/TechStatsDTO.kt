package org.guivicj.support.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class TechStatsDTO(
    val technicianId: Long,
    val totalTickets: Int,
    val totalResolved: Int,
    val totalInProgress: Int,
    val avgResolutionTime: String
)
