package com.guivicj.apiSupport.repositories

import com.guivicj.apiSupport.models.Admin
import org.springframework.data.jpa.repository.JpaRepository

interface AdminRepository: JpaRepository<Admin, Long> {
    fun getAdminByUserId(userId: Long): Admin
}