package com.guivicj.apiSupport.mappers

import com.guivicj.apiSupport.dtos.TicketHistoryDTO
import com.guivicj.apiSupport.models.TicketHistory
import org.mapstruct.Mapper
import org.mapstruct.Mapping

@Mapper(componentModel = "spring")
interface TicketHistoryMapper {

    @Mapping(source = "ticket.id", target = "ticketId")
    @Mapping(source = "fromTechnician.id", target = "fromTechnicianId")
    @Mapping(source = "toTechnician.id", target = "toTechnicianId")
    fun toDto(history: TicketHistory): TicketHistoryDTO

    fun toDtoList(historyList: List<TicketHistory>): List<TicketHistoryDTO>
}
