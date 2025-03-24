package com.guivicj.apiSupport.models

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
data class TicketHistory(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne @JoinColumn(name = "ticket_id")
    val ticket: TicketModel,

    @ManyToOne @JoinColumn(name = "from_technician_id")
    val fromTechnician: Technician? = null,

    @ManyToOne @JoinColumn(name = "to_technician_id")
    val toTechnician: Technician,

    val changedAt: LocalDateTime = LocalDateTime.now()
)
