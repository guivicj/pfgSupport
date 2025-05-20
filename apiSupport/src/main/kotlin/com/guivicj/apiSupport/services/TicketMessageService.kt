package com.guivicj.apiSupport.services

import com.guivicj.apiSupport.dtos.MessageDTO
import com.guivicj.apiSupport.dtos.responses.UserSessionInfoDTO
import com.guivicj.apiSupport.enums.ChatRole
import com.guivicj.apiSupport.enums.UserType
import com.guivicj.apiSupport.models.TicketMessage
import com.guivicj.apiSupport.repositories.TicketMessageRepository
import com.guivicj.apiSupport.repositories.TicketRepository
import com.guivicj.apiSupport.repositories.UserRepository
import com.guivicj.apiSupport.websocket.ChatWebSocketHandler
import org.springframework.stereotype.Service

@Service
class TicketMessageService(
    private val ticketRepository: TicketRepository,
    private val userRepository: UserRepository,
    private val ticketMessageRepository: TicketMessageRepository,
    private val notificationService: NotificationService,
    private val openAiService: OpenAiService,
    private val ticketService: TicketService,
    private val chatWebSocketHandler: ChatWebSocketHandler
) {

    fun sendMessage(ticketMessage: TicketMessage, currentUser: UserSessionInfoDTO): TicketMessage {
        val ticket = ticketRepository.findById(ticketMessage.ticket.id)
            .orElseThrow { RuntimeException("Ticket not found") }

        if (currentUser.user.type == UserType.USER && ticket.userId.email != currentUser.user.email) {
            throw RuntimeException("You cannot send messages to this ticket")
        }

        val technician = userRepository.findById(ticket.technicianId.userModel.id)
            .orElseThrow { RuntimeException("Technician not found") }

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

        try {
            if (!chatWebSocketHandler.isUserConnected(recipient.id)) {
                notificationService.sendNotificationToUser(
                    userId = recipient.id,
                    title = "New message on Ticket #${ticket.id}",
                    message = savedMessage.content
                )
            } else {
                println("User ${recipient.id} is connected via WebSocket. No FCM sent.")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return savedMessage
    }

    fun sendMessageFromDTO(request: MessageDTO, currentUser: UserSessionInfoDTO): MessageDTO {
        val ticket = ticketRepository.findById(request.ticketId!!)
            .orElseThrow { RuntimeException("Ticket not found") }

        val message = TicketMessage(
            ticket = ticket,
            role = request.role!!,
            content = request.content!!
        )

        val saved = sendMessage(message, currentUser)

        return MessageDTO(
            ticketId = saved.ticket.id,
            role = saved.role,
            content = saved.content,
        )
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
                ticketService.assignToAvailableHuman(ticketId, userSession)
            }
        }

        return response
    }

    fun getMessages(ticketId: Long, user: UserSessionInfoDTO): List<MessageDTO> {
        val ticket = ticketRepository.findById(ticketId)
            .orElseThrow { RuntimeException("Ticket not found") }

        val isUserTicketOwner = user.user.type == UserType.USER && ticket.userId.email == user.user.email
        val isTechnicianAssigned = user.user.type == UserType.TECHNICIAN
        val isAdmin = user.user.type == UserType.ADMIN

        if (!isUserTicketOwner && !isTechnicianAssigned && !isAdmin) {
            throw RuntimeException("You don't have access to this ticket's messages")
        }

        return ticketMessageRepository.findAllByTicketIdOrderByTimestampAsc(ticketId)
            .map {
                MessageDTO(
                    ticketId = it.ticket.id,
                    role = it.role,
                    content = it.content,
                )
            }
    }
}
