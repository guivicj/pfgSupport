package com.guivicj.apiSupport.services

import com.guivicj.apiSupport.dtos.TicketDTO
import com.guivicj.apiSupport.dtos.requests.ChangeStateRequest
import com.guivicj.apiSupport.enums.StateType
import com.guivicj.apiSupport.enums.TechnicianType
import com.guivicj.apiSupport.mappers.TicketMapper
import com.guivicj.apiSupport.models.TicketModel
import com.guivicj.apiSupport.repositories.ProductRepository
import com.guivicj.apiSupport.repositories.TechRepository
import com.guivicj.apiSupport.repositories.TicketRepository
import com.guivicj.apiSupport.repositories.UserRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class TicketService(
    private val userRepository: UserRepository,
    private val ticketRepository: TicketRepository,
    private val techRepository: TechRepository,
    private val productRepository: ProductRepository,
    private val ticketMapper: TicketMapper
) {
    fun toEntity(dto: TicketDTO): TicketModel {
        val user = userRepository.findById(dto.userId)
            .orElseThrow { RuntimeException("User not found") }

        val technician = techRepository.findById(dto.technicianId)
            .orElseThrow { RuntimeException("Technician not found") }

        val product = productRepository.findById(dto.productId)
            .orElseThrow { RuntimeException("Product not found") }

        val ticket = TicketModel(
            id = 0,
            userId = user,
            technicianId = technician,
            productId = product,
            description = dto.description,
            state = dto.state,
            openedAt = LocalDateTime.now(),
            inProgressAt = dto.inProgressAt,
            closedAt = dto.closedAt
        )

        val saved = ticketRepository.save(ticket)
        return saved
    }

    fun getAllTickets(): List<TicketDTO> {
        return ticketRepository.findAll().map { ticketMapper.toDTO(it) }
    }

    fun getTicketById(ticketId: Long): TicketDTO {
        val ticket = (ticketRepository.findById(ticketId))
            .orElseThrow { RuntimeException("Ticket not found") }
        return ticketMapper.toDTO(ticket)
    }

    fun createTicket(ticket: TicketDTO): TicketDTO {
        if (!userRepository.existsById(ticket.userId)) {
            throw RuntimeException("User not found")
        }

        val entity = toEntity(ticket)
        val saved = ticketRepository.save(entity)
        return ticketMapper.toDTO(saved)
    }

    fun assignToAvailableHuman(ticketId: Long): TicketDTO {
        val ticket = ticketRepository.findById(ticketId).orElseThrow { RuntimeException("Ticket not found") }

        val techs = techRepository.getTechsByTechnicianType(TechnicianType.CHAT)

        val techWithLeastTickets = techs.minByOrNull {
            ticketRepository.countByTechnicianId(it)
        } ?: throw RuntimeException("No technics available")

        ticket.technicianId = techWithLeastTickets
        return ticketMapper.toDTO(ticketRepository.save(ticket))
    }

    fun escalateTicket(ticketId: Long, newTechnicianId: Long): TicketDTO {
        val ticket = ticketRepository.findById(ticketId)
            .orElseThrow { RuntimeException("Ticket not found") }

        val newTechnician = techRepository.findById(newTechnicianId)
            .orElseThrow { RuntimeException("Technician not found") }

        ticket.technicianId = newTechnician

        return ticketMapper.toDTO(ticketRepository.save(ticket))
    }

    fun changeState(ticketId: Long, request: ChangeStateRequest): TicketDTO {
        val ticket = ticketRepository.findById(ticketId)
            .orElseThrow { RuntimeException("Ticket not found") }

        when (request.state) {
            StateType.IN_PROGRESS -> {
                if (ticket.inProgressAt == null) {
                    ticket.inProgressAt = LocalDateTime.now()
                }
            }
            StateType.CLOSED -> {
                if (ticket.closedAt == null) {
                    ticket.closedAt = LocalDateTime.now()
                }
            }
            else -> {}
        }

        ticket.state = request.state
        ticket.technicianId = techRepository.findById(request.technicianId)
            .orElseThrow()

        return ticketMapper.toDTO(ticketRepository.save(ticket))
    }

}