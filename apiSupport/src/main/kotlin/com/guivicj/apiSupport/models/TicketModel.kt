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
    @JoinColumn(name = "userid", nullable = false, unique = true)
    var userModel: UserModel,
    @OneToOne
    @JoinColumn(name = "techid", nullable = false, unique = true)
    var technician: Technician,
    @OneToOne
    @JoinColumn(name = "productid", nullable = false, unique = true)
    var product: Product,
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
