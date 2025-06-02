package com.guivicj.apiSupport.dtos

data class AppStatsDTO(
    val opened: Int,
    val inProgress: Int,
    val closed: Int,
    val avgResolutionTimeMinutes: Long,
    val totalTechnicians: Int
)
