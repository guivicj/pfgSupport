package com.guivicj.apiSupport.models

import com.guivicj.apiSupport.enums.StateType
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "tickets")
data class TicketModel(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    var id: Long,
    @OneToOne
    @JoinColumn(name = "userid", nullable = false)
    var userId: UserModel,
    @OneToOne
    @JoinColumn(name = "techid", nullable = false)
    var technicianId: Technician,
    @OneToOne
    @JoinColumn(name = "productid", nullable = false)
    var productId: Product,
    var description: String,
    var state: StateType,
    var openedAt: LocalDateTime,
    var inProgressAt: LocalDateTime? = null,
    var closedAt: LocalDateTime? = null,
    ) {
    @PrePersist
    fun setOpenedAtAutomatically() {
        openedAt = LocalDateTime.now()
    }
}
