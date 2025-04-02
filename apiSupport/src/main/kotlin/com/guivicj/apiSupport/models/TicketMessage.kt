package com.guivicj.apiSupport.models

import com.guivicj.apiSupport.enums.ChatRole
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
data class TicketMessage(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne
    @JoinColumn(name = "ticket_id", nullable = false)
    val ticket: TicketModel,

    @Column(nullable = false)
    val role: ChatRole,

    @Column(nullable = false, columnDefinition = "TEXT")
    val content: String,

    val timestamp: LocalDateTime = LocalDateTime.now()
)
