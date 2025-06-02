package com.guivicj.apiSupport.repositories

import com.guivicj.apiSupport.enums.TechnicianType
import com.guivicj.apiSupport.models.Technician
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TechRepository: JpaRepository<Technician, Long> {
    fun getTechsByTechnicianType(technicianType: TechnicianType): List<Technician>
    fun findByUserModel_Id(userId: Long): Technician?
    fun findByTechnicianType(technicianType: TechnicianType): List<Technician>
}
