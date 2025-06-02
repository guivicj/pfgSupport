package org.guivicj.support.data.repository

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import org.guivicj.support.domain.model.AppStatsDTO
import org.guivicj.support.domain.model.TechStatsDTO
import org.guivicj.support.domain.model.TechnicianDTO
import org.guivicj.support.domain.repository.TechRepository

class TechRepositoryImpl(
    private val client: HttpClient
) : TechRepository {
    private val baseUrl = "http://10.0.2.2:8080/api/tech"
    override suspend fun getTechStats(id: Long): Result<TechStatsDTO> {
        return try {
            val response: TechStatsDTO = client.get("$baseUrl/stats/user/$id").body()
            Result.success(response)
        } catch (e: Exception) {
            println("Failed to fetch TechStats: ${e.message}")
            Result.failure(e)
        }
    }

    override suspend fun getTechnicians(): List<TechnicianDTO> {
        return try {
            client.get(baseUrl).body()
        } catch (e: Exception) {
            println("Failed to fetch Technicians: ${e.message}")
            emptyList()
        }
    }

    override suspend fun getAppStats(idToken: String): AppStatsDTO {
        return client.get("http://10.0.2.2:8080/api/admin/stats") {
            header("Authorization", "Bearer $idToken")
        }.body()
    }
}