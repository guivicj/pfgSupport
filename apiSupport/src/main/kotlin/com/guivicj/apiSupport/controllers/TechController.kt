package com.guivicj.apiSupport.controllers

import com.guivicj.apiSupport.annotations.CurrentUser
import com.guivicj.apiSupport.dtos.TechDTO
import com.guivicj.apiSupport.dtos.responses.UserSessionInfoDTO
import com.guivicj.apiSupport.enums.TechnicianType
import com.guivicj.apiSupport.enums.UserType
import com.guivicj.apiSupport.services.TechService
import org.springframework.http.HttpStatus
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

    @PostMapping
    fun addTech(@CurrentUser user: UserSessionInfoDTO, @RequestBody dto: TechDTO): ResponseEntity<Any> {
        return if (user.user.type == UserType.ADMIN) {
            val tech = techService.addTech(user, dto)
            ResponseEntity.ok(tech)
        } else {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not an ADMIN")
        }
    }

    @PutMapping("/update/")
    fun updateTech(@CurrentUser user: UserSessionInfoDTO, @RequestBody dto: TechDTO): ResponseEntity<Any> {
        return if (user.user.type == UserType.ADMIN) {
            val tech = techService.updateTech(user, dto)
            ResponseEntity.ok(tech)
        } else {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not an ADMIN")
        }

    }

    @DeleteMapping("/delete")
    fun deleteTech(@CurrentUser user: UserSessionInfoDTO, @RequestBody dto: TechDTO): ResponseEntity<Any> {
        return if (user.user.type == UserType.ADMIN) {
            val response = techService.deleteTech(user, dto)
            ResponseEntity.status(response.status).body(response)
        } else {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not an ADMIN")
        }
    }

}
