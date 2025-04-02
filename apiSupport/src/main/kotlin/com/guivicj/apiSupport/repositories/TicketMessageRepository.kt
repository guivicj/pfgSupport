package com.guivicj.apiSupport.repositories

import com.guivicj.apiSupport.models.TicketMessage
import org.springframework.data.jpa.repository.JpaRepository

interface TicketMessageRepository : JpaRepository<TicketMessage, Long> {
    fun findAllByTicketIdOrderByTimestampAsc(ticketId: Long): List<TicketMessage>
}

