package com.guivicj.apiSupport.models

import com.guivicj.apiSupport.enums.ProductType
import jakarta.persistence.*

@Entity
@Table(name = "products")
data class Product(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    var id: Long,
    @Column(nullable = false)
    var name: String,
    @Column(nullable = false)
    var type: ProductType,
    var description: String,
)
