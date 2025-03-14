package com.guivicj.apiSupport.models

import com.guivicj.apiSupport.enums.TechnicianType
import jakarta.persistence.*
import org.hibernate.annotations.IdGeneratorType

@Entity
@Table(name = "technician")
data class Technician(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    var id: Long = 0,
    @OneToOne
    @JoinColumn(name = "userid", nullable = false, unique = true)
    var userModel: UserModel,
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var technicianType: TechnicianType
)
