package com.guivicj.apiSupport.repositories

import com.guivicj.apiSupport.models.Product
import org.springframework.data.jpa.repository.JpaRepository

interface ProductRepository: JpaRepository<Product, Long> {
    fun existsByName(name: String): Boolean
}
