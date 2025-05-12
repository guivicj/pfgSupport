package com.guivicj.apiSupport.services

import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingException
import com.google.firebase.messaging.Message
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
    private val userRepository: UserRepository,
    private val ticketRepository: TicketRepository,
    private val techRepository: TechRepository,
    private val productRepository: ProductRepository,
    private val ticketMapper: TicketMapper,
    private val ticketHistoryRepository: TicketHistoryRepository,
    private val ticketHistoryMapper: TicketHistoryMapper,
    private val ticketMessageRepository: TicketMessageRepository,
    private val openAiService: OpenAiService,
    private val firebaseMessaging: FirebaseMessaging,
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

    fun changeState(ticketId: Long, request: ChangeStateRequest, currentUser: UserSessionInfoDTO): TicketDTO {
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

    fun sendMessage(ticketMessage: TicketMessage, currentUser: UserSessionInfoDTO): TicketMessage {
        val ticket = ticketRepository.findById(ticketMessage.ticket.id)
            .orElseThrow { RuntimeException("Ticket not found") }

        if (currentUser.user.type == UserType.USER && ticket.userId.email != currentUser.user.email) {
            throw RuntimeException("You cannot send messages to this ticket")
        }

        val technician =
            userRepository.findById(ticket.technicianId.id).orElseThrow { RuntimeException("Technician not found") }

        if (currentUser.user.type == UserType.TECHNICIAN && technician.email != currentUser.user.email) {
            throw RuntimeException("You are not assigned to this ticket")
        }

        val savedMessage = ticketMessageRepository.save(ticketMessage)
        val recipientEmail = when (currentUser.user.type) {
            UserType.USER -> technician.email
            UserType.TECHNICIAN -> ticket.userId.email
            else -> return savedMessage
        }

        val recipient = userRepository.findByEmail(recipientEmail)
            .orElseThrow { RuntimeException("Recipient not found") }

        val token = recipient.firebaseUid

        val notification = Message.builder()
            .setToken(token)
            .putData("title", "New message on Ticket #${ticket.id}")
            .putData("body", savedMessage.content)
            .build()

        try {
            firebaseMessaging.send(notification)
        } catch (e: FirebaseMessagingException) {
            e.printStackTrace()
        }
        return savedMessage
    }

    fun chatWithIA(ticketId: Long, userSession: UserSessionInfoDTO, message: String): String {
        val user = userRepository.findByEmail(userSession.user.email)
            .orElseThrow { RuntimeException("User not found") }

        val ticket = ticketRepository.findById(ticketId)
            .orElseThrow { RuntimeException("Ticket not found") }

        if (ticket.userId.id != user.id) {
            throw RuntimeException("This ticket is not yours")
        }

        ticketMessageRepository.save(
            TicketMessage(
                ticket = ticket,
                role = ChatRole.USER,
                content = message
            )
        )

        val messages = ticketMessageRepository
            .findAllByTicketIdOrderByTimestampAsc(ticketId)
            .map {
                mapOf("role" to it.role.name.lowercase(), "content" to it.content)
            }

        val response = openAiService.sendMessage(messages)

        ticketMessageRepository.save(
            TicketMessage(
                ticket = ticket,
                role = ChatRole.TECHNICIAN,
                content = response
            )
        )
        if (
            message.contains("real person", ignoreCase = true)) {
            if (ticket.technicianId.technicianType.name == "CHAT") {
                assignToAvailableHuman(ticketId, userSession)
            }
        }

        return response
    }
    fun getMessages(ticketId: Long, user: UserSessionInfoDTO): List<TicketMessage> {
        val ticket = ticketRepository.findById(ticketId)
            .orElseThrow { RuntimeException("Ticket not found") }

        val isUserTicketOwner = user.user.type == UserType.USER && ticket.userId.email == user.user.email
        val isTechnicianAssigned = user.user.type == UserType.TECHNICIAN
        val isAdmin = user.user.type == UserType.ADMIN

        if (!isUserTicketOwner && !isTechnicianAssigned && !isAdmin) {
            throw RuntimeException("You don't have access to this ticket's messages")
        }

        return ticketMessageRepository.findAllByTicketIdOrderByTimestampAsc(ticketId)
    }
}
