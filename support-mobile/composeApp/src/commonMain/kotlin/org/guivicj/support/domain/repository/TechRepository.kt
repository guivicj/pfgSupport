package org.guivicj.support.domain.repository

import org.guivicj.support.domain.model.TechStatsDTO

interface TechRepository {
    suspend fun getTechStats(id: Long): Result<TechStatsDTO>
}