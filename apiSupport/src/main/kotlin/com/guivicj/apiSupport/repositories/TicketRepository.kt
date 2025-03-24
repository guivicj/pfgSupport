package com.guivicj.apiSupport.repositories

import com.guivicj.apiSupport.enums.StateType
import com.guivicj.apiSupport.models.Technician
import com.guivicj.apiSupport.models.TicketModel
import com.guivicj.apiSupport.models.UserModel
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TicketRepository : JpaRepository<TicketModel, Long> {
    fun countByTechnicianId(technicianId: Technician): Long
    fun getTicketsByTechnicianId(technicianId: Technician): List<TicketModel>
    fun getTicketsByUserId(userId: UserModel): List<TicketModel>
    fun getTicketsByState(state: StateType): List<TicketModel>
}