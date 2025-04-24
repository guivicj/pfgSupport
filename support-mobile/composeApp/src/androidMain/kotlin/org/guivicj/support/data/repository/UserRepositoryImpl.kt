package org.guivicj.support.data.repository

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import org.guivicj.support.domain.model.UserDTO
import org.guivicj.support.domain.repository.UserRepository

class UserRepositoryImpl(private val client: HttpClient) : UserRepository {
    private val baseUrl = "http://10.0.2.2:8080/api/users"

    override suspend fun getUserById(id: Long): UserDTO {
        return client.get("$baseUrl/id/$id").body()
    }

}