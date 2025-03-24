package com.guivicj.apiSupport.services

import com.guivicj.apiSupport.dtos.TicketDTO
import com.guivicj.apiSupport.dtos.TicketHistoryDTO
import com.guivicj.apiSupport.dtos.requests.ChangeStateRequest
import com.guivicj.apiSupport.enums.StateType
import com.guivicj.apiSupport.enums.TechnicianType
import com.guivicj.apiSupport.mappers.TechMapper
import com.guivicj.apiSupport.mappers.TicketHistoryMapper
import com.guivicj.apiSupport.mappers.TicketMapper
import com.guivicj.apiSupport.models.TicketHistory
import com.guivicj.apiSupport.models.TicketModel
import com.guivicj.apiSupport.repositories.*
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class TicketService(
    private val userRepository: UserRepository,
    private val ticketRepository: TicketRepository,
    private val techRepository: TechRepository,
    private val productRepository: ProductRepository,
    private val techMapper: TechMapper,
    private val ticketMapper: TicketMapper,
    private val ticketHistoryRepository: TicketHistoryRepository,
    private val ticketHistoryMapper: TicketHistoryMapper,

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

    fun getTicketByTechnician(techId: Long): List<TicketDTO> {
        val tech = techRepository.findById(techId)
            .orElseThrow { RuntimeException("Technician not found") }
        return ticketRepository.getTicketsByTechnicianId(tech).map { ticketMapper.toDTO(it) }
    }

    fun getTicketByUser(userId: Long): List<TicketDTO> {
        val user = userRepository.findById(userId)
            .orElseThrow { RuntimeException("User not found") }
        return ticketRepository.getTicketsByUserId(user).map { ticketMapper.toDTO(it) }
    }

    fun getTicketByState(state: StateType): List<TicketDTO> {
        return ticketRepository.getTicketsByState(state).map { ticketMapper.toDTO(it) }
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

        val techs = techRepository.findAll().filter { it.technicianType != TechnicianType.CHAT }

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

        val history = TicketHistory(
            ticket = ticket,
            fromTechnician = ticket.technicianId,
            toTechnician = newTechnician
        )
        ticket.technicianId = newTechnician

        ticketHistoryRepository.save(history)

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

    fun getTicketHistory(ticketId: Long): List<TicketHistoryDTO> {
        val history = ticketHistoryRepository.findByTicketId(ticketId)
        return ticketHistoryMapper.toDtoList(history)
    }

}