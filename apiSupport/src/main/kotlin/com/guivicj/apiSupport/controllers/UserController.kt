package com.guivicj.apiSupport.controllers

import com.guivicj.apiSupport.annotations.CurrentUser
import com.guivicj.apiSupport.annotations.RoleRequired
import com.guivicj.apiSupport.dtos.UserDTO
import com.guivicj.apiSupport.dtos.requests.UserUpdateRequest
import com.guivicj.apiSupport.dtos.responses.UserSessionInfoDTO
import com.guivicj.apiSupport.enums.UserType
import com.guivicj.apiSupport.services.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import java.util.*

@Controller
@RequestMapping("/api/users")
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

    @PutMapping("/update/{email}")
    fun updateUser(
        @PathVariable email: String,
        @RequestBody updateRequest: UserUpdateRequest
    ): ResponseEntity<UserDTO> {
        val user = userService.updateUser(email, updateRequest)
        return ResponseEntity.ok(user)
    }

    @RoleRequired(UserType.ADMIN)
    @DeleteMapping("/delete/{email}")
    fun deleteUser(
        @CurrentUser user: UserSessionInfoDTO,
        @PathVariable email: String
    ): ResponseEntity<Any> {
            val response = userService.deleteUser(email)
            return ResponseEntity.status(response.status).body(response)
    }
}
