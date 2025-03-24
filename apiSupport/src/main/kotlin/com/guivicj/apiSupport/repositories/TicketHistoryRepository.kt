package com.guivicj.apiSupport.repositories

import com.guivicj.apiSupport.models.TicketHistory
import org.springframework.data.jpa.repository.JpaRepository

interface TicketHistoryRepository : JpaRepository<TicketHistory, Long> {
    fun findByTicketId(ticketId: Long): List<TicketHistory>
}
