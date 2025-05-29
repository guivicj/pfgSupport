package org.guivicj.support.state

import org.guivicj.support.domain.model.TechStatsDTO

sealed class TechAnalyticsState {
    data object Loading : TechAnalyticsState()
    data class Success(
        val stats: TechStatsDTO,
    ) : TechAnalyticsState()
    data class Error(val message: String) : TechAnalyticsState()
}
