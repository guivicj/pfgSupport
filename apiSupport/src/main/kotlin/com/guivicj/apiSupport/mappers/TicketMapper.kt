package com.guivicj.apiSupport.mappers

import com.guivicj.apiSupport.dtos.TicketDTO
import com.guivicj.apiSupport.models.TicketModel
import org.mapstruct.Mapper
import org.mapstruct.Mapping


@Mapper(componentModel = "spring")
interface TicketMapper {
    @Mapping(source = "userModel.id", target = "userId")
    @Mapping(source = "technician.id", target = "technicianId")
    @Mapping(source = "product.id", target = "productId")
    fun toDTO(ticket: TicketModel): TicketDTO
}
