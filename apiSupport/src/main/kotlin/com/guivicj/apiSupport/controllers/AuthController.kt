package com.guivicj.apiSupport.controllers

import com.guivicj.apiSupport.dtos.requests.LoginRequest
import com.guivicj.apiSupport.dtos.responses.LoginResponse
import com.guivicj.apiSupport.dtos.requests.RegisterRequest
import com.guivicj.apiSupport.models.UserModel
import com.guivicj.apiSupport.services.AuthService
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/api/auth")
class AuthController(val authService: AuthService) {

    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequest): ResponseEntity<LoginResponse> {
        val response = authService.login(request)
        return ResponseEntity.status(response.status).body(response)
    }

    @PostMapping("/register")
    fun register(@RequestBody request: RegisterRequest): ResponseEntity<UserModel> {
        val user = authService.register(request)
        return try {
            ResponseEntity.ok(user)
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().build()
        }
    }
}