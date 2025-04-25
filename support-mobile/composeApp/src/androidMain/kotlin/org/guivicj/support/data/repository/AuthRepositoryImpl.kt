package org.guivicj.support.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
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
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            Result.failure(IllegalArgumentException("ERROR: Wrong email or password"))
        } catch (e: Exception) {
            Result.failure(Exception("GENERAL_ERROR: ${e.message}"))
        }
    }

    override suspend fun register(
        id: Long,
        name: String,
        email: String,
        password: String,
        telephone: Int
    ): Result<UserSessionInfoDTO> {
        return try {
            val authResult = FirebaseAuth.getInstance()
                .createUserWithEmailAndPassword(email, password)
                .await()

            val idToken = authResult.user?.getIdToken(true)?.await()?.token
                ?: return Result.failure(Exception("Failed to retrieve ID token"))

            val request = FirebaseLoginRequest(
                token = idToken,
                name = name,
                telephone = telephone
            )

            val response = client.post("http://10.0.2.2:8080/api/auth/firebase-login") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }.body<UserSessionInfoDTO>()

            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun loginWithFirebase(idToken: String): Result<UserSessionInfoDTO> {
        return try {
            val request = FirebaseLoginRequest(token = idToken, name = null, telephone = null)
            val response: HttpResponse =
                client.post("http://10.0.2.2:8080/api/auth/firebase-login") {
                    contentType(ContentType.Application.Json)
                    setBody(request)
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
