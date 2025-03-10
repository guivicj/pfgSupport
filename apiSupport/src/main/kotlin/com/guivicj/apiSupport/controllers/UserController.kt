package com.guivicj.apiSupport.controllers

import com.guivicj.apiSupport.dtos.UserDTO
import com.guivicj.apiSupport.services.UserService
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import java.util.*

@Controller
@RequestMapping("/users")
class UserController(val userService: UserService) {

    @GetMapping
    fun getUsers(): ResponseEntity<List<UserDTO>> {
        return ResponseEntity.ok(userService.getUsers())
    }

    @GetMapping("id/{id}")
    fun getUserById(@PathVariable id: Long): ResponseEntity<UserDTO> {
        val user: Optional<UserDTO> = userService.getUserById(id)
        return if (user.isPresent) {
            ResponseEntity.ok(user.get())
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @GetMapping("/name/{name}")
    fun getUserByName(@PathVariable name: String): ResponseEntity<UserDTO> {
        val user: Optional<UserDTO> = userService.getUserByName(name)
        return ResponseEntity.ok(user.get())
    }

    @GetMapping("/email/{email}")
    fun getUserByEmail(@PathVariable email: String): ResponseEntity<UserDTO> {
        val user: Optional<UserDTO> = userService.getUserByEmail(email)
        return ResponseEntity.ok(user.get())
    }

    @PostMapping("/signup")
    fun signUp(@RequestBody dto: UserDTO): ResponseEntity<UserDTO> {
        val user: UserDTO = userService.signUp(dto)
        return ResponseEntity.ok(user)
    }
}
