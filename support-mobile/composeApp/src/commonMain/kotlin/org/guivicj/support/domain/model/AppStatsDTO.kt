package org.guivicj.support.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class AppStatsDTO(
    val opened: Int,
    val inProgress: Int,
    val closed: Int,
    val avgResolutionTimeMinutes: Long,
    val totalTechnicians: Int
)
