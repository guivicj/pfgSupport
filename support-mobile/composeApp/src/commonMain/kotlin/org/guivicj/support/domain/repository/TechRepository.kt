package org.guivicj.support.domain.repository

import org.guivicj.support.domain.model.AppStatsDTO
import org.guivicj.support.domain.model.TechStatsDTO
import org.guivicj.support.domain.model.TechnicianDTO

interface TechRepository {
    suspend fun getTechStats(id: Long): Result<TechStatsDTO>
    suspend fun getTechnicians(): List<TechnicianDTO>
    suspend fun getAppStats(idToken: String): AppStatsDTO
}