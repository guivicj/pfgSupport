package com.guivicj.apiSupport.services

import com.guivicj.apiSupport.mappers.TicketMapper
import com.guivicj.apiSupport.repositories.TicketRepository
import org.springframework.stereotype.Service

@Service
class TicketService (
    val ticketRepository: TicketRepository,
    val ticketMapper: TicketMapper
){

}