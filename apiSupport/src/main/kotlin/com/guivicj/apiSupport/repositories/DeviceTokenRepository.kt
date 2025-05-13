package com.guivicj.apiSupport.repositories

import com.guivicj.apiSupport.models.DeviceToken
import org.springframework.data.jpa.repository.JpaRepository

interface DeviceTokenRepository : JpaRepository<DeviceToken, Long> {
    fun findByUserId(userId: Long): List<DeviceToken>
    fun findByToken(token: String): DeviceToken?
}

