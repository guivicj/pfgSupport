package com.guivicj.apiSupport.controllers

import com.guivicj.apiSupport.dtos.requests.DeleteRequest
import com.guivicj.apiSupport.dtos.responses.Response
import com.guivicj.apiSupport.dtos.UserDTO
import com.guivicj.apiSupport.dtos.requests.UserUpdateRequest
import com.guivicj.apiSupport.services.UserService
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

    @DeleteMapping("/delete/{email}")
    fun deleteUser(@PathVariable email: String, @RequestBody deleteRequest: DeleteRequest): ResponseEntity<Response> {
        val response = userService.deleteUser(email, deleteRequest)
        return ResponseEntity.status(response.status).body(response)
    }
}