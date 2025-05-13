package com.guivicj.apiSupport.services

import com.guivicj.apiSupport.models.DeviceToken
import com.guivicj.apiSupport.repositories.DeviceTokenRepository
import org.springframework.stereotype.Service

@Service
class DeviceTokenService(
    private val deviceTokenRepository: DeviceTokenRepository
) {

    fun registerToken(userId: Long, token: String) {
        val existing = deviceTokenRepository.findByToken(token)
        if (existing != null && existing.userId != userId) {
            throw RuntimeException("Token already assigned to another user")
        }

        if (existing == null) {
            val newToken = DeviceToken(userId = userId, token = token)
            deviceTokenRepository.save(newToken)
        }
    }

    fun getTokensForUser(userId: Long): List<String> {
        return deviceTokenRepository.findByUserId(userId).map { it.token }
    }
}
