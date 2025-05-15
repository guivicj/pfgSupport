package com.guivicj.apiSupport.services

import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message
import com.google.firebase.messaging.Notification
import com.guivicj.apiSupport.repositories.DeviceTokenRepository
import org.springframework.stereotype.Service

@Service
class NotificationService(
    private val deviceTokenService: DeviceTokenService,
    private val deviceTokenRepository: DeviceTokenRepository
) {
    fun sendNotificationToUser(userId: Long, title: String, message: String) {
        val tokens = deviceTokenRepository.findByUserId(userId).map { it.token }

        tokens.forEach { token ->
            val message = Message.builder()
                .setToken(token)
                .setNotification(
                    Notification.builder()
                        .setTitle(title)
                        .setTitle(message)
                        .build()
                ).build()

            FirebaseMessaging.getInstance().send(message)
        }
    }
}
