package com.guivicj.apiSupport.models

import com.guivicj.apiSupport.enums.UserType
import jakarta.persistence.*

@Entity
@Table(name = "users", schema = "public")
@Inheritance(strategy = InheritanceType.JOINED)
data class UserModel(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    var id: Long = 0,
    @Column(nullable = true, unique = true)
    val firebaseUid: String? = null,
    @Column(nullable = false)
    var name: String = "",
    @Column(nullable = false)
    var email: String = "",
    @Column(nullable = false)
    var password: String = "",
    @Column(nullable = false)
    var telephone: Int = 0,
    @Column(nullable = false)
    var type: UserType = UserType.USER,

)