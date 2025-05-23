package org.guivicj.support.domain.repository

import org.guivicj.support.domain.model.UserSessionInfoDTO

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<UserSessionInfoDTO>

    suspend fun register(
        id: Long,
        name: String,
        email: String,
        password: String,
        telephone: Int
    ): Result<UserSessionInfoDTO>

    suspend fun loginWithFirebase(idToken: String): Result<UserSessionInfoDTO>
    suspend fun saveFcmToken(userId: Long, fcmToken: String)
    suspend fun sendResetEmail(email: String): Result<Unit>
}
