package com.guivicj.apiSupport.controllers

import com.guivicj.apiSupport.annotations.CurrentUser
import com.guivicj.apiSupport.annotations.RoleRequired
import com.guivicj.apiSupport.dtos.TechDTO
import com.guivicj.apiSupport.dtos.responses.UserSessionInfoDTO
import com.guivicj.apiSupport.enums.TechnicianType
import com.guivicj.apiSupport.enums.UserType
import com.guivicj.apiSupport.services.TechService
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

@Controller
@RequestMapping("/api/tech")
class TechController(val techService: TechService) {

    @GetMapping
    fun getAllTechs(): ResponseEntity<List<TechDTO>> {
        return ResponseEntity.ok(techService.getAllTechs())
    }

    @GetMapping("/id/{id}")
    fun getTechById(@PathVariable id: Long): TechDTO? {
        return techService.getTechById(id)
    }

    @GetMapping("/type/{type}")
    fun getTechsByType(@PathVariable type: TechnicianType): ResponseEntity<List<TechDTO>> {
        return ResponseEntity.ok(techService.getTechsByType(type))
    }

    @RoleRequired(UserType.ADMIN)
    @PostMapping
    fun addTech(@CurrentUser user: UserSessionInfoDTO, @RequestBody dto: TechDTO): ResponseEntity<Any> {
        val tech = techService.addTech(user, dto)
        return ResponseEntity.ok(tech)
    }

    @RoleRequired(UserType.ADMIN)
    @PutMapping("/update/")
    fun updateTech(@CurrentUser user: UserSessionInfoDTO, @RequestBody dto: TechDTO): ResponseEntity<Any> {
        val tech = techService.updateTech(user, dto)
        return ResponseEntity.ok(tech)
    }

    @RoleRequired(UserType.ADMIN)
    @DeleteMapping("/delete")
    fun deleteTech(@CurrentUser user: UserSessionInfoDTO, @RequestBody dto: TechDTO): ResponseEntity<Any> {
        val response = techService.deleteTech(user, dto)
        return ResponseEntity.status(response.status).body(response)
    }

}
