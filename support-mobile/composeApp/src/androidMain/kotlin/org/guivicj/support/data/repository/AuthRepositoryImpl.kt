package org.guivicj.support.data.repository

import com.google.firebase.auth.FirebaseAuth
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import kotlinx.coroutines.tasks.await
import kotlinx.serialization.json.Json
import org.guivicj.support.data.model.request.FirebaseLoginRequest
import org.guivicj.support.domain.model.UserSessionInfoDTO
import org.guivicj.support.domain.repository.AuthRepository

class AuthRepositoryImpl(
    private val client: HttpClient
) : AuthRepository {

    override suspend fun login(email: String, password: String): Result<UserSessionInfoDTO> {
        return try {
            val authResult = FirebaseAuth.getInstance()
                .signInWithEmailAndPassword(email, password)
                .await()

            val idToken = authResult.user?.getIdToken(true)?.await()?.token
                ?: return Result.failure(Exception("Failed to retrieve ID token"))

            val response = client.post("http://10.0.2.2:8080/api/auth/firebase-login") {
                contentType(ContentType.Application.Json)
                setBody(mapOf("token" to idToken))
            }.body<UserSessionInfoDTO>()

            Result.success(response)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun loginWithFirebase(idToken: String): Result<UserSessionInfoDTO> {
        return try {
            val response: HttpResponse =
                client.post("http://10.0.2.2:8080/api/auth/firebase-login") {
                    contentType(ContentType.Application.Json)
                    setBody(FirebaseLoginRequest(token = idToken))
                }

            if (response.status.isSuccess()) {
                val session = Json.decodeFromString<UserSessionInfoDTO>(response.bodyAsText())
                Result.success(session)
            } else {
                Result.failure(Exception("Login failed with status: ${response.status}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
