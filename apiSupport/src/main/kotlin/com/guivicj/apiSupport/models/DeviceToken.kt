package com.guivicj.apiSupport.models

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
data class DeviceToken(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    val userId: Long,

    @Column(nullable = false, unique = true)
    val token: String,

    val createdAt: LocalDateTime = LocalDateTime.now()
)
