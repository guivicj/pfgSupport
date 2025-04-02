package com.guivicj.apiSupport.controllers

import com.guivicj.apiSupport.annotations.CurrentUser
import com.guivicj.apiSupport.dtos.TicketDTO
import com.guivicj.apiSupport.dtos.TicketHistoryDTO
import com.guivicj.apiSupport.dtos.requests.ChangeStateRequest
import com.guivicj.apiSupport.dtos.requests.ChatRequest
import com.guivicj.apiSupport.dtos.requests.EscalateTicketRequest
import com.guivicj.apiSupport.dtos.responses.ChatResponse
import com.guivicj.apiSupport.dtos.responses.UserSessionInfoDTO
import com.guivicj.apiSupport.enums.StateType
import com.guivicj.apiSupport.services.TicketService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/tickets")
class TicketController(val ticketService: TicketService) {
    @GetMapping
    fun getTickets(): List<TicketDTO> {
        return ticketService.getAllTickets()
    }

    @GetMapping("id/{id}")
    fun getTicketById(@PathVariable("id") id: Long): TicketDTO {
        return ticketService.getTicketById(id)
    }

    @GetMapping("/{ticketId}/history")
    fun getTicketHistory(@PathVariable ticketId: Long): ResponseEntity<List<TicketHistoryDTO>> {
        return ResponseEntity.ok(ticketService.getTicketHistory(ticketId))
    }

    @GetMapping("/by-technician/{techId}")
    fun getTicketByTechnician(@PathVariable techId: Long): ResponseEntity<List<TicketDTO>> {
        val tickets = ticketService.getTicketByTechnician(techId)
        return ResponseEntity.ok(tickets)
    }

    @GetMapping("/by-user/{userId}")
    fun getTicketByUserId(@PathVariable userId: Long): ResponseEntity<List<TicketDTO>> {
        val tickets = ticketService.getTicketByUser(userId)
        return ResponseEntity.ok(tickets)
    }

    @GetMapping("/by-state/{state}")
    fun getTicketByState(@PathVariable state: StateType): ResponseEntity<List<TicketDTO>> {
        val tickets = ticketService.getTicketByState(state)
        return ResponseEntity.ok(tickets)
    }

    @PostMapping
    fun createTicket(@CurrentUser user: UserSessionInfoDTO, @RequestBody dto: TicketDTO): ResponseEntity<TicketDTO> {
        val createdTicket = ticketService.createTicket(user, dto)
        return ResponseEntity.ok(createdTicket)
    }

    @PutMapping("/tickets/{ticketId}/assign-human")
    fun assignHumanTech(
        @CurrentUser user: UserSessionInfoDTO,
        @PathVariable ticketId: Long
    ): ResponseEntity<TicketDTO> {
        val reassignedTicket = ticketService.assignToAvailableHuman(ticketId, user)
        return ResponseEntity.ok(reassignedTicket)
    }

    @PutMapping("/tickets/{ticketId}/escalate")
    fun escalateTicket(
        @CurrentUser user: UserSessionInfoDTO,
        @PathVariable ticketId: Long,
        @RequestBody request: EscalateTicketRequest
    ): ResponseEntity<TicketDTO> {
        val updatedTicket = ticketService.escalateTicket(ticketId, request.newTechnicianId, user)
        return ResponseEntity.ok(updatedTicket)
    }

    @PutMapping("/tickets/{ticketId}/change-state")
    fun changeState(
        @CurrentUser user: UserSessionInfoDTO,
        @PathVariable ticketId: Long,
        @RequestBody request: ChangeStateRequest
    ): ResponseEntity<TicketDTO> {
        val updatedTicket = ticketService.changeState(ticketId, request, user)
        return ResponseEntity.ok(updatedTicket)
    }

    @PostMapping("/{ticketId}/chat")
    fun chatWithIA(
        @PathVariable ticketId: Long,
        @RequestBody request: ChatRequest,
        @CurrentUser userSession: UserSessionInfoDTO
    ): ResponseEntity<ChatResponse> {
        val response = ticketService.chatWithIA(ticketId, userSession, request.message)
        return ResponseEntity.ok(ChatResponse(response))
    }
}
