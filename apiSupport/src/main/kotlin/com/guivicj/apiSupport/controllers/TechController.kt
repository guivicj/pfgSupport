package com.guivicj.apiSupport.controllers

import com.guivicj.apiSupport.dtos.TechDTO
import com.guivicj.apiSupport.dtos.requests.DeleteTechRequest
import com.guivicj.apiSupport.dtos.requests.TechRequest
import com.guivicj.apiSupport.dtos.requests.UpdateTechRequest
import com.guivicj.apiSupport.dtos.responses.DeleteTechResponse
import com.guivicj.apiSupport.enums.TechnicianType
import com.guivicj.apiSupport.services.TechService
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import java.util.*

@Controller
@RequestMapping("/api/tech")
class TechController(val techService: TechService) {

    @GetMapping
    fun getAllTechs(): ResponseEntity<List<TechDTO>> {
        return ResponseEntity.ok(techService.getAllTechs())
    }

    @GetMapping("/id/{id}")
    fun getTechById(@PathVariable id: Long): Optional<TechDTO> {
        return techService.getTechById(id)
    }

    @GetMapping("/type/{type}")
    fun getTechsByType(@PathVariable type: TechnicianType): ResponseEntity<List<TechDTO>> {
        return ResponseEntity.ok(techService.getTechsByType(type))
    }

    @PostMapping
    fun addTech(@RequestBody techRequest: TechRequest): ResponseEntity<TechDTO> {
        val tech = techService.addTech(techRequest)
        return ResponseEntity.ok(tech)
    }

    @PutMapping("/update/")
    fun updateTech(@RequestBody techRequest: UpdateTechRequest): ResponseEntity<TechDTO> {
        val tech = techService.updateTech(techRequest)
        return ResponseEntity.ok(tech)
    }

    @DeleteMapping("/delete/")
    fun deleteTech(@RequestBody techRequest: DeleteTechRequest): ResponseEntity<DeleteTechResponse> {
        val response = techService.deleteTech(techRequest)
        return ResponseEntity.status(response.status).body(response)
    }

}