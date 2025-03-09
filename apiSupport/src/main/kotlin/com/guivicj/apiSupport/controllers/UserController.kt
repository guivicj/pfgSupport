package com.guivicj.apiSupport.controllers

import com.guivicj.apiSupport.dtos.UserDTO
import com.guivicj.apiSupport.services.UserService
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import java.util.*

@Controller
@RequestMapping("/users")
class UserController(val userService: UserService) {

    @GetMapping
    fun getUsers(): ResponseEntity<List<UserDTO>> {
        return ResponseEntity.ok(userService.getUsers())
    }

    @GetMapping("id/{id}")
    fun getUserById(@PathVariable userId: Long): ResponseEntity<UserDTO> {
        var user: Optional<UserDTO> = userService.getUserById(userId)
        return ResponseEntity.ok(user.get())
    }

    @GetMapping("/name/{name}")
    fun getUserByName(@PathVariable name: String): ResponseEntity<UserDTO> {
        var user: Optional<UserDTO> = userService.getUserByName(name)
        return ResponseEntity.ok(user.get())
    }

    @GetMapping("/email/{email}")
    fun getUserByEmail(@PathVariable email: String): ResponseEntity<UserDTO> {
        var user: Optional<UserDTO> = userService.getUserByEmail(email)
        return ResponseEntity.ok(user.get())
    }
}
