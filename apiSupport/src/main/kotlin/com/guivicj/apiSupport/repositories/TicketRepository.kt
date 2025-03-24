package com.guivicj.apiSupport.repositories

import com.guivicj.apiSupport.models.TicketModel
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TicketRepository: JpaRepository<TicketModel, Long> {
}