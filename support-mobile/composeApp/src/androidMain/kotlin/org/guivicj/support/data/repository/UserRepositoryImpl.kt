package org.guivicj.support.data.repository

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import org.guivicj.support.data.model.request.UserUpdateRequest
import org.guivicj.support.domain.model.UserDTO
import org.guivicj.support.domain.repository.UserRepository

class UserRepositoryImpl(private val client: HttpClient) : UserRepository {
    private val baseUrl = "http://10.0.2.2:8080/api/users"

    override suspend fun getUserById(id: Long): UserDTO {
        return client.get("$baseUrl/id/$id").body()
    }

    override suspend fun updateUser(user: UserDTO): UserDTO {
        val request = UserUpdateRequest(
            name = user.name,
            email = user.email,
            telephone = user.telephone,
            userType = user.type
        )
        return client.put("$baseUrl/update/${user.email}") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }

}