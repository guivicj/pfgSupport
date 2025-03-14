package com.guivicj.apiSupport.models

import jakarta.persistence.*

@Entity
@Table(name = "admin")
data class Admin(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,
    @OneToOne
    @JoinColumn(name = "userid", nullable = false, unique = true)
    var user: UserModel
)
