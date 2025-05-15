package com.guivicj.apiSupport.services

import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingException
import com.google.firebase.messaging.Message
import com.guivicj.apiSupport.dtos.MessageDTO
import com.guivicj.apiSupport.dtos.TicketDTO
import com.guivicj.apiSupport.dtos.TicketHistoryDTO
import com.guivicj.apiSupport.dtos.requests.ChangeStateRequest
import com.guivicj.apiSupport.dtos.responses.UserSessionInfoDTO
import com.guivicj.apiSupport.enums.ChatRole
import com.guivicj.apiSupport.enums.StateType
import com.guivicj.apiSupport.enums.TechnicianType
import com.guivicj.apiSupport.enums.UserType
import com.guivicj.apiSupport.mappers.TicketHistoryMapper
import com.guivicj.apiSupport.mappers.TicketMapper
import com.guivicj.apiSupport.models.TicketHistory
import com.guivicj.apiSupport.models.TicketMessage
import com.guivicj.apiSupport.models.TicketModel
import com.guivicj.apiSupport.repositories.*
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class TicketService(
    private val ticketRepository: TicketRepository,
    private val userRepository: UserRepository,
    private val productRepository: ProductRepository,
    private val techRepository: TechRepository,
    private val ticketMapper: TicketMapper,
    private val ticketHistoryMapper: TicketHistoryMapper,
    private val ticketHistoryRepository: TicketHistoryRepository
) {
    fun toEntity(dto: TicketDTO): TicketModel {
        val user = userRepository.findById(dto.userId)
            .orElseThrow { RuntimeException("User not found") }

        val technician = techRepository.findById(dto.technicianId)
            .orElseThrow { RuntimeException("Technician not found") }

        val product = productRepository.findById(dto.productId)
            .orElseThrow { RuntimeException("Product not found") }

        val ticket = TicketModel(
            id = dto.ticketId,
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

    fun createTicket(currentUser: UserSessionInfoDTO, dto: TicketDTO): TicketDTO {
        val userEmail = currentUser.user.email

        val user = userRepository.findByEmail(userEmail)
            .orElseThrow { RuntimeException("User not found") }

        val chatTech = techRepository.findAll().find {
            it.technicianType == TechnicianType.CHAT
        } ?: throw RuntimeException("No AI technician available")

        val ticket = TicketModel(
            id = 0,
            userId = user,
            technicianId = chatTech,
            productId = productRepository.findById(dto.productId)
                .orElseThrow { RuntimeException("Product not found") },
            description = dto.description,
            state = StateType.OPEN,
            openedAt = LocalDateTime.now()
        )

        val saved = ticketRepository.save(ticket)
        return ticketMapper.toDTO(saved)
    }

    fun assignToAvailableHuman(ticketId: Long, currentUser: UserSessionInfoDTO): TicketDTO {
        val ticket = ticketRepository.findById(ticketId)
            .orElseThrow { RuntimeException("Ticket not found") }

        if (ticket.userId.email != currentUser.user.email) {
            throw RuntimeException("You can only escalate your own tickets")
        }

        if (ticket.technicianId.technicianType != TechnicianType.CHAT) {
            throw RuntimeException("Ticket already assigned to a human technician")
        }

        val techWithLeastTickets = techRepository.findAll()
            .filter { it.technicianType != TechnicianType.CHAT }
            .minByOrNull { ticketRepository.countByTechnicianId(it) }
            ?: throw RuntimeException("No human technicians available")

        ticket.technicianId = techWithLeastTickets
        ticket.inProgressAt = LocalDateTime.now()
        ticket.state = StateType.IN_PROGRESS

        return ticketMapper.toDTO(ticketRepository.save(ticket))
    }

    fun escalateTicket(ticketId: Long, newTechnicianId: Long, currentUser: UserSessionInfoDTO): TicketDTO {
        val ticket = ticketRepository.findById(ticketId)
            .orElseThrow { RuntimeException("Ticket not found") }

        if (ticket.userId.email != currentUser.user.email &&
            currentUser.user.type != UserType.ADMIN && currentUser.user.type != UserType.TECHNICIAN
        ) {
            throw RuntimeException("You can't escalate this ticket")
        }

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

        if (request.state == StateType.IN_PROGRESS && ticket.inProgressAt == null)
            ticket.inProgressAt = LocalDateTime.now()

        if (request.state == StateType.CLOSED && ticket.closedAt == null)
            ticket.closedAt = LocalDateTime.now()

        request.technicianId.let {
            val newTech = techRepository.findById(it)
                .orElseThrow { RuntimeException("Technician not found") }
            ticket.technicianId = newTech
        }

        ticket.state = request.state
        return ticketMapper.toDTO(ticketRepository.save(ticket))
    }

    fun getTicketHistory(ticketId: Long): List<TicketHistoryDTO> {
        val history = ticketHistoryRepository.findByTicketId(ticketId)
        return ticketHistoryMapper.toDtoList(history)
    }
}
