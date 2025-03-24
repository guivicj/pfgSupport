package com.guivicj.apiSupport.controllers

import com.guivicj.apiSupport.dtos.TicketDTO
import com.guivicj.apiSupport.dtos.TicketHistoryDTO
import com.guivicj.apiSupport.dtos.requests.ChangeStateRequest
import com.guivicj.apiSupport.dtos.requests.EscalateTicketRequest
import com.guivicj.apiSupport.models.TicketHistory
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

    @PostMapping
    fun createTicket(@RequestBody request: TicketDTO): ResponseEntity<TicketDTO> {
        val createdTicket = ticketService.createTicket(request)
        return ResponseEntity.ok(createdTicket)
    }

    @PutMapping("/tickets/{ticketId}/assign-human")
    fun assignHumanTech(@PathVariable ticketId: Long): ResponseEntity<TicketDTO> {
        val reassignedTicket = ticketService.assignToAvailableHuman(ticketId)
        return ResponseEntity.ok(reassignedTicket)
    }

    @PutMapping("/tickets/{ticketId}/escalate")
    fun escalateTicket(
        @PathVariable ticketId: Long, @RequestBody request: EscalateTicketRequest
    ): ResponseEntity<TicketDTO> {
        val updatedTicket = ticketService.escalateTicket(ticketId, request.newTechnicianId)
        return ResponseEntity.ok(updatedTicket)
    }

    @PutMapping("/tickets/{ticketId}/change-state")
    fun changeState(
        @PathVariable ticketId: Long, @RequestBody request: ChangeStateRequest
    ): ResponseEntity<TicketDTO> {
        val stateChanged = ticketService.changeState(ticketId, request)
        return ResponseEntity.ok(stateChanged)
    }

    @GetMapping("/{ticketId}/history")
    fun getTicketHistory(@PathVariable ticketId: Long): ResponseEntity<List<TicketHistoryDTO>> {
        return ResponseEntity.ok(ticketService.getTicketHistory(ticketId))
    }


}